package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.dto.Instruction;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.DriverStatus;
import com.marta.logistika.enums.OrderStatus;
import com.marta.logistika.enums.TripTicketStatus;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.api.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.marta.logistika.dto.Instruction.*;
import static com.marta.logistika.dto.Instruction.Task.*;
import static com.marta.logistika.enums.DriverStatus.*;

@Component
class TripTicketServiceHelper {

    private final static int SPEED = 60;
    private final static Duration STOPOVER_DURATION = Duration.ofHours(2);

    private final TripTicketDao ticketDao;
    private final RoadService roadService;
    private final TimeTrackerService timeService;

    @Autowired
    public TripTicketServiceHelper(TripTicketDao ticketDao, RoadService roadService, TimeTrackerService timeService) {
        this.ticketDao = ticketDao;
        this.roadService = roadService;
        this.timeService = timeService;
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
    int[] suggestLoadUnloadPoints (TripTicketEntity ticketEntity, OrderEntity order) {

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

    float getAvgLoad (TripTicketEntity ticket) {
        List<CityEntity> route = ticket.getStopovers().stream().map(StopoverEntity::getCity).collect(Collectors.toList());
        List<Integer> weights = ticket.getStopovers().stream().map(StopoverEntity::getTotalWeight).collect(Collectors.toList());
        return getAvgLoad(route, weights);
    }

    private float getAvgLoad (List<CityEntity> route, List<Integer> weights) {
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
    LocalDateTime calculateDurationAndArrivalDateTime(TripTicketEntity ticket) {

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

        LocalDateTime estimatedArrivalTime = ticket.getDepartureDateTime().plusMinutes(totalTripDuration.toMinutes());
        ticket.setArrivalDateTime(estimatedArrivalTime);
        return estimatedArrivalTime;
    }

    /**
     * Method removes stopovers with no load / unload operations from the trip tickets.
     * The endpoints (start and finish) are not removed even if empty.
     * @param ticket ticket to be processed
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void removeEmptyStopovers(TripTicketEntity ticket) {
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
     * Method is used when next stage in the trip ticket is completed.
     * It updates statuses for all the drivers assigned to the ticket,
     * closes their open time records and opens new time records if the trip ticket is not yet fully completed.
     * @param ticket to be updated
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void updateDriversTimeRecords(TripTicketEntity ticket) {
        // get actual task for the ticket
        Instruction.Task currentTask = getCurrentTask(ticket);

        boolean isFirstDriverSet = false;

        for (int i = 0; i < ticket.getDrivers().size(); i++) {
            DriverEntity driver = ticket.getDrivers().get(i);
            DriverStatus previousDriverStatus = driver.getStatus();

            // update driver status
            switch (currentTask) {
//                case START:
                case GOTO:
                    if(isFirstDriverSet) {
                        driver.setStatus(SECONDING);
                    } else {
                        driver.setStatus(DRIVING);
                        isFirstDriverSet = true;
                    }
                    break;
                case LOAD:
                case UNLOAD:
                    if(driver.getStatus() != RESTING) driver.setStatus(HANDLING);
                    break;
                case FINISH:
                    driver.setStatus(OFFLINE);
                    break;
                default:
                    throw new RuntimeException(String.format("Invalid task %s for ticket id %d", currentTask, ticket.getId()));
            }

            //update driver time records
            if(driver.getStatus() != RESTING) {
                if (previousDriverStatus == OFFLINE) timeService.openNewTimeRecord(driver);
                else if (currentTask == FINISH) timeService.closeTimeRecord(driver);
                else timeService.closeReopenTimeRecord(driver);
            }
        }
    }


    /**
     * Checks if all ticket tasks have been completed, and if yes - closes the ticket
     * and updates booking information for the truck and the drivers
     * @param ticket trip ticket to be checked
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void checkTicketCompletion(TripTicketEntity ticket) {
        if(getCurrentTask(ticket) == FINISH) {
            ticket.setStatus(TripTicketStatus.CLOSED);

            TripTicketEntity nextTripForThisTruck = ticketDao.findByTruckAndStatus(ticket.getTruck().getRegNumber(), TripTicketStatus.APPROVED);
            if(nextTripForThisTruck == null) {
                ticket.getTruck().setBookedUntil(LocalDateTime.now());
                ticket.getTruck().setParked(true);
            }

            ticket.getDrivers().forEach(d -> {
                TripTicketEntity nextTripForThisDriver = ticketDao.findByDriverAndStatus(d.getPersonalId(), TripTicketStatus.APPROVED);
                if(nextTripForThisDriver == null) {
                    d.setBookedUntil(LocalDateTime.now());
                }
            });
        }
    }

    /**
     * Method determines current task for the trip ticket
     * @param ticket trip ticket
     * @return current task
     */
    Task getCurrentTask(TripTicketEntity ticket) {
        int currentStep = ticket.getCurrentStep();
        if(currentStep == -1) return START;
        if(currentStep >= ticket.getStopovers().size()) return NONE;

        StopoverEntity currentStopover = ticket.getStopoverWithSequenceNo(ticket.getCurrentStep());
        if(currentStopover.getUnloads().stream().map(TransactionEntity::getOrder).anyMatch(o -> o.getStatus() == OrderStatus.SHIPPED)) return UNLOAD;
        if(currentStopover.getLoads().stream().map(TransactionEntity::getOrder).anyMatch(o -> o.getStatus() == OrderStatus.READY_TO_SHIP)) return LOAD;
        if(currentStep == ticket.getStopovers().size() - 1) return FINISH;
        return GOTO;
    }
}
