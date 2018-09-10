package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;

import java.util.List;

public interface CityService {

    void add(CityEntity city);
    void update(CityEntity city);
    void remove(long id);
    List<CityEntity> listAll();
    CityEntity getCityById(long id);

}
