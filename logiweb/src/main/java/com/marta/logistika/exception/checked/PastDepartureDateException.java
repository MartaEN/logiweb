package com.marta.logistika.exception.checked;

import java.util.Locale;

public class PastDepartureDateException extends CheckedServiceException {

    private long ticketId;

    public PastDepartureDateException(long ticketId) {
        super("com.marta.logistika.error.pastDepartureDate");
        this.ticketId = ticketId;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedLabel(locale);
    }
}
