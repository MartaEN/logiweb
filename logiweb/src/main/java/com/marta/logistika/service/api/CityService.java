package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.exception.checked.DuplicateCityException;

import java.util.List;

public interface CityService {

    void add(CityEntity city) throws DuplicateCityException;

    void remove(long id);

    CityEntity findById(long id);

    List<CityEntity> listAll();
}
