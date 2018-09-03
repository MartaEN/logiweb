package com.marta.logistika.dto;

import lombok.Data;

@Data
public class RoadRecord {

    private CityRecord fromCity;
    private CityRecord toCity;
    private float distance;

}
