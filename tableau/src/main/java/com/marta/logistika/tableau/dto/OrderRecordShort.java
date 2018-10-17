package com.marta.logistika.tableau.dto;

import java.io.Serializable;

public class OrderRecordShort implements Serializable {

    private long id;
    private String creationDate;
    private CityRecord fromCity;
    private CityRecord toCity;
    private int weight;
    private OrderStatus status;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
