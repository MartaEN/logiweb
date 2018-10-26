package com.marta.logistika.service.impl.helpers;

import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.dto.Instruction;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.DriverStatus;
import com.marta.logistika.event.EntityUpdateEvent;
import com.marta.logistika.exception.checked.NoRouteFoundException;
import com.marta.logistika.exception.checked.OrderDoesNotFitToTicketException;
import com.marta.logistika.exception.unchecked.UncheckedServiceException;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.api.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.marta.logistika.dto.Instruction.Task;
import static com.marta.logistika.dto.Instruction.Task.*;
import static com.marta.logistika.enums.DriverStatus.*;
import static com.marta.logistika.enums.OrderStatus.*;
import static com.marta.logistika.enums.TripTicketStatus.*;

@Component
public class TripTicketServiceHelper {

    private final static int SPEED = 60;
    private final static Duration STOPOVER_DURATION = Duration.ofHours(2);

    private final TripTicketDao ticketDao;
    private final RoadService roadService;
    private final TimeTrackerService timeService;
    private final StopoverHelper stopoverHelper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TripTicketServiceHelper(TripTicketDao ticketDao, RoadService roadService, TimeTrackerService timeService, StopoverHelper stopoverHelper, ApplicationEventPublisher applicationEventPublisher) {
        this.ticketDao = ticketDao;
        this.roadService = roadService;
        this.timeService = timeService;
        this.stopoverHelper = stopoverHelper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Adds order to trip ticket, minimizing the total trip distance and checking truck capacity limits
     *
     * @param order  order that has to be placed to the ticket
     * @param ticket trip ticket that has to intake new order
     * @throws NoRouteFoundException            in case order destination point can't be reached from trip ticket starting point with existing roads
     * @throws OrderDoesNotFitToTicketException in case order doesn't fit to ticket due to truck capacity limit
     *                                          and in case of weight limit breakage
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void addOrderToTicket(OrderEntity order, TripTicketEntity ticket) throws NoRouteFoundException, OrderDoesNotFitToTicketException {
        // check order and ticket statuses
        if (ticket.getStatus() != CREATED)
            throw new UncheckedServiceException(String.format("Can't add to trip ticket id %d - ticket has already been approved", ticket.getId()));
        if (order.getStatus() != NEW)
            throw new UncheckedServiceException(String.format("Order id %d has already been assigned to some trip ticket", order.getId()));

        // find suggested load and unload stopovers for the order
        int[] suggestedLoadUnloadForNewOrder = stopoverHelper.suggestLoadUnloadPoints(ticket, order);
        int loadPoint = suggestedLoadUnloadForNewOrder[0];
        int unloadPoint = suggestedLoadUnloadForNewOrder[1];

        // add load and unload operations for the order
        if (!ticket.getStopoverWithSequenceNo(loadPoint).getCity().equals(order.getFromCity())) {
            ++unloadPoint;
            stopoverHelper.insertNewStopover(ticket, order.getFromCity(), ++loadPoint);
        }
        ticket.getStopoverWithSequenceNo(loadPoint).addLoadFor(order);

        if (!ticket.getStopoverWithSequenceNo(unloadPoint).getCity().equals(order.getToCity())) {
            stopoverHelper.insertNewStopover(ticket, order.getToCity(), unloadPoint);
        }
        ticket.getStopoverWithSequenceNo(unloadPoint).addUnloadFor(order);

        // update weights and check against truck capacity
        stopoverHelper.updateWeights(ticket);
        try {
            stopoverHelper.checkWeightLimit(ticket);
        } catch (OrderDoesNotFitToTicketException e) {
            throw new OrderDoesNotFitToTicketException(e, order.getId());
        }
        ticket.setAvgLoad((int) (stopoverHelper.getAvgLoad(ticket) / ticket.getTruck().getCapacity() * 100));

        // update order status in case of successful weight check
        order.setStatus(ASSIGNED);

        //publish update event
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

    /**
     * Method calculates estimated duration for each stopover in the trip ticket and total trip duration
     * and updates estimated arrival time accordingly
     *
     * @param ticket trip ticket to be processed
     * @return estimated arrival time
     * @throws NoRouteFoundException in case some of the stopover are not linked with roads
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public LocalDateTime calculateDurationAndArrivalDateTime(TripTicketEntity ticket) throws NoRouteFoundException {
        List<StopoverEntity> stopovers = ticket.getStopovers();
        stopovers.sort(StopoverEntity::compareTo);

        stopovers.get(0).setEstimatedDuration(STOPOVER_DURATION);
        Duration totalTripDuration = STOPOVER_DURATION;

        for (int i = 1; i < stopovers.size(); i++) {
            Duration currentStopoverDuration = STOPOVER_DURATION
                    .plusMinutes(roadService.getDistanceFromTo(stopovers.get(i).getCity(), stopovers.get(i - 1).getCity()) / SPEED * 60);
            stopovers.get(i).setEstimatedDuration(currentStopoverDuration);
            totalTripDuration = totalTripDuration.plus(currentStopoverDuration);
        }

        LocalDateTime estimatedArrivalTime = ticket.getDepartureDateTime().plusMinutes(totalTripDuration.toMinutes());
        ticket.setArrivalDateTime(estimatedArrivalTime);
        return estimatedArrivalTime;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeOrderFromTicket(OrderEntity order, TripTicketEntity ticket) {
        for (int i = 0; i < ticket.getStopovers().size(); i++) {

            StopoverEntity stopover = ticket.getStopovers().get(i);

            for (int j = 0; j < stopover.getLoads().size(); j++) {
                if (stopover.getLoads().get(j).getOrder().equals(order))
                    stopover.getLoads().remove(j);
            }

            for (int j = 0; j < stopover.getUnloads().size(); j++) {
                if (stopover.getUnloads().get(j).getOrder().equals(order))
                    stopover.getUnloads().remove(j);
            }
        }

        stopoverHelper.removeEmptyStopovers(ticket);
        stopoverHelper.updateWeights(ticket);

        try {
            ticket.setAvgLoad((int) (stopoverHelper.getAvgLoad(ticket) / ticket.getTruck().getCapacity() * 100));
        } catch (NoRouteFoundException e) {
            // unreachable statement unless somebody drops the total database
        }
    }


    /**
     * Method is used when next stage in the trip ticket is completed.
     * It updates statuses for all the drivers assigned to the ticket,
     * closes their open time records and opens new time records if the trip ticket is not yet fully completed.
     *
     * @param ticket to be updated
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateDriversTimeRecordsForNewStage(TripTicketEntity ticket) {
        // get actual task for the ticket
        Instruction.Task currentTask = getCurrentTask(ticket);

        boolean isFirstDriverSet = false;

        // if ticket is closing, all drivers are set offline
        if (currentTask == CLOSE_TICKET) {
            ticket.getDrivers().forEach(driver -> {
                driver.setStatus(OFFLINE);
                timeService.closeTimeRecord(driver);
            });
            applicationEventPublisher.publishEvent(new EntityUpdateEvent());
            return;
        }

        //otherwise define new driver status for each driver
        for (int i = 0; i < ticket.getDrivers().size(); i++) {
            DriverEntity driver = ticket.getDrivers().get(i);
            DriverStatus previousDriverStatus = driver.getStatus();

            // update driver statuses
            switch (driver.getStatus()) {
                case OFFLINE:
                case STOPOVER_BREAK:
                case ROAD_BREAK:
                    break;
                default:
                    switch (currentTask) {
                        case LOAD:
                        case UNLOAD:
                            driver.setStatus(HANDLING);
                            break;
                        case GOTO:
                            boolean anyOtherDriverMissing = ticket.getDrivers().stream().anyMatch(d -> d.getStatus() == STOPOVER_BREAK || d.getStatus() == OFFLINE);
                            if (anyOtherDriverMissing) {
                                driver.setStatus(WAITING);
                            } else if (isFirstDriverSet) {
                                driver.setStatus(SECONDING);
                            } else {
                                driver.setStatus(DRIVING);
                                isFirstDriverSet = true;
                            }
                            break;
                        default:
                            throw new RuntimeException(String.format("Invalid task %s for ticket id %d", currentTask, ticket.getId()));
                    }
            }

            //update driver time records
            if (!driver.getStatus().equals(previousDriverStatus)) timeService.closeReopenTimeRecord(driver);
        }
    }


    /**
     * Method registers the reporting driver as the first (driving) one, and changes all the rest to seconding status.
     *
     * @param ticket           trip ticket
     * @param initiatingDriver reporting driver
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setFirstDriver(TripTicketEntity ticket, DriverEntity initiatingDriver) {
        if (getCurrentTask(ticket).equals(GOTO)) {
            if (initiatingDriver.getStatus().equals(SECONDING)) {
                ticket.getDrivers().forEach(driver -> {
                    if (driver.equals(initiatingDriver)) driver.setStatus(DRIVING);
                    else driver.setStatus(SECONDING);
                    timeService.closeReopenTimeRecord(driver);
                });
            } else {
                throw new UncheckedServiceException(String.format("Driver role changes can be only made when ticket current task is 'goto'. Ticket id %d has %s task",
                        ticket.getId(), getCurrentTask(ticket)));
            }
        } else {
            throw new UncheckedServiceException(String.format("Changing to first driver can only be made from seconding status. Driver %s has %s status",
                    initiatingDriver, initiatingDriver.getStatus()));
        }
    }

    /**
     * Method registers driver going online (opening his next shift)
     *
     * @param initiatingDriver requesting driver
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void setOnline(DriverEntity initiatingDriver, TripTicketEntity ticket) {
        if (initiatingDriver.getStatus() != OFFLINE)
            throw new UncheckedServiceException(String.format("Driver can only go online from offline status. Driver %s has %s status", initiatingDriver, initiatingDriver.getStatus()));
        Task currentTask = getCurrentTask(ticket);
        switch (currentTask) {
            case LOAD:
            case UNLOAD:
                initiatingDriver.setStatus(HANDLING);
                break;
            case GOTO:
                boolean anyOtherDriversOffline = ticket.getDrivers().stream()
                        .filter(d -> !d.equals(initiatingDriver))
                        .anyMatch(d -> d.getStatus() == OFFLINE || d.getStatus() == STOPOVER_BREAK || d.getStatus() == ROAD_BREAK);
                if (anyOtherDriversOffline) {
                    initiatingDriver.setStatus(WAITING);
                } else {
                    initiatingDriver.setStatus(DRIVING);
                    ticket.getDrivers().forEach(otherDriver -> {
                        if (!otherDriver.equals(initiatingDriver)) {
                            otherDriver.setStatus(SECONDING);
                            timeService.closeReopenTimeRecord(otherDriver);
                        }
                    });
                }
                break;
            default:
                throw new UncheckedServiceException("No ticket tasks for driver going online");
        }
        timeService.openNewTimeRecord(initiatingDriver);
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

    /**
     * Method registers the road break for the ticket
     *
     * @param ticket to be updated
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void startRoadBreak(TripTicketEntity ticket) {
        ticket.getDrivers().forEach(driver -> {
            switch (driver.getStatus()) {
                case DRIVING:
                case SECONDING:
                    driver.setStatus(ROAD_BREAK);
                    timeService.closeReopenTimeRecord(driver);
                    break;
                case HANDLING:
                case ROAD_BREAK:
                case STOPOVER_BREAK:
                case OFFLINE:
                    throw new UncheckedServiceException(String.format("Road break can be only taken from driving or seconding status. Driver %s has %s status",
                            driver, driver.getStatus()));
            }
        });
    }

    /**
     * Method registers the road break is over (statuses are updated for all truck drivers)
     *
     * @param ticket           to be updated
     * @param initiatingDriver initiating driver will be set as the driving one after break
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void finishRoadBreak(TripTicketEntity ticket, DriverEntity initiatingDriver) {
        if (getCurrentTask(ticket).equals(GOTO)) {
            for (int i = 0; i < ticket.getDrivers().size(); i++) {
                DriverEntity driver = ticket.getDrivers().get(i);
                if (driver.equals(initiatingDriver)) {
                    driver.setStatus(DRIVING);
                } else {
                    driver.setStatus(SECONDING);
                }
                timeService.closeReopenTimeRecord(driver);
            }
        } else {
            throw new UncheckedServiceException(String.format(
                    "Road break can only be taken while ticket task is 'GOTO';  ticket id %d current task is %s",
                    ticket.getId(), getCurrentTask(ticket)));
        }
    }

    /**
     * Method registers the start of stopover break for the driver
     *
     * @param driver reporting driver
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void startStopoverBreak(DriverEntity driver) {
        if (driver.getStatus().equals(HANDLING)) {
            driver.setStatus(STOPOVER_BREAK);
            timeService.closeReopenTimeRecord(driver);
        } else {
            throw new UncheckedServiceException(String.format("Stopover break can be only taken from handling status. Driver %s has %s status",
                    driver, driver.getStatus()));
        }
    }

    /**
     * Method registers the end of stopover break for the driver
     *
     * @param driver reporting driver
     * @param ticket trip ticket
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void finishStopoverBreak(DriverEntity driver, TripTicketEntity ticket) {
        Task currentTask = getCurrentTask(ticket);
        switch (currentTask) {
            case LOAD:
            case UNLOAD:
                driver.setStatus(HANDLING);
                timeService.closeReopenTimeRecord(driver);
                break;
            case GOTO:
                boolean anyOtherDriversOffline = ticket.getDrivers().stream()
                        .filter(d -> !d.equals(driver))
                        .anyMatch(d -> d.getStatus() == OFFLINE || d.getStatus() == STOPOVER_BREAK || d.getStatus() == ROAD_BREAK);
                if (anyOtherDriversOffline) {
                    driver.setStatus(WAITING);
                } else {
                    driver.setStatus(DRIVING);
                    for (DriverEntity otherDriver : ticket.getDrivers()) {
                        if (!otherDriver.equals(driver)) otherDriver.setStatus(SECONDING);
                    }
                }
                break;
            default:
                throw new UncheckedServiceException("No ticket tasks for driver returning from stopover break");
        }
    }

    /**
     * Checks if all ticket tasks have been completed, and if yes - closes the ticket
     * and updates booking information for the truck and the drivers
     *
     * @param ticket trip ticket to be checked
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void checkTicketCompletion(TripTicketEntity ticket) {
        Task currentTask = getCurrentTask(ticket);
        if (currentTask == CLOSE_TICKET) {
            //update ticket status
            ticket.setStatus(CLOSED);

            //update truck status and availability
            TripTicketEntity nextTripForThisTruck = ticketDao.findByTruckAndStatus(ticket.getTruck().getRegNumber(), APPROVED);
            if (nextTripForThisTruck == null) {
                ticket.getTruck().setBookedUntil(LocalDateTime.now());
                ticket.getTruck().setParked(true);
            }

            //update each driver status and availability
            ticket.getDrivers().forEach(d -> {
                TripTicketEntity nextTripForThisDriver = ticketDao.findByDriverAndStatus(d.getPersonalId(), APPROVED);
                if (nextTripForThisDriver == null) {
                    d.setBookedUntil(LocalDateTime.now());
                }
            });

            //publish update event
            applicationEventPublisher.publishEvent(new EntityUpdateEvent());
        }
    }

    /**
     * Method determines current task for the trip ticket
     *
     * @param ticket trip ticket
     * @return current task
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Task getCurrentTask(TripTicketEntity ticket) {
        if (ticket.getStatus() != APPROVED && ticket.getStatus() == RUNNING) return NONE;
        StopoverEntity currentStopover = ticket.getStopoverWithSequenceNo(ticket.getCurrentStep());
        if (currentStopover.getUnloads().stream().map(TransactionEntity::getOrder).anyMatch(o -> o.getStatus() == SHIPPED))
            return UNLOAD;
        if (currentStopover.getLoads().stream().map(TransactionEntity::getOrder).anyMatch(o -> o.getStatus() == READY_TO_SHIP))
            return LOAD;
        if (ticket.getCurrentStep() == ticket.getStopovers().size() - 1) return CLOSE_TICKET;
        return GOTO;
    }

}
