package com.marta.logistika.exception.checked;

import com.marta.logistika.entity.CityEntity;

import java.util.Locale;

public class NoRouteFoundException extends CheckedServiceException {

    private final CityEntity fromCity;
    private final CityEntity toCity;

    public NoRouteFoundException(CityEntity fromCity, CityEntity toCity) {
        super("com.marta.logistika.error.noRouteFound");
        this.fromCity = fromCity;
        this.toCity = toCity;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), fromCity.getName(), toCity.getName());
    }
}
