package com.marta.logistika.exception.checked;

import java.util.Locale;

public class OrderDoesNotFitToTicketException extends CheckedServiceException {

    private long orderId;

    public OrderDoesNotFitToTicketException(Throwable e, long orderId){
        super("com.marta.logistika.error.orderDoesNotFitToTicket", e);
        this.orderId = orderId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), orderId);
    }
}
