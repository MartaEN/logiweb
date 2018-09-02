package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.dto.CityRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service("cityService")
public class CityServiceImpl extends AbstractService implements CityService {

    @Autowired
    private CityDao cityDao;

    @Override
    public void add(CityEntity city) {
        cityDao.add(city);
    }

    @Override
    public List<CityRecord> listAll() {
        return cityDao.listAll()
                .stream()
                .map(c -> mapper.map(c, CityRecord.class))
                .collect(Collectors.toList());
    }

    @Override
    public CityRecord getCityById(long id) {
        CityEntity city = cityDao.getCityById(id);
        return mapper.map(city, CityRecord.class);
    }

}
