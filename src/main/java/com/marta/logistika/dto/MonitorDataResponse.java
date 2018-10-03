package com.marta.logistika.dto;

import java.io.Serializable;
import java.util.List;

public class MonitorDataResponse implements Serializable {

    private List<OrderRecordShort> orders;
    private List<TripTicketRecord> tickets;
    private String error;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
