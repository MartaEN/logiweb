package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;

import java.util.LinkedList;
import java.util.List;

public interface RoadService {

    void add(RoadEntity road);
    void remove(long roadId);
    List<RoadEntity> listAll();
    List<RoadEntity> listAllRoadsFrom(CityEntity city);
    RoadEntity getDirectRoadFromTo (CityEntity fromCity, CityEntity toCity);
    LinkedList<RoadEntity> findRouteFromTo (CityEntity fromCity, CityEntity toCity);

}
