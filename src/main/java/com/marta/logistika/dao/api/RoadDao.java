package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;

import java.util.List;

public interface RoadDao {

    void add(RoadEntity road);
    void update(RoadEntity road);
    void remove(RoadEntity road);
    RoadEntity findById (long id);
    List<RoadEntity> listAll();
    List<RoadEntity> listAllRoadsFrom(CityEntity fromCity);
    RoadEntity getDirectRoadFromTo (CityEntity fromCity, CityEntity toCity);
}
