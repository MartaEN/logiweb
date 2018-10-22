package com.marta.logistika.service.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.exception.DuplicateCityException;
import com.marta.logistika.exception.EntityNotFoundException;

import java.util.List;

public interface CityService {

    void add(CityEntity city) throws DuplicateCityException;
    void remove(long id);
    List<CityEntity> listAll();
    CityEntity findById(long id) throws EntityNotFoundException;

}
