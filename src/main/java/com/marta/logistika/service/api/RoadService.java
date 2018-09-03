package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;

import java.util.List;

public interface RoadService {

    void add(RoadEntity road);
    void merge(RoadEntity road);
    void remove(long id);
    void setDistance(RoadEntity road, float distance);
    List<RoadEntity> listAll();
    List<RoadEntity> listAllRoadsFrom(CityEntity city);
    RoadEntity getDirectRoadFromTo (CityEntity startCity, CityEntity finishCity);

}
