package com.marta.logistika.service.impl;

import com.marta.logistika.exception.DuplicateCityException;
import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.entity.CityEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("cityService")
public class CityServiceImpl extends AbstractService implements CityService {

    private final CityDao cityDao;

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    @Transactional
    public void add(CityEntity city) throws DuplicateCityException {
        if(cityDao.cityNameExists(city.getName())) throw new DuplicateCityException(city.getName());
        cityDao.add(city);
    }

    @Override
    @Transactional
    public void remove(long id) {
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        cityDao.remove(city);
    }

    @Override
    public List<CityEntity> listAll() {
        return cityDao.listAll();
    }

    @Override
    public CityEntity findById(long id) throws EntityNotFoundException {
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        return city;
    }

}
