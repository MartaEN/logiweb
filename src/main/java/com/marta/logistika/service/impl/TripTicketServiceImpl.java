package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.*;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.OrderStatus;
import com.marta.logistika.enums.TripTicketStatus;
import com.marta.logistika.exception.ServiceException;
import com.marta.logistika.service.api.RoadService;
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

@Service("tripTicketService")
public class TripTicketServiceImpl extends AbstractService implements TripTicketService {

    private final TripTicketDao tripTicketDao;
    private final TruckDao truckDao;
    private final DriverDao driverDao;
    private final OrderDao orderDao;
    private final RoadService roadService;
    private final TripTicketServiceHelper helper;

    @Autowired
    public TripTicketServiceImpl(TripTicketDao tripTicketDao, TruckDao truckDao, DriverDao driverDao, OrderDao orderDao, RoadService roadService) {
        this.tripTicketDao = tripTicketDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
        this.orderDao = orderDao;
        this.roadService = roadService;
        this.helper = new TripTicketServiceHelper(roadService);
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

        tripTicketDao.add(ticket);
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
        TripTicketEntity ticket = tripTicketDao.findById(ticketId);
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
            throw new ServiceException(String.format("### Order id %d does not fit into trip ticket %d: %s", order.getId(), ticket.getId(), e.getMessage()));
        }
        ticket.setAvgLoad((int) (helper.getAvgLoad(ticket) / ticket.getTruck().getCapacity() * 100));

        // update order status in case of successful weight check
        order.setStatus(OrderStatus.ASSIGNED);
//        orderDao.merge(order);
//        tripTicketDao.merge(ticket);
    }


    @Override
    public void removeOrderFromTicket(long ticketId, long orderId) {
        //todo
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
    public void signTicket(long ticketId) throws ServiceException {
        TripTicketEntity ticket = tripTicketDao.findById(ticketId);

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

            // calculate estimated arrival time
            helper.calculateDurationAndArrivalDateTime(ticket);

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
    public TripTicketEntity findById(long ticketId) {
        return tripTicketDao.findById(ticketId);
    }

    @Override
    @Transactional
    public TripTicketRecord findDtoById(long ticketId) {
        return mapper.map(tripTicketDao.findById(ticketId), TripTicketRecord.class);
    }

    @Override
    @Transactional
    public List<TripTicketRecord> listAllUnapproved() {
        return tripTicketDao.listAllUnapproved()
                .stream()
                .map(t -> mapper.map(t, TripTicketRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> listAllOrderInTicket(long id) {
        TripTicketEntity ticket = tripTicketDao.findById(id);
        return ticket.getStopovers().stream().flatMap(s -> s.getLoads().stream()).map(TransactionEntity::getOrder).collect(Collectors.toList());
    }
}
