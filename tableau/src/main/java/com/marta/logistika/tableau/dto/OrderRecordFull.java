package com.marta.logistika.tableau.dto;

import java.io.Serializable;

public class OrderRecordFull implements Serializable {

    private long id;
    private String creationDate;
    private String description;
    private int weight;
    private CityRecord fromCity;
    private CityRecord toCity;
    private String status;

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

    public CityRecord getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityRecord fromCity) {
        this.fromCity = fromCity;
    }

    public CityRecord getToCity() {
        return toCity;
    }

    public void setToCity(CityRecord toCity) {
        this.toCity = toCity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
