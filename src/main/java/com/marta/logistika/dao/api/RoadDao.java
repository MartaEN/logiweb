package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;

import java.util.List;

public interface RoadDao {

    void add(RoadEntity road);
    void remove(RoadEntity road);
    void setDistance(RoadEntity road, float distance);
    RoadEntity findById (long id);
    List<RoadEntity> listAll();
    List<RoadEntity> listAllRoadsFrom(CityEntity startCity);
    RoadEntity getDirectRoadFromTo (CityEntity startCity, CityEntity finishCity);
}
