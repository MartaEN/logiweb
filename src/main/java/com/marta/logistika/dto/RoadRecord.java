package com.marta.logistika.dto;

import lombok.Data;

@Data
public class RoadRecord {

    private long fromCityId;
    private long toCityId;
    private int distance;

    public long getFromCityId() {
        return fromCityId;
    }

    public void setFromCityId(long fromCityId) {
        this.fromCityId = fromCityId;
    }

    public long getToCityId() {
        return toCityId;
    }

    public void setToCityId(long toCityId) {
        this.toCityId = toCityId;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
