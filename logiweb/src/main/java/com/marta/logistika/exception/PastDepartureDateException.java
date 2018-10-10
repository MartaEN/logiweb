package com.marta.logistika.exception;

import java.util.Locale;

public class PastDepartureDateException extends ServiceException {

    private long ticketId;

    public PastDepartureDateException(long ticketId){
        super("com.marta.logistika.error.pastDepartureDate");
        this.ticketId = ticketId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedLabel(locale);
    }
}
