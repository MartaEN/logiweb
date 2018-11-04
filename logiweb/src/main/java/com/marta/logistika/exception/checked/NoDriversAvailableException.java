package com.marta.logistika.exception.checked;

import java.util.Locale;

public class NoDriversAvailableException extends CheckedServiceException {

    private final long ticketId;
    private final String city;
    private final String departureDate;

    public NoDriversAvailableException(long ticketId, String city, String departureDate) {
        super("com.marta.logistika.error.noDriversAvailable");
        this.ticketId = ticketId;
        this.city = city;
        this.departureDate = departureDate;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), ticketId, city, departureDate);
    }
}
