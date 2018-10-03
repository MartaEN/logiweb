package com.marta.logistika.exception;

import java.util.Locale;

public class OrderDoesNotFitToTicketException extends ServiceException {

    private long orderId;

    public OrderDoesNotFitToTicketException(Throwable e, long orderId){
        super("com.marta.logistika.exceptions.orderDoesNotFitToTicket", e);
        this.orderId = orderId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), orderId);
    }
}
