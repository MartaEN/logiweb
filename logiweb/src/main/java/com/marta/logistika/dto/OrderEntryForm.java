package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

import java.io.Serializable;

public class OrderEntryForm implements Serializable {

    private String description;
    private int weight;
    private CityEntity fromCity;
    private CityEntity toCity;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
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
}
