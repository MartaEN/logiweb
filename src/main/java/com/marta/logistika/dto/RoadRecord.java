package com.marta.logistika.dto;

import lombok.Data;

@Data
public class RoadRecord {

    private long fromCityId;
    private long toCityId;
    private float distance;

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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
