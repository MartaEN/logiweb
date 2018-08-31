package com.marta.logistika.service;

import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.model.City;
import com.marta.logistika.repository.CityRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("cityService")
public class CityService {

    private CityRepository cityRepository;

    public CityService(@Qualifier("cityRepository") CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public City addCity(City city) {
        return cityRepository.save(city);
    }

    public Iterable<City> getCities() {
        return cityRepository.findAll();
    }

    public City getCity(long cityId) {
        return cityRepository.findById(cityId)
                .orElseThrow(() -> new EntityNotFoundException(cityId, City.class));
    }

}
