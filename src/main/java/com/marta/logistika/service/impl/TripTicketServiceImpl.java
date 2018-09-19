package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.*;
import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.*;
import com.marta.logistika.enums.OrderStatus;
import com.marta.logistika.enums.TripTicketStatus;
import com.marta.logistika.service.ServiceException;
import com.marta.logistika.service.api.RoadService;
import com.marta.logistika.service.api.TripTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service("tripTicketService")
public class TripTicketServiceImpl extends AbstractService implements TripTicketService {

    private final TripTicketDao tripTicketDao;
    private final TruckDao truckDao;
    private final OrderDao orderDao;
    private final RoadService roadService;
    private final TripTicketServiceHelper helper;

    @Autowired
    public TripTicketServiceImpl(TripTicketDao tripTicketDao, TruckDao truckDao, OrderDao orderDao, RoadService roadService) {
        this.tripTicketDao = tripTicketDao;
        this.truckDao = truckDao;
        this.orderDao = orderDao;
        this.roadService = roadService;
        this.helper = new TripTicketServiceHelper(roadService);
    }

    @Override
    @Transactional
    public void createTripTicket(String truckRegNum, LocalDateTime departureDateTime, @Nullable CityEntity toCity) {

        TripTicketEntity ticket = new TripTicketEntity();
        ticket.setStatus(TripTicketStatus.CREATED);

        //validate and assign trip departure date
        if(departureDateTime.isBefore(LocalDateTime.now())) throw new ServiceException("Trip starting date should be in the future");
        ticket.setDepartureDate(departureDateTime);

        //validate and assign truck and mark it as booked
        TruckEntity truck = truckDao.findByRegNumber(truckRegNum);
        if(!truck.isServiceable()) throw new ServiceException(String.format(
                "Can't book truck %s - it is out of service", truck.getRegNumber()));
        if(truck.getBookedUntil().isAfter(departureDateTime)) throw new ServiceException(String.format(
                "Can't book truck %s for the trip starting %tF: the truck is booked until %tF", truck.getRegNumber(), departureDateTime, truck.getBookedUntil()));
        truck.setBookedUntil(MAX_FUTURE_DATE);
        ticket.setTruck(truck);

        //if no destination city is specified - the trip will be planned as the round one
        ticket.getStopovers().add(new StopoverEntity(truck.getLocation(), 0));
        if (toCity == null) {
            ticket.getStopovers().add(new StopoverEntity(truck.getLocation(), 1));
        } else {
            ticket.getStopovers().add(new StopoverEntity(toCity, 1));
        }

        tripTicketDao.add(ticket);
    }

    @Override
    @Transactional
    public void approveTripTicket(long id) {
        TripTicketEntity ticket = tripTicketDao.findById(id);

        if (ticket.getStatus() == TripTicketStatus.CLOSED) throw new ServiceException(String.format("Error trying to assign APPROVED status to trip ticket id %d: ticket is already closed", ticket.getId()));
        if (ticket.getDepartureDate().isBefore(LocalDateTime.now())) throw new ServiceException("Can't approve ticket with past departure date - please edit the date first");

        helper.calculateDurationAndArrivalDateTime(ticket);

        //todo check route - capacity - drivers

        ticket.setStatus(TripTicketStatus.APPROVED);
        tripTicketDao.merge(ticket);
    }

    @Override
    @Transactional
    public TripTicketEntity findById(long id) {
        return tripTicketDao.findById(id);
    }

    @Override
    public TripTicketRecord findDtoById(long id) {
        return mapper.map(tripTicketDao.findById(id), TripTicketRecord.class);
    }

    /**
     * Adds order to trip ticket, minimizing the total trip distance and checking truck capacity limits
     * @param ticket trip ticket to intake new order
     * @param order order to be placed to the ticket
     * @throws ServiceException is thrown in case of weight limit breakage
     */
    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addOrderToTicket(TripTicketEntity ticket, OrderEntity order) throws ServiceException {

        if (ticket.getStatus() != TripTicketStatus.CREATED) throw new ServiceException(String.format("Can't add to trip ticket id %d - ticket has already been approved", ticket.getId()));
        if (order.getStatus() != OrderStatus.NEW) throw new ServiceException(String.format("Order id %d has already been assigned to some trip ticket", order.getId()));

        int [] suggestedLoadUnloadForNewOrder = helper.suggestLoadUnloadPoints(ticket.getCities(), order.getFromCity(), order.getToCity());
        int loadPoint = suggestedLoadUnloadForNewOrder[0];
        int unloadPoint = suggestedLoadUnloadForNewOrder [1];

        if ( ! ticket.getStopoverWithSequenceNo(loadPoint).getCity().equals(order.getFromCity())) {
            ++unloadPoint;
            helper.insertNewStopover(ticket, order.getFromCity(), ++loadPoint);
        }
        ticket.getStopoverWithSequenceNo(loadPoint).addLoadFor(order);

        if ( ! ticket.getStopoverWithSequenceNo(unloadPoint).getCity().equals(order.getToCity())) {
            helper.insertNewStopover(ticket, order.getToCity(), ++unloadPoint);
        }
        ticket.getStopoverWithSequenceNo(unloadPoint).addUnloadFor(order);

        helper.updateWeights(ticket);
        try {
            helper.checkWeightLimit(ticket);
        } catch (ServiceException e) {
            throw new ServiceException(String.format("### Order id %d does not fit into trip ticket %d: %s", order.getId(), ticket.getId(), e.getMessage()));
        }

        order.setStatus(OrderStatus.READY_TO_SHIP);

        orderDao.merge(order);
        tripTicketDao.merge(ticket);
    }

    @Override
    public void removeOrderFromTicket(TripTicketEntity ticket, OrderEntity order) {
        //todo
        helper.updateWeights(ticket);
    }

    @Override
    public int getDistance(TripTicketEntity ticket) {
        List<StopoverEntity> route = ticket.getStopovers();

        if (route.size() < 2) return 0;

        int distance = 0;
        route.sort(StopoverEntity::compareTo);
        for (int i = 1; i < route.size(); i++) {
            distance += roadService.getDistanceFromTo(route.get(i - 1).getCity(), route.get(i).getCity());
        }
        return distance;
    }

    @Override
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
