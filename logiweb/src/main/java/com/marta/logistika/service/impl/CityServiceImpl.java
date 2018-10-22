package com.marta.logistika.service.impl;

import com.marta.logistika.controller.TripTicketController;
import com.marta.logistika.exception.checked.DuplicateCityException;
import com.marta.logistika.exception.unchecked.EntityNotFoundException;
import com.marta.logistika.service.api.CityService;
import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.entity.CityEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("cityService")
public class CityServiceImpl extends AbstractService implements CityService {

    private final CityDao cityDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(TripTicketController.class);

    @Autowired
    public CityServiceImpl(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    /**
     * Adds a new city
     * @param city new city
     * @throws DuplicateCityException in case new city name is already registered in the system
     */
    @Override
    @Transactional
    public void add(CityEntity city) throws DuplicateCityException {
        LOGGER.debug(String.format("###LOGIWEB### CityServiceImpl::add(%s)", city));
        if(cityDao.cityNameExists(city.getName())) throw new DuplicateCityException(city.getName());
        cityDao.add(city);
    }

    /**
     * Removes a city
     * @param id city id to be removed
     */
    @Override
    @Transactional
    public void remove(long id) {
        LOGGER.debug(String.format("###LOGIWEB### CityServiceImpl::remove(%d)", id));
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        cityDao.remove(city);
    }

    /**
     * finds city by id
     * @param id id
     * @return city
     */
    @Override
    public CityEntity findById(long id) {
        LOGGER.debug(String.format("###LOGIWEB### CityServiceImpl::findbyId(%d)", id));
        CityEntity city = cityDao.findById(id);
        if (city == null) throw new EntityNotFoundException(id, CityEntity.class);
        return city;
    }

    /**
     * Lists all the cities
     * @return cities list
     */
    @Override
    public List<CityEntity> listAll() {
        LOGGER.debug("###LOGIWEB### CityServiceImpl::listAll()");
        return cityDao.listAll();
    }

}
