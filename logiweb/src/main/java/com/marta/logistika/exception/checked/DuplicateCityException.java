package com.marta.logistika.exception.checked;

import java.util.Locale;

public class DuplicateCityException extends CheckedServiceException {

    private String cityName;

    public DuplicateCityException(String cityName) {
        super("com.marta.logistika.error.duplicateCity");
        this.cityName = cityName;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), cityName);
    }
}

