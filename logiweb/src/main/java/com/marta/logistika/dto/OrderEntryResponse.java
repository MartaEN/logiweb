package com.marta.logistika.dto;

import java.io.Serializable;

public class OrderEntryResponse implements Serializable {

    private String description;
    private int weight;
    private long fromCity;
    private long toCity;

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

    public long getFromCity() {
        return fromCity;
    }

    public void setFromCity(long fromCity) {
        this.fromCity = fromCity;
    }

    public long getToCity() {
        return toCity;
    }

    public void setToCity(long toCity) {
        this.toCity = toCity;
    }
}
