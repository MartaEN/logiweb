package com.marta.logistika.service.impl.helpers;

import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.StopoverEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.exception.checked.NoRouteFoundException;
import com.marta.logistika.exception.checked.OrderDoesNotFitToTicketException;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.api.TimeTrackerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StopoverHelper {

    private final RoadService roadService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TripTicketServiceHelper.class);

    @Autowired
    public StopoverHelper(RoadService roadService) {
        this.roadService = roadService;
    }

    /**
     * Method suggests best placement of new load and unload points on the route
     * to minimize total route distance and average load on the way.
     * @param ticketEntity - ticket to be updated
     * @param order - order to be placed to ticket
     * @return returns an array containing suggested load and unload points:
     *             load on or immediately after current route element number "load"
     *             unload on or immediately before current route element number "unload"
     */
    int[] suggestLoadUnloadPoints (TripTicketEntity ticketEntity, OrderEntity order) throws NoRouteFoundException {
        LOGGER.debug(String.format("###LOGIWEB### TripTicketServiceHelper::suggestLoadUnloadPoints(ticket::%d,order::%d)", ticketEntity.getId(), order.getId()));

        List<CityEntity> currentRoute = ticketEntity.getCities();
        List<Integer> weights = ticketEntity.getStopovers().stream().map(StopoverEntity::getTotalWeight).collect(Collectors.toList());
        CityEntity fromCity = order.getFromCity();
        CityEntity toCity = order.getToCity();
        int newWeight = order.getWeight();

        int loadPoint = 0;
        int unloadPoint = 0;
        int distance = Integer.MAX_VALUE;
        float load = (float) Integer.MAX_VALUE;

        for (int i = 0; i < currentRoute.size() - 1; i++) {
            if (currentRoute.size() > 2 && currentRoute.get(i+1).equals(fromCity)) continue;
            currentRoute.add(i + 1, fromCity);

            for (int j = i + 1; j < currentRoute.size() - 1; j++) {
                if(currentRoute.get(j).equals(toCity)) continue;
                currentRoute.add(j + 1, toCity);
                int tmpRouteDistance = roadService.getRouteDistance(currentRoute);

                if (tmpRouteDistance <= distance) {
                    weights.add(i + 1, weights.get(i));
                    weights.add(j + 1, weights.get(j));
                    for (int k = i + 1; k < currentRoute.size(); k++) {
                        weights.set(k, weights.get(k) + newWeight);
                    }
                    for (int k = j + 1; k < currentRoute.size(); k++) {
                        weights.set(k, weights.get(k) - newWeight);
                    }
                    float tmpLoad = getAvgLoad(currentRoute, weights);
                    if (tmpRouteDistance < distance || (tmpRouteDistance == distance && tmpLoad < load)) {
                        loadPoint = i;
                        unloadPoint = j;
                        distance = tmpRouteDistance;
                        load = tmpLoad;
                    }
                    for (int k = j + 1; k < currentRoute.size(); k++) {
                        weights.set(k, weights.get(k) + newWeight);
                    }
                    for (int k = i + 1; k < currentRoute.size(); k++) {
                        weights.set(k, weights.get(k) - newWeight);
                    }
                    weights.remove(j + 1);
                    weights.remove(i + 1);
                }
                currentRoute.remove(j + 1);
            }
            currentRoute.remove(i + 1);
        }

        return new int[] {loadPoint, unloadPoint};

    }

    /**
     * Calculates weighted average load for the truck
     * @param ticket trip ticket ticket
     * @throws NoRouteFoundException in case no route found
     * @return weighted average truck load
     */
    float getAvgLoad (TripTicketEntity ticket) throws NoRouteFoundException {
        LOGGER.debug(String.format("###LOGIWEB### TripTicketServiceHelper::getAvgLoad(ticket::%d)", ticket.getId()));
        List<CityEntity> route = ticket.getStopovers().stream().map(StopoverEntity::getCity).collect(Collectors.toList());
        List<Integer> weights = ticket.getStopovers().stream().map(StopoverEntity::getTotalWeight).collect(Collectors.toList());
        return getAvgLoad(route, weights);
    }

    private float getAvgLoad (List<CityEntity> route, List<Integer> weights) throws NoRouteFoundException {
        int totalDistance = 0;
        float totalLoad = 0;
        for (int i = 0; i < route.size() - 1; i++) {
            int distance = roadService.getDistanceFromTo(route.get(i), route.get(i+1));
            totalDistance += distance;
            totalLoad += distance * weights.get(i);
        }
        return (float) totalLoad / totalDistance;
    }

    /**
     * Method inserts new stopover into a trip ticket route and updates sequencing for the remaining stopovers in the route
     * @param ticket - trip ticket to be updated
     * @param city - place for a new stopover
     * @param sequenceNo - indication where the new stopover should be inserted
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void insertNewStopover(TripTicketEntity ticket, CityEntity city, int sequenceNo) {
        LOGGER.debug(String.format("###LOGIWEB### TripTicketServiceHelper::insertNewStopover(ticket::%d,city::%s,sequenceNo::%d)", ticket.getId(), city.getName(), sequenceNo));
        ticket.getStopovers().forEach(s -> {
            if(s.getSequenceNo() >= sequenceNo) s.setSequenceNo(s.getSequenceNo() + 1);
        });
        StopoverEntity newStopover = new StopoverEntity();
        newStopover.setSequenceNo(sequenceNo);
        newStopover.setCity(city);
        ticket.getStopovers().add(newStopover);
    }

    /**
     * Method removes stopovers with no load / unload operations from the trip tickets.
     * The endpoints (start and finish) are not removed even if empty.
     * @param ticket ticket to be processed
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeEmptyStopovers(TripTicketEntity ticket) {
        LOGGER.debug(String.format("###LOGIWEB### TripTicketServiceHelper::removeEmptyStopovers(ticket::%d)", ticket.getId()));
        for (int i = ticket.getStopovers().size() - 2 ; i > 0 ; i--) {
            StopoverEntity stopover = ticket.getStopovers().get(i);
            if (stopover.getLoads().size() == 0 && stopover.getUnloads().size() == 0)
                ticket.getStopovers().remove(stopover);
        }
        for (int i = 0; i < ticket.getStopovers().size(); i++) {
            ticket.getStopovers().get(i).setSequenceNo(i);
        }
    }

    /**
     * Method recalculates total weight at the end of each stopover for a given tip ticket number
     * @param ticket trip ticket to be updated
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void updateWeights(TripTicketEntity ticket) {
        LOGGER.debug(String.format("###LOGIWEB### TripTicketServiceHelper::updateWeights(ticket::%d)", ticket.getId()));
        int cumulativeWeight = 0;
        List<StopoverEntity> route = ticket.getStopovers();
        route.sort(StopoverEntity::compareTo);
        for (StopoverEntity s : route) {
            s.setTotalWeight(cumulativeWeight += s.getIncrementalWeight());
        }
    }

    /**
     * Method checks total weight at the end of each trip stopover against truck weight limit
     * @param ticket trip ticket to be validated
     * @throws OrderDoesNotFitToTicketException is thrown in case total weight exceeds truck capacity limit at any stopover
     */
    void checkWeightLimit (TripTicketEntity ticket) throws OrderDoesNotFitToTicketException {
        LOGGER.debug(String.format("###LOGIWEB### TripTicketServiceHelper::checkWeightLimit(ticket::%d)", ticket.getId()));
        for (StopoverEntity s : ticket.getStopovers()) {
            if (s.getTotalWeight() > ticket.getTruck().getCapacity()) {
                throw new OrderDoesNotFitToTicketException(ticket.getId(),
                        String.format("at stopover no %d %s total weight gets to %d kg while truck capacity is %d kg",
                                s.getSequenceNo(), s.getCity().getName(), s.getTotalWeight(), ticket.getTruck().getCapacity()));
            }
        }
    }


}
