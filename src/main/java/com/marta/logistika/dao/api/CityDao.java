package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;

import java.util.List;

public interface CityDao {

    void add(CityEntity city);
    void update(CityEntity city);
    void remove(CityEntity city);
    CityEntity findById(long id);
    List<CityEntity> listAll();

}
