package com.marta.logistika.service.impl;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.StopoverEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.service.ServiceException;
import com.marta.logistika.service.api.RoadService;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

class TripTicketServiceHelper {

    private final static int SPEED = 60;
    private final static Duration STOPOVER_DURATION = Duration.ofHours(2);

    private final RoadService roadService;

    TripTicketServiceHelper(RoadService roadService) {
        this.roadService = roadService;
    }

    /**
     * Method suggests best placement of new load and unload points on the route to minimize total route distance
     * @param currentRoute - current route (list of cities)
     * @param fromCity - new stopover to be added for loading
     * @param toCity - new stopover to be added for unloading
     * @return returns an array containing suggested load and unload points:
     *             load on or immediately after current route element number "load"
     *             unload on or immediately after current route element number "unload"
     */
    int[] suggestLoadUnloadPoints (List<CityEntity> currentRoute, CityEntity fromCity, CityEntity toCity) {

        int loadPoint = 0;
        int unloadPoint = 0;
        int distance = Integer.MAX_VALUE;

        for (int i = 0; i < currentRoute.size() - 1; i++) {
            currentRoute.add(i + 1, fromCity);
            for (int j = i + 1; j < currentRoute.size() - 1; j++) {
                currentRoute.add(j + 1, toCity);
                int tmpRouteDistance = roadService.getRouteDistance(currentRoute);
                if (tmpRouteDistance < distance) {
                    loadPoint = i;
                    unloadPoint = j - 1;
                    distance = tmpRouteDistance;
                }
                currentRoute.remove(j + 1);
            }
            currentRoute.remove(i + 1);
        }

        return new int[] {loadPoint, unloadPoint};
    }

    /**
     * Method inserts new stopover into a trip ticket route and updates sequencing for the remaining stopovers in the route
     * @param ticket - trip ticket to be updated
     * @param city - place for a new stopover
     * @param sequenceNo - indication where the new stopover should be inserted
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void insertNewStopover(TripTicketEntity ticket, CityEntity city, int sequenceNo) {

        ticket.getStopovers().forEach(s -> {
            if(s.getSequenceNo() >= sequenceNo) s.setSequenceNo(s.getSequenceNo() + 1);
        });
        StopoverEntity newStopover = new StopoverEntity();

        newStopover.setSequenceNo(sequenceNo);
        newStopover.setCity(city);
        ticket.getStopovers().add(newStopover);
    }

    /**
     * Method recalculates total weight at the end of each stopover for a given tip ticket number
     * @param ticket trip ticket to be updated
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    void updateWeights(TripTicketEntity ticket) {
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
     * @throws ServiceException is thrown in case total weight exceeds truck capacity limit at any stopover
     */
    void checkWeightLimit (TripTicketEntity ticket) throws ServiceException {
        for (StopoverEntity s : ticket.getStopovers()) {
            if (s.getTotalWeight() > ticket.getTruck().getCapacity()) {
                throw new ServiceException(String.format("at stopover no %d %s total weight gets to %d kg while truck capacity is %d kg",
                        s.getSequenceNo(), s.getCity().getName(), s.getTotalWeight(), ticket.getTruck().getCapacity()));
            }
        }
    }

    /**
     * Method calculates estimated duration for each stopover in the trip ticket and total trip duration
     * and updates estimated arrival time accordingly
     * @param ticket trip ticket to be processed
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void calculateDurationAndArrivalDateTime(TripTicketEntity ticket) {

        List<StopoverEntity> stopovers = ticket.getStopovers();
        stopovers.sort(StopoverEntity::compareTo);

        stopovers.get(0).setEstimatedDuration(STOPOVER_DURATION);
        Duration totalTripDuration = STOPOVER_DURATION;

        for (int i = 1; i < stopovers.size(); i++) {
            Duration currentStopoverDuration = STOPOVER_DURATION
                    .plusMinutes(roadService.getDistanceFromTo(stopovers.get(i).getCity(), stopovers.get(i-1).getCity()) / SPEED * 60);
            stopovers.get(i).setEstimatedDuration(currentStopoverDuration);
            totalTripDuration = totalTripDuration.plus(currentStopoverDuration);
        }

        ticket.setArrivalDate(ticket.getDepartureDate().plusMinutes(totalTripDuration.toMinutes()));
    }
}
