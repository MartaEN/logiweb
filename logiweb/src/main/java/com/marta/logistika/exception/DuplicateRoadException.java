package com.marta.logistika.exception;

import com.marta.logistika.entity.CityEntity;

import java.util.Locale;

public class DuplicateRoadException extends ServiceException {

    private CityEntity fromCity;
    private CityEntity toCity;

    public DuplicateRoadException(CityEntity fromCity, CityEntity toCity) {
        super("com.marta.logistika.error.duplicateRoad");
        this.fromCity = fromCity;
        this.toCity = toCity;
    }

    @Override
    public String getLocalizedMessage(Locale locale) {
        return String.format(getLocalizedLabel(locale), fromCity.getName(), toCity.getName());
    }
}
