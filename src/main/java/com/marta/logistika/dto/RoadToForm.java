package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;
import lombok.Data;

@Data
public class RoadToForm {
    private CityEntity toCity;
    private int distance;

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
