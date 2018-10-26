package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;

import java.util.List;

public interface RoadDao extends GenericDao<RoadEntity> {

    RoadEntity findById(long id);

    List<RoadEntity> listAll();

    List<RoadEntity> listAllRoadsFrom(CityEntity fromCity);

    List<RoadEntity> listAllRoadsTo(CityEntity toCity);

    RoadEntity getDirectRoadFromTo(CityEntity fromCity, CityEntity toCity);

    List<CityEntity> listAllUnlinkedCities(CityEntity fromCity);
}
