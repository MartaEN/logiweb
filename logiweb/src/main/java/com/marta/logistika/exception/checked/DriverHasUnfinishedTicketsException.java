package com.marta.logistika.exception.checked;

import java.util.Locale;

public class DriverHasUnfinishedTicketsException extends CheckedServiceException {

    public DriverHasUnfinishedTicketsException() {
        super("com.marta.logistika.error.driverHasTickets");
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return getLocalizedLabel(locale);
    }

}
