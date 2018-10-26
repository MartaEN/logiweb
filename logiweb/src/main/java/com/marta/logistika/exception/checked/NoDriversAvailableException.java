package com.marta.logistika.exception.checked;

import java.util.Locale;

public class NoDriversAvailableException extends CheckedServiceException {

    private long ticketId;

    public NoDriversAvailableException(long ticketId) {
        super("com.marta.logistika.error.noDriversAvailable");
        this.ticketId = ticketId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedLabel(locale);
    }
}
