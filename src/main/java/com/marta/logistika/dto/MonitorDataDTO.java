package com.marta.logistika.dto;

import java.io.Serializable;
import java.util.List;

public class MonitorDataDTO implements Serializable {

    private List<OrderRecordShort> orders;
    private List<TripTicketRecord> tickets;

    public List<OrderRecordShort> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderRecordShort> orders) {
        this.orders = orders;
    }

    public List<TripTicketRecord> getTickets() {
        return tickets;
    }

    public void setTickets(List<TripTicketRecord> tickets) {
        this.tickets = tickets;
    }
}
