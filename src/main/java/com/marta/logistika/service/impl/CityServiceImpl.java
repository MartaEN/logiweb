package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.service.api.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service("cityService")
public class CityServiceImpl extends AbstractService implements CityService {

    private final CityDao cityDao;

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @Override
    public void add(CityEntity city) {
        cityDao.add(city);
    }

    @Override
    public void updateCity(CityEntity city) {
        cityDao.update(city);
    }

    @Override
    public void removeCity(long id) {
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        cityDao.remove(city);
    }

    @Override
    public List<CityEntity> listAll() {
        return cityDao.listAll();
    }

    @Override
    public CityEntity getCityById(long id) {
        return cityDao.findById(id);
    }

    @Override
    public CityRecord getCityRecordById(long id) {
        return mapper.map(cityDao.findById(id), CityRecord.class);
    }

    @Override
    public List<CityRecord> listAllDtos() {
        return cityDao.listAll()
                .stream()
                .map(c -> mapper.map(c, CityRecord.class))
                .collect(Collectors.toList());
    }
}
