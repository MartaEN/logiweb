package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.*;
import com.marta.logistika.dto.Instruction;
import com.marta.logistika.dto.OrderRecordDriverInstruction;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.OrderStatus;
import com.marta.logistika.enums.TripTicketStatus;
import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.exception.OrderDoesNotFitToTicketException;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.TimeTrackerService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

import static com.marta.logistika.dto.Instruction.*;
import static com.marta.logistika.dto.Instruction.Task.*;

@Service("tripTicketService")
public class TripTicketServiceImpl extends AbstractService implements TripTicketService {

    private final TripTicketDao ticketDao;
    private final TruckDao truckDao;
    private final DriverDao driverDao;
    private final TimeTrackerService timeService;
    private final OrderDao orderDao;
    private final TripTicketServiceHelper helper;

    @Autowired
    public TripTicketServiceImpl(TripTicketDao ticketDao, TruckDao truckDao, DriverDao driverDao, TimeTrackerService timeService, OrderDao orderDao, TripTicketServiceHelper helper) {
        this.ticketDao = ticketDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
        this.timeService = timeService;
        this.orderDao = orderDao;
        this.helper = helper;
    }

    /**
     * Method creates new trip ticket and books the truck.
     * Departure city is defined as truck location city.
     * Arrival city, if not specified, is set the same as departure city (round-trip).
     * @param truckRegNum truck to be booked
     * @param departureDateTime departure date and time
     * @param toCity destination city (may be omitted)
     * @throws ServiceException in case truck is not available or departure time is in the past
     */
    @Override
    @Transactional
    public void createTicket(String truckRegNum, LocalDateTime departureDateTime, @Nullable CityEntity toCity) throws ServiceException {

        //create new ticket entity
        TripTicketEntity ticket = new TripTicketEntity();
        ticket.setStatus(TripTicketStatus.CREATED);

        //validate and assign trip departure date
        if(departureDateTime.isBefore(LocalDateTime.now())) throw new ServiceException("Trip starting date should be in the future");
        ticket.setDepartureDateTime(departureDateTime);

        //validate and assign truck and mark it as booked
        TruckEntity truck = truckDao.findByRegNumber(truckRegNum);
        if(!truck.isServiceable()) throw new ServiceException(String.format(
                "Can't book truck %s - it is out of service", truck.getRegNumber()));
        if(truck.getBookedUntil().isAfter(departureDateTime)) throw new ServiceException(String.format(
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
        ticket.setCurrentStep(-1);

        ticketDao.add(ticket);
    }

    /**
     * Adds order to trip ticket, minimizing the total trip distance and checking truck capacity limits
     * @param ticketId id of the trip ticket that has to intake new order
     * @param orderId id of the order that has to be placed to the ticket
     * @throws ServiceException is thrown in case ticket and / or order are have wrong status
     * and in case of weight limit breakage
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addOrderToTicket(long ticketId, long orderId) throws ServiceException {

        // find ticket and order and check their statuses
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        OrderEntity order = orderDao.findById(orderId);
        if (ticket.getStatus() != TripTicketStatus.CREATED) throw new ServiceException(String.format("Can't add to trip ticket id %d - ticket has already been approved", ticket.getId()));
        if (order.getStatus() != OrderStatus.NEW) throw new ServiceException(String.format("Order id %d has already been assigned to some trip ticket", order.getId()));

        // find suggested load and unload stopovers for the order
        int [] suggestedLoadUnloadForNewOrder = helper.suggestLoadUnloadPoints(ticket, order);
        int loadPoint = suggestedLoadUnloadForNewOrder[0];
        int unloadPoint = suggestedLoadUnloadForNewOrder [1];

        // add load and unload operations for the order
        if ( ! ticket.getStopoverWithSequenceNo(loadPoint).getCity().equals(order.getFromCity())) {
            ++unloadPoint;
            helper.insertNewStopover(ticket, order.getFromCity(), ++loadPoint);
        }
        ticket.getStopoverWithSequenceNo(loadPoint).addLoadFor(order);

        if ( ! ticket.getStopoverWithSequenceNo(unloadPoint).getCity().equals(order.getToCity())) {
            helper.insertNewStopover(ticket, order.getToCity(), unloadPoint);
        }
        ticket.getStopoverWithSequenceNo(unloadPoint).addUnloadFor(order);

        // update weights and check against truck capacity
        helper.updateWeights(ticket);
        try {
            helper.checkWeightLimit(ticket);
        } catch (ServiceException e) {
//            throw new ServiceException(String.format("### Order id %d does not fit into trip ticket %d: %s", order.getId(), ticket.getId(), e.getMessage()));
            throw new OrderDoesNotFitToTicketException(e, order.getId());
        }
        ticket.setAvgLoad((int) (helper.getAvgLoad(ticket) / ticket.getTruck().getCapacity() * 100));

        // update order status in case of successful weight check
        order.setStatus(OrderStatus.ASSIGNED);
    }


    /**
     * Method removes order from the ticket and updates route and truck load data.
     * @param ticketId ticket id
     * @param orderId order id
     */
    @Override
    @Transactional
    public void removeOrderFromTicket(long ticketId, long orderId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        OrderEntity order = orderDao.findById(orderId);

        for (int i = 0; i < ticket.getStopovers().size(); i++) {

            StopoverEntity stopover = ticket.getStopovers().get(i);

            for (int j = 0; j < stopover.getLoads().size(); j++) {
                if(stopover.getLoads().get(j).getOrder().equals(order))
                    stopover.getLoads().remove(j);
            }

            for (int j = 0; j < stopover.getUnloads().size(); j++) {
                if(stopover.getUnloads().get(j).getOrder().equals(order))
                    stopover.getUnloads().remove(j);
            }

        }

        helper.removeEmptyStopovers(ticket);
        helper.updateWeights(ticket);
        ticket.setAvgLoad((int) (helper.getAvgLoad(ticket) / ticket.getTruck().getCapacity() * 100));

        order.setStatus(OrderStatus.NEW);
    }


    /**
     * Method finalizes trip ticket and marks it as approved for execution.
     * Finalization includes correction of the departure date to the future (if necessary)
     * and assigning the drivers.
     * @param ticketId ticket id to be approved
     * @throws ServiceException is thrown in case no drivers are available
     */
    @Override
    @Transactional
    public void approveTicket(long ticketId) throws ServiceException {
        TripTicketEntity ticket = ticketDao.findById(ticketId);

        if (ticket.getStatus().equals(TripTicketStatus.CREATED)) {

            // correct the date to the future if necessary
            if (ticket.getDepartureDateTime().isBefore(LocalDateTime.now())) {
                ticket.setDepartureDateTime(LocalDateTime.now().plusDays(1).withHour(9).withMinute(0));
            }

            // assign drivers
            CityEntity fromCity = ticket.getStopoverWithSequenceNo(0).getCity();
            int shiftSize = ticket.getTruck().getShiftSize();
            List<DriverEntity> availableDrivers = driverDao.listAllAvailable(fromCity, ticket.getDepartureDateTime());
            if (availableDrivers.size() < shiftSize) throw new ServiceException("No drivers available for the ticket");
            ticket.setDrivers(availableDrivers.subList(0, shiftSize));

            // calculate estimated arrival time and record it for truck and drivers
            LocalDateTime estimatedArrival = helper.calculateDurationAndArrivalDateTime(ticket);
            ticket.setArrivalDateTime(estimatedArrival);
            ticket.getTruck().setBookedUntil(estimatedArrival);
            ticket.getDrivers().forEach(d -> d.setBookedUntil(estimatedArrival));

            // update ticket and its orders statuses
            ticket.setStatus(TripTicketStatus.APPROVED);
            ticket.getStopovers().stream().flatMap(s -> s.getLoads().stream()).map(TransactionEntity::getOrder).forEach(o -> o.setStatus(OrderStatus.READY_TO_SHIP));
        }
    }

    @Override
    public void deleteTicket(long ticketId) {
        //todo
    }

    @Override
    @Transactional
    public Map<YearMonth, Long> getPlannedMinutesByYearMonth(TripTicketEntity ticket) {
        Map<YearMonth, Long> result = new HashMap<>();
        helper.calculateDurationAndArrivalDateTime(ticket);
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

    @Override
    @Transactional
    public TripTicketRecord findById(long ticketId) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        if (ticket == null) throw new EntityNotFoundException(ticketId, TripTicketEntity.class);
        return mapper.map(ticket, TripTicketRecord.class);
    }

    @Override
    @Transactional
    public List<TripTicketRecord> listAll() {
        return ticketDao.listAll()
                .stream()
                .map(t -> mapper.map(t, TripTicketRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TripTicketRecord> listAllUnapproved() {
        return ticketDao.listAllUnapproved()
                .stream()
                .map(t -> mapper.map(t, TripTicketRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> listAllOrderInTicket(long id) {
        TripTicketEntity ticket = ticketDao.findById(id);
        return ticket.getStopovers().stream().flatMap(s -> s.getLoads().stream()).map(TransactionEntity::getOrder).collect(Collectors.toList());
    }

    /**
     * Method looks up for a current trip ticket assigned to the driver
     * and compiles the instruction to the driver based on current ticket step and its loads / unloads list
     * @param personalId driver's personal Id
     * @return instruction to the driver usable in OnTheRoadController
     */
    @Override
    @Transactional
    public Instruction getInstructionForDriver(String personalId) {

        Instruction instruction = new Instruction();

        DriverEntity driver = driverDao.findByPersonalId(personalId);
        instruction.setDriverStatus(driver.getStatus());

        TripTicketEntity currentTicket = ticketDao.findByDriverAndStatus(personalId, TripTicketStatus.RUNNING);
        if (currentTicket == null) currentTicket = ticketDao.findByDriverAndStatus(personalId, TripTicketStatus.APPROVED);
        if (currentTicket == null) {
            instruction.setDirectiveMessage("Маршрутных заданий нет. Отдыхайте!");
            return instruction;
        }
        instruction.setTicket(mapper.map(currentTicket, TripTicketRecord.class));

        int step = currentTicket.getCurrentStep();
        StopoverEntity currentStopover;
        Task task = helper.getCurrentTask(currentTicket);
        switch (task) {
            case START:
                instruction.setTask(GOTO);
                instruction.setTargetStep(0);
                instruction.setDirectiveMessage(String.format("Старт Вашей следующей поездки: %s, %s, %s",
                        currentTicket.getStopoverWithSequenceNo(0).getCity().getName(),
                        currentTicket.getDepartureDateTime().toString().substring(0, 10),
                        currentTicket.getDepartureDateTime().toString().substring(11, 16)));
                instruction.setRequestedActionMessage("Открыть смену");
                break;
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
            case FINISH:
                instruction.setTask(FINISH);
                instruction.setTargetStep(step + 1);
                instruction.setDirectiveMessage("Маршрут завершён. Спасибо за работу!");
                instruction.setRequestedActionMessage("Закрыть");
                break;
            case NONE:
            default:
                throw new ServiceException(String.format("Invalid instruction for ticket id %d", currentTicket.getId()));

        }
        return instruction;
    }

    /**
     * Method records finished move to the new stopover and updates time tracking for all the drivers
     * @param ticketId ticket id
     * @param step sequence number of the stopover to move to
     */
    @Override
    @Transactional
    public void reachStopover(long ticketId, int step) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        if(ticket.getCurrentStep() + 1 == step) {
            ticket.setCurrentStep(step);
            helper.updateDriversTimeRecords(ticket);
            helper.checkTicketCompletion(ticket);
        } else throw new ServiceException(String.format("Wrong step sequence for ticket id %d", ticketId));
    }


    /**
     * Method marks all load operations at the given stopover as completed
     * (order statuses changed to shipped), and updates time tracking for the driver
     * @param ticketId ticket id
     * @param step sequence number of the stopover
     */
    @Override
    @Transactional
    public void loadAtStopover(long ticketId, int step) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        List<OrderEntity> ordersToLoad = ticket.getStopoverWithSequenceNo(step)
                .getLoads().stream()
                .map(TransactionEntity::getOrder).collect(Collectors.toList());
        ordersToLoad.forEach(o -> o.setStatus(OrderStatus.SHIPPED));
        helper.updateDriversTimeRecords(ticket);
        helper.checkTicketCompletion(ticket);
    }

    /**
     * Method marks all unload operations at the given stopover as completed
     * (order statuses changed to delivered), and updates time tracking for the driver
     * @param ticketId ticket id
     * @param step sequence number of the stopover
     */
    @Override
    @Transactional
    public void unloadAtStopover(long ticketId, int step) {
        TripTicketEntity ticket = ticketDao.findById(ticketId);
        List<OrderEntity> ordersToUnload = ticket.getStopoverWithSequenceNo(step)
                .getUnloads().stream()
                .map(TransactionEntity::getOrder).collect(Collectors.toList());
        ordersToUnload.forEach(o -> o.setStatus(OrderStatus.DELIVERED));
        helper.updateDriversTimeRecords(ticket);
        helper.checkTicketCompletion(ticket);
    }

}
