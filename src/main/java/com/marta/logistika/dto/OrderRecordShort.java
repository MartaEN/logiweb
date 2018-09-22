package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

import java.io.Serializable;

public class OrderRecordShort implements Serializable {

    private long id;
    private String creationDate;
    private CityEntity fromCity;
    private CityEntity toCity;
    private int weight;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
