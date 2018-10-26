package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.DriverDao;
import com.marta.logistika.dao.api.OrderDao;
import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.dto.Instruction;
import com.marta.logistika.dto.OrderRecordDriverInstruction;
import com.marta.logistika.dto.SystemMessage;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.DriverStatus;
import com.marta.logistika.enums.OrderStatus;
import com.marta.logistika.event.EntityUpdateEvent;
import com.marta.logistika.exception.checked.CheckedServiceException;
import com.marta.logistika.exception.checked.NoDriversAvailableException;
import com.marta.logistika.exception.checked.NoRouteFoundException;
import com.marta.logistika.exception.checked.PastDepartureDateException;
import com.marta.logistika.exception.unchecked.EntityNotFoundException;
import com.marta.logistika.exception.unchecked.UncheckedServiceException;
import com.marta.logistika.service.api.TripTicketService;
import com.marta.logistika.service.impl.helpers.TripTicketServiceHelper;
import com.marta.logistika.service.impl.helpers.WebsocketUpdateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import static com.marta.logistika.dto.Instruction.Task;
import static com.marta.logistika.dto.Instruction.Task.*;
import static com.marta.logistika.enums.TripTicketStatus.*;

@Service("tripTicketService")
public class TripTicketServiceImpl extends AbstractService implements TripTicketService {

    private final TripTicketDao ticketDao;
    private final TruckDao truckDao;
    private final DriverDao driverDao;
    private final OrderDao orderDao;
    private final TripTicketServiceHelper helper;
    private final WebsocketUpdateHelper websocketUpdateHelper;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public TripTicketServiceImpl(TripTicketDao ticketDao, TruckDao truckDao, DriverDao driverDao, OrderDao orderDao, TripTicketServiceHelper helper, WebsocketUpdateHelper websocketUpdateHelper, ApplicationEventPublisher applicationEventPublisher) {
        this.ticketDao = ticketDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
        this.orderDao = orderDao;
        this.helper = helper;
        this.websocketUpdateHelper = websocketUpdateHelper;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Method creates new trip ticket and books the truck.
     * Departure city is defined as truck location city.
     * Arrival city, if not specified, is set the same as departure city (round-trip).
     *
     * @param truckRegNum       truck to be booked
     * @param departureDateTime departure date and time
     * @param toCity            destination city (may be omitted)
     */
    @Override
    @Transactional
    public long createTicket(String truckRegNum, LocalDateTime departureDateTime, @Nullable CityEntity toCity) {
        //create new ticket entity
        TripTicketEntity ticket = new TripTicketEntity();
        ticket.setStatus(CREATED);

        //validate and assign trip departure date
        if (departureDateTime.isBefore(LocalDateTime.now()))
            throw new UncheckedServiceException("Trip starting date should be in the future");
        ticket.setDepartureDateTime(departureDateTime);

        //validate and assign truck and mark it as booked
        TruckEntity truck = truckDao.findByRegNumber(truckRegNum);
        if (!truck.isServiceable()) throw new UncheckedServiceException(String.format(
                "Can't book truck %s - it is out of service", truck.getRegNumber()));
        if (truck.getBookedUntil().isAfter(departureDateTime)) throw new UncheckedServiceException(String.format(
                "Can't book truck %s for the trip starting %tF: the truck is booked until %tF", truck.getRegNumber(), departureDateTime, truck.getBookedUntil()));
        truck.setBookedUntil(MAX_FUTURE_DATE);
        ticket.setTruck(truck);

        //if no destination city is specified - the trip will be planned as the round one
        //truck location is changed to "on the way to destination city"
        ticket.getStopovers().add(new StopoverEntity(truck.getLocation(), 0));
        if (toCity == null) {
            ticket.getStopovers().add(new StopoverEntity(truck.getLocation(), 1));
        } else {
            ticket.getStopovers().add(new StopoverEntity(toCity, 1));
            truck.setLocation(toCity);
        }
        truck.setParked(false);
        ticket.setCurrentStep(0);

        //publish update event
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());

        return ticketDao.add(ticket);
    }

    /**
     * Updates departure date and time in the trip ticket
     *
     * @param ticketId          ticket id
     * @param departureDateTime new departure date and time
     */
    @Override
    @Transactional
    public void updateDepartureDateTime(long ticketId, LocalDateTime departureDateTime) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        if (ticket.getStatus() != CREATED) throw new UncheckedServiceException(
                String.format("Can't update ticket if %d as it has status %s",
                        ticketId,
                        ticket.getStatus()));
        if (departureDateTime.isBefore(getMinNewDateTime(ticketId))) throw new UncheckedServiceException(
                String.format("Cant' update departure date for ticket id %d: " +
                                "new departure time should be in the future and not earlier then the current departure time",
                        ticketId));
        ticket.setDepartureDateTime(departureDateTime);
    }

    /**
     * Attempts adding the orders to a trip ticket.
     *
     * @param ticketId target trip ticket id
     * @param orderId  order id
     * @param locale   user locale
     * @return system message with the result of operation
     */
    @Override
    @Transactional
    public SystemMessage addSingleOrderToTicketAndReport(long ticketId, long orderId, Locale locale) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        OrderEntity order = orderDao.findById(orderId);
        try {
            helper.addOrderToTicket(order, ticket);
            return new SystemMessage("Success", String.format("Order %d is added to ticket %d", orderId, ticketId));
        } catch (CheckedServiceException e) {
            return new SystemMessage("Failed", e.getLocalizedMessage(locale));
        } catch (Exception e) {
            return new SystemMessage("Failed", "Something went wrong at server side");
        }
    }

    /**
     * Attempts adding several orders to a trip ticket, and tells how many additions were successful.
     * Orders to be added are selected by given criteria.
     *
     * @param fromCityId order selection criteria - departure city id
     * @param toCityId   order selection criteria - destination city id
     * @param date       order selection criteria - order creation date
     * @param ticketId   target trip ticket id
     * @param locale     user's locale
     * @return system message with the result of operation
     */
    //todo entry point for the most tricky operation - adding several orders to ticket
    @Override
    @Transactional
    public SystemMessage addMultipleOrdersToTicketAndReport(long fromCityId, long toCityId, @Nullable LocalDate date, long ticketId, Locale locale) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        List<OrderEntity> orders;
        if (date == null) orders = orderDao.listUnassigned(fromCityId, toCityId);
        else orders = orderDao.listUnassigned(fromCityId, toCityId, date);
        int totalQuantity = 0;
        int totalWeight = 0;

        for (OrderEntity order : orders) {
            try {
                //todo call of method which will actually add order to ticket
                helper.addOrderToTicket(order, ticket);
                totalQuantity++;
                totalWeight += order.getWeight();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new SystemMessage("Adding orders to tickets",
                String.format("%d orders with total weight of %d kg added to ticket %d", totalQuantity, totalWeight, ticketId));
    }


    /**
     * Method removes order from the ticket and updates route and truck load data.
     *
     * @param ticketId ticket id
     * @param orderId  order id
     */
    @Override
    @Transactional
    public void removeOrderFromTicket(long ticketId, long orderId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        OrderEntity order = orderDao.findById(orderId);
        helper.removeOrderFromTicket(order, ticket);
        order.setStatus(OrderStatus.NEW);

        //publish update event
        applicationEventPublisher.publishEvent(new EntityUpdateEvent());
    }

    /**
     * Method finalizes trip ticket and marks it as approved for execution.
     * Finalization includes correction of the departure date to the future (if necessary)
     * and assigning the drivers.
     *
     * @param ticketId ticket id to be approved
     * @throws PastDepartureDateException  in case ticket departure date is in the past
     * @throws NoDriversAvailableException in case no drivers are available for the ticket
     */
    @Override
    @Transactional
    public void approveTicket(long ticketId) throws PastDepartureDateException, NoDriversAvailableException {
        TripTicketEntity ticket = ticketDao.findById(ticketId);

        if (ticket.getStatus().equals(CREATED)) {

            // check the departure date
            if (ticket.getDepartureDateTime().isBefore(LocalDateTime.now()))
                throw new PastDepartureDateException(ticketId);

            // assign drivers
            CityEntity fromCity = ticket.getStopoverWithSequenceNo(0).getCity();
            int shiftSize = ticket.getTruck().getShiftSize();
            List<DriverEntity> availableDrivers = driverDao.listAllAvailable(fromCity, ticket.getDepartureDateTime());
            if (availableDrivers.size() < shiftSize) throw new NoDriversAvailableException(ticketId);
            ticket.setDrivers(availableDrivers.subList(0, shiftSize));

            // calculate estimated arrival time and record it for truck and drivers
            try {
                LocalDateTime estimatedArrival = helper.calculateDurationAndArrivalDateTime(ticket);
                ticket.setArrivalDateTime(estimatedArrival);
                ticket.getTruck().setBookedUntil(estimatedArrival);
                ticket.getDrivers().forEach(d -> d.setBookedUntil(estimatedArrival));
            } catch (NoRouteFoundException e) {
                ticket.setArrivalDateTime(MAX_FUTURE_DATE);
                ticket.getTruck().setBookedUntil(MAX_FUTURE_DATE);
                ticket.getDrivers().forEach(d -> d.setBookedUntil(MAX_FUTURE_DATE));
            }

            // update ticket and its orders statuses
            ticket.setStatus(APPROVED);
            ticket.getStopovers().stream().flatMap(s -> s.getLoads().stream()).map(TransactionEntity::getOrder).forEach(o -> o.setStatus(OrderStatus.READY_TO_SHIP));

            //publish update event
            sendUpdateToAllDrivers(ticket);
            applicationEventPublisher.publishEvent(new EntityUpdateEvent());
        }
    }

    /**
     * calculates planned trip ticket execution time, in minutes, split by year and month
     *
     * @param ticket trip ticket
     * @return map with planned trip ticket execution time by monthly periods
     */
    @Override
    @Transactional
    public Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket) {
        Map<YearMonth, Long> result = new HashMap<>();
        try {
            helper.calculateDurationAndArrivalDateTime(ticket);
        } catch (NoRouteFoundException e) {
            throw new UncheckedServiceException(String.format("NoRouteFoundException on already existing ticket id %d", ticket.getId()), e);
        }
        long totalPlannedMinutes = ticket.getStopovers().stream()
                .map(StopoverEntity::getEstimatedDuration)
                .mapToLong(Duration::toMinutes)
                .sum();
        YearMonth month = YearMonth.from(ticket.getDepartureDateTime());
        LocalDateTime start = ticket.getDepartureDateTime();
        LocalDateTime finish = month.atEndOfMonth().atTime(LocalTime.MAX);

        while (totalPlannedMinutes > 0) {
            long monthlyPlannedMinutes = Math.min(totalPlannedMinutes, Duration.between(start, finish).toMinutes());
            result.put(month, monthlyPlannedMinutes);
            totalPlannedMinutes -= monthlyPlannedMinutes;
            month = month.plusMonths(1);
            start = month.atDay(1).atStartOfDay();
            finish = month.atEndOfMonth().atTime(LocalTime.MAX);
        }

        return result;
    }

    /**
     * finds trip ticket by id
     *
     * @param ticketId ticket id
     * @return ticket
     */
    @Override
    @Transactional
    public TripTicketRecord findById(long ticketId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        if (ticket == null) throw new EntityNotFoundException(ticketId, TripTicketEntity.class);
        return mapper.map(ticket, TripTicketRecord.class);
    }

    /**
     * lists all trip tickets
     *
     * @return ticket list
     */
    @Override
    @Transactional
    public List<TripTicketRecord> listAll() {
        return ticketDao.listAll()
                .stream()
                .map(t -> mapper.map(t, TripTicketRecord.class))
                .collect(Collectors.toList());
    }

    /**
     * lists all created but unapproved trip tickets
     *
     * @return ticket list
     */
    @Override
    @Transactional
    public List<TripTicketRecord> listAllUnapproved() {
        return ticketDao.listAllUnapproved()
                .stream()
                .map(t -> mapper.map(t, TripTicketRecord.class))
                .collect(Collectors.toList());
    }

    /**
     * lists all orders assigned to the ticket
     *
     * @param id ticket id
     * @return orders list
     */
    @Override
    public List<OrderEntity> listAllOrderInTicket(long id) {
        TripTicketEntity ticket = ticketDao.findById(id);
        return ticket.getStopovers().stream().flatMap(s -> s.getLoads().stream()).map(TransactionEntity::getOrder).collect(Collectors.toList());
    }

    /**
     * Method looks up for a current trip ticket assigned to the driver
     * and compiles the instruction to the driver based on current ticket step and its loads / unloads list
     *
     * @param principal driver initiating the request
     * @return instruction to the driver usable in OnTheRoadController
     */
    @Override
    @Transactional
    public Instruction getInstructionForDriver(Principal principal) {
        DriverEntity driver = driverDao.findByUsername(principal.getName());
        return getInstructionForDriver(driver);
    }

    /**
     * Method looks up for a current trip ticket assigned to the driver
     * and compiles the instruction to the driver based on current ticket step and its loads / unloads list
     *
     * @param driver driver initiating the request
     * @return instruction to the driver usable in OnTheRoadController
     */
    private Instruction getInstructionForDriver(DriverEntity driver) {
        Instruction instruction = new Instruction();

        TripTicketEntity currentTicket = ticketDao.findByDriverAndStatus(driver.getPersonalId(), RUNNING);
        if (currentTicket == null) currentTicket = ticketDao.findByDriverAndStatus(driver.getPersonalId(), APPROVED);
        if (currentTicket == null) {
            instruction.setDirectiveMessage("Маршрутных заданий нет. Отдыхайте!");
            return instruction;
        }
        instruction.setTicket(mapper.map(currentTicket, TripTicketRecord.class));

        DriverStatus driverStatus = driver.getStatus();
        instruction.setDriverStatus(driverStatus);
        switch (driverStatus) {
            case OFFLINE:
                instruction.setTask(START);
                instruction.setTargetStep(0);
                instruction.setDirectiveMessage(String.format("Старт Вашей следующей поездки: %s, %s, %s",
                        currentTicket.getStopoverWithSequenceNo(0).getCity().getName(),
                        currentTicket.getDepartureDateTime().toString().substring(0, 10),
                        currentTicket.getDepartureDateTime().toString().substring(11, 16)));
                instruction.setRequestedActionMessage("Открыть смену");
                if (currentTicket.getDrivers().stream().anyMatch(d -> d.getStatus() == DriverStatus.WAITING)) {
                    instruction.setAlert("Вашего выхода на линию ожидают напарники");
                }
                break;
            case ROAD_BREAK:
                instruction.setTask(FINISH_ROAD_BREAK);
                instruction.setDirectiveMessage("Ваш статус: стоянка для отдыха");
                instruction.setRequestedActionMessage("Завершить стоянку");
                if (currentTicket.getDrivers().stream().anyMatch(d -> d.getStatus() == DriverStatus.WAITING)) {
                    instruction.setAlert("Вашего выхода на линию ожидают напарники");
                }
                break;
            case STOPOVER_BREAK:
                instruction.setTask(FINISH_STOPOVER_BREAK);
                instruction.setDirectiveMessage("Ваш статус: перерыв");
                instruction.setRequestedActionMessage("Завершить перерыв");
                if (currentTicket.getDrivers().stream().anyMatch(d -> d.getStatus() == DriverStatus.WAITING)) {
                    instruction.setAlert("Вашего выхода на линию ожидают напарники");
                }
                break;
            case WAITING:
                instruction.setTask(WAIT);
                instruction.setDirectiveMessage("Для продолжения маршрута дождитесь Вашего напарника");
                break;
            default:
                int step = currentTicket.getCurrentStep();
                StopoverEntity currentStopover;
                Task task = helper.getCurrentTask(currentTicket);
                switch (task) {
                    case GOTO:
                        instruction.setTask(GOTO);
                        instruction.setTargetStep(step + 1);
                        instruction.setDirectiveMessage(String.format("Следуйте в город %s",
                                currentTicket.getStopoverWithSequenceNo(step + 1).getCity().getName()));
                        instruction.setRequestedActionMessage("Подтвердить прибытие");
                        break;
                    case LOAD:
                        currentStopover = currentTicket.getStopoverWithSequenceNo(step);
                        instruction.setTask(LOAD);
                        instruction.setTargetStep(step);
                        instruction.setCurrentStop(currentStopover.getCity());
                        instruction.setOrders(currentStopover.getLoads().stream()
                                .map(TransactionEntity::getOrder)
                                .map(o -> mapper.map(o, OrderRecordDriverInstruction.class))
                                .collect(Collectors.toList()));
                        instruction.setDirectiveMessage("Получите груз:");
                        instruction.setRequestedActionMessage("Подтвердить погрузку");
                        break;
                    case UNLOAD:
                        currentStopover = currentTicket.getStopoverWithSequenceNo(step);
                        instruction.setTask(UNLOAD);
                        instruction.setTargetStep(currentTicket.getCurrentStep());
                        instruction.setCurrentStop(currentStopover.getCity());
                        instruction.setOrders(currentStopover.getUnloads().stream()
                                .map(TransactionEntity::getOrder)
                                .map(o -> mapper.map(o, OrderRecordDriverInstruction.class))
                                .collect(Collectors.toList()));
                        instruction.setDirectiveMessage("Произведите выгрузку");
                        instruction.setRequestedActionMessage("Подтвердить отгрузку");
                        break;
                    case CLOSE_TICKET:
                        instruction.setTask(CLOSE_TICKET);
                        instruction.setTargetStep(step + 1);
                        instruction.setDirectiveMessage("Маршрут завершён. Спасибо за работу!");
                        instruction.setRequestedActionMessage("Закрыть");
                        break;
                    case NONE:
                    default:
                        throw new UncheckedServiceException(String.format("Invalid instruction for ticket id %d", currentTicket.getId()));

                }
        }

        instruction.setUrl(instruction.getTask().getUrl());
        return instruction;
    }


    /**
     * Method records driver going online (opening a new shift)
     *
     * @param principal requesting driver
     * @param ticketId  ticket id
     */
    @Override
    @Transactional
    public void setOnline(Principal principal, long ticketId) {
        DriverEntity driver = driverDao.findByUsername(principal.getName());
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        if (ticket.getStatus() == APPROVED) ticket.setStatus(RUNNING);
        helper.setOnline(driver, ticket);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * Method records finished move to the new stopover and updates time tracking for all the drivers
     *
     * @param ticketId ticket id
     * @param step     sequence number of the stopover to move to
     */
    @Override
    @Transactional
    public void reachStopover(Principal principal, long ticketId, int step) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        if (ticket.getCurrentStep() + 1 == step) {
            if (ticket.getCurrentStep() == 0) ticket.setStatus(RUNNING);
            ticket.setCurrentStep(step);
            helper.updateDriversTimeRecordsForNewStage(ticket);
            helper.checkTicketCompletion(ticket);
            sendUpdateToOtherDrivers(ticket, principal);
        } else throw new UncheckedServiceException(String.format("Wrong step sequence for ticket id %d", ticketId));
    }


    /**
     * Method marks all load operations at the given stopover as completed
     * (order statuses changed to shipped), and updates time tracking for the driver
     *
     * @param ticketId ticket id
     * @param step     sequence number of the stopover
     */
    @Override
    @Transactional
    public void loadAtStopover(Principal principal, long ticketId, int step) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        List<OrderEntity> ordersToLoad = ticket.getStopoverWithSequenceNo(step)
                .getLoads().stream()
                .map(TransactionEntity::getOrder).collect(Collectors.toList());
        ordersToLoad.forEach(o -> o.setStatus(OrderStatus.SHIPPED));
        helper.updateDriversTimeRecordsForNewStage(ticket);
        helper.checkTicketCompletion(ticket);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * Method marks all unload operations at the given stopover as completed
     * (order statuses changed to delivered), and updates time tracking for the driver
     *
     * @param ticketId ticket id
     * @param step     sequence number of the stopover
     */
    @Override
    @Transactional
    public void unloadAtStopover(Principal principal, long ticketId, int step) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        List<OrderEntity> ordersToUnload = ticket.getStopoverWithSequenceNo(step)
                .getUnloads().stream()
                .map(TransactionEntity::getOrder).collect(Collectors.toList());
        ordersToUnload.forEach(o -> o.setStatus(OrderStatus.DELIVERED));
        helper.updateDriversTimeRecordsForNewStage(ticket);
        helper.checkTicketCompletion(ticket);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * Method registers the reporting driver as the first (driving) one, and changes all the rest to seconding status.
     *
     * @param principal reporting driver
     * @param ticketId  ticket id
     */
    @Override
    @Transactional
    public void setFirstDriver(Principal principal, long ticketId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        DriverEntity driver = driverDao.findByUsername(principal.getName());
        helper.setFirstDriver(ticket, driver);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * Method registers drivers taking a road break (status ROAD_BREAK is assigned to all truck drivers).
     *
     * @param principal reporting driver
     * @param ticketId  ticket id
     */
    @Override
    @Transactional
    public void startRoadBreak(Principal principal, long ticketId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        helper.startRoadBreak(ticket);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * Method registers drivers finishing a road break (status RESTING is assigned to all truck drivers).
     *
     * @param principal reporting driver
     * @param ticketId  ticket id
     */
    @Override
    @Transactional
    public void finishRoadBreak(Principal principal, long ticketId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        DriverEntity driver = driverDao.findByUsername(principal.getName());
        helper.finishRoadBreak(ticket, driver);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * Method registers drivers taking a stopover break (break status is assigned to him but not to other truck drivers).
     *
     * @param principal reporting driver
     */
    @Override
    @Transactional
    public void startStopoverBreak(Principal principal) {
        DriverEntity driver = driverDao.findByUsername(principal.getName());
        helper.startStopoverBreak(driver);
    }

    /**
     * Method registers driver's stopover break is over
     *
     * @param principal reporting driver
     */
    @Override
    @Transactional
    public void finishStopoverBreak(Principal principal, long ticketId) {
        DriverEntity driver = driverDao.findByUsername(principal.getName());
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        helper.finishStopoverBreak(driver, ticket);
        sendUpdateToOtherDrivers(ticket, principal);
    }

    /**
     * @param ticketId trip ticket
     * @return minimum date / time that the ticket's departure datetime can be changed to
     */
    @Override
    @Transactional
    public LocalDateTime getMinNewDateTime(long ticketId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        return LocalDateTime.now().isAfter(ticket.getDepartureDateTime()) ?
                LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES) :
                ticket.getDepartureDateTime().truncatedTo(ChronoUnit.MINUTES);
    }

    /**
     * sends websocket update on ticket status to other drivers in the shift
     *
     * @param ticket    ticket
     * @param principal initiating driver
     */
    private void sendUpdateToOtherDrivers(TripTicketEntity ticket, Principal principal) {
        if (ticket.getDrivers().size() > 1) {
            DriverEntity initiatingDriver = driverDao.findByUsername(principal.getName());
            ticket.getDrivers().stream().filter(driver -> !driver.equals(initiatingDriver)).forEach(driver -> {
                websocketUpdateHelper.sendUpdate(driver.getUsername(), getInstructionForDriver(driver));
            });
        }
    }

    /**
     * sends websocket update on ticket status to all the drivers in the shift
     *
     * @param ticket ticket
     */
    private void sendUpdateToAllDrivers(TripTicketEntity ticket) {
        if (ticket.getDrivers().size() > 1) {
            ticket.getDrivers().forEach(driver -> {
                websocketUpdateHelper.sendUpdate(driver.getUsername(), getInstructionForDriver(driver));
            });
        }
    }
}
