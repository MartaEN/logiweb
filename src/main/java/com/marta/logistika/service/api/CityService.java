package com.marta.logistika.service.api;

import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.entity.CityEntity;

import java.util.List;

public interface CityService {

    void add(CityEntity city);
//    void updateCity(CityRecord city);
    List<CityRecord> listAll();
    CityRecord getCityById(long id);
//    void removeCity(long id);

}
