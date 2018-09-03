package com.marta.logistika.service.api;

import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.entity.CityEntity;

import java.util.List;

public interface CityService {

    void add(CityEntity city);
    void updateCity(CityEntity city);
    void removeCity(long id);
    List<CityEntity> listAll();
    CityEntity getCityById(long id);

    //TODO как использовать DTO?
    CityRecord getCityRecordById(long id);
    List<CityRecord> listAllDtos();

}
