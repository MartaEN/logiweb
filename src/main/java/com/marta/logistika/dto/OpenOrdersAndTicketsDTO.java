package com.marta.logistika.dto;

import com.marta.logistika.entity.OrderEntity;

import java.io.Serializable;
import java.util.List;

public class OpenOrdersAndTicketsDTO implements Serializable {
    private List<OrderEntity> orders;
    private List<TripTicketRecord> tickets;

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public List<TripTicketRecord> getTickets() {
        return tickets;
    }

    public void setTickets(List<TripTicketRecord> tickets) {
        this.tickets = tickets;
    }
}
