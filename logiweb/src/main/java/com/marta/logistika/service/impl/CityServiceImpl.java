package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.exception.checked.DuplicateCityException;
import com.marta.logistika.exception.unchecked.EntityNotFoundException;
import com.marta.logistika.service.api.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("cityService")
public class CityServiceImpl extends AbstractService implements CityService {

    private final CityDao cityDao;

    /**
     * {@inheritDoc}
     */
    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public long add(CityRecord city) throws DuplicateCityException {
        if (cityDao.cityNameExists(city.getName())) throw new DuplicateCityException(city.getName());
        return cityDao.add(mapper.map(city, CityEntity.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void remove(long id) {
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        cityDao.remove(city);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CityEntity findById(long id) {
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        return city;
    }


    @Override
    public List<CityEntity> listAll() {
        return cityDao.listAll();
    }

}
