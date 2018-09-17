package com.marta.logistika.service.api;

import com.marta.logistika.dto.RoadRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;

import java.util.List;

public interface RoadService {

    void add(RoadEntity road);
    void remove(long roadId);
    List<RoadRecord> listAll();
    List<RoadRecord> listAllRoadsFrom(CityEntity city);
    List<RoadRecord> findRouteFromTo (CityEntity fromCity, CityEntity toCity);
    int getDistanceFromTo (CityEntity fromCity, CityEntity toCity);
    int getRouteDistance(List<CityEntity> route);

}
