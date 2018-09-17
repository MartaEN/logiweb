package com.marta.logistika.service.api;

import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.OrderEntity;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.service.ServiceException;

import java.time.LocalDateTime;

public interface TripTicketService {

    TripTicketEntity createTripTicket (TruckRecord truck, LocalDateTime date);
    void approveTripTicket (TripTicketEntity ticket);
    TripTicketEntity findById(long id);
    void addOrderToTicket (TripTicketEntity ticket, OrderEntity order) throws ServiceException;
    void removeOrderFromTicket (TripTicketEntity ticket, OrderEntity order);
    int getDistance(TripTicketEntity ticket);

}
