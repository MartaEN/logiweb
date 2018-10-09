package com.marta.logistika.exception;

import java.util.Locale;

public class NoDriversAvailableException extends ServiceException {

    private long ticketId;

    public NoDriversAvailableException(long ticketId){
        super("com.marta.logistika.error.noDriversAvailable");
        this.ticketId = ticketId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedLabel(locale);
    }
}
