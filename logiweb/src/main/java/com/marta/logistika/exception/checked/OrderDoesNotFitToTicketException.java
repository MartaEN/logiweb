package com.marta.logistika.exception.checked;

import java.util.Locale;

public class OrderDoesNotFitToTicketException extends CheckedServiceException {

    private long orderId;
    private long ticketId;
    private String details;

    public OrderDoesNotFitToTicketException(long ticketId, String details){
        super("com.marta.logistika.error.orderDoesNotFitToTicket");
        this.ticketId = ticketId;
        this.details = details;
    }

    public OrderDoesNotFitToTicketException(OrderDoesNotFitToTicketException e, long orderId){
        super("com.marta.logistika.error.orderDoesNotFitToTicket");
        this.ticketId = e.getTicketId();
        this.details = e.getDetails();
        this.orderId = orderId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), orderId);
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
