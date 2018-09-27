package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

public class OrderRecordDriverInstruction {

    private long id;
    private String description;
    private int weight;
    private CityEntity toCity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }
}
