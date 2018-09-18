package com.marta.logistika.service.api;

import com.marta.logistika.dto.TripTicketRecord;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.service.ServiceException;

import java.time.LocalDateTime;
import java.util.List;

public interface TripTicketService {

    TripTicketEntity createTripTicket (TruckRecord truck, LocalDateTime date);
    void approveTripTicket (long id);
    TripTicketEntity findById(long id);
    TripTicketRecord findDtoById (long id);
    void addOrderToTicket (TripTicketEntity ticket, OrderEntity order) throws ServiceException;
    void removeOrderFromTicket (TripTicketEntity ticket, OrderEntity order);
    int getDistance(TripTicketEntity ticket);
    List<TripTicketRecord> listAllUnapproved();
    List<OrderEntity> listAllOrderInTicket(long id);
}
