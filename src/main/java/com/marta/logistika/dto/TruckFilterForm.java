package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

import java.time.LocalDateTime;

public class TruckFilterForm {

    private LocalDateTime departureDate;
    private int minCapacity;
    private int maxCapacity;
    private CityEntity fromCity;
    private CityEntity toCity;
//    private String truckRegNumber;

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public int getMinCapacity() {
        return minCapacity;
    }

    public void setMinCapacity(int minCapacity) {
        this.minCapacity = minCapacity;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public CityEntity getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityEntity fromCity) {
        this.fromCity = fromCity;
    }

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }

    //    public String getTruckRegNumber() {
//        return truckRegNumber;
//    }
//
//    public void setTruckRegNumber(String truckRegNumber) {
//        this.truckRegNumber = truckRegNumber;
//    }
}
