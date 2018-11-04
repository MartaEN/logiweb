package com.marta.logistika.service.api;

import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.exception.checked.DuplicateCityException;

import java.util.List;

public interface CityService {

    /**
     * Adds a new city
     *
     * @param city new city
     * @throws DuplicateCityException in case new city name is already registered in the system
     * @return id of the newly added city
     */
    long add(CityRecord city) throws DuplicateCityException;

    /**
     * Removes a city
     *
     * @param id city id to be removed
     */
    void remove(long id);

    /**
     * finds city by id
     *
     * @param id id
     * @return city
     */
    CityEntity findById(long id);

    /**
     * Lists all the cities
     *
     * @return cities list
     */
    List<CityEntity> listAll();
}
