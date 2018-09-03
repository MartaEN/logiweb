package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.service.api.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("roadService")
public class RoadServiceImpl extends AbstractService implements RoadService {

    private final RoadDao roadDao;

    @Autowired
    public RoadServiceImpl(RoadDao roadDao) {
        this.roadDao = roadDao;
    }

    @Override
    public void merge(RoadEntity road) {

    }

    @Override
    public void add(RoadEntity road) {
        roadDao.add(road);
    }

    @Override
    public void remove(long id) {
        RoadEntity road = roadDao.findById(id);
        if (road == null) throw new EntityNotFoundException(id, RoadEntity.class);
        roadDao.remove(road);
    }

    @Override
    public void setDistance(RoadEntity road, float distance) {
        roadDao.setDistance(road, distance);
    }

    @Override
    public List<RoadEntity> listAll() {
        return roadDao.listAll();
    }

    @Override
    public List<RoadEntity> listAllRoadsFrom(CityEntity city) {
        return roadDao.listAllRoadsFrom(city);
    }

    @Override
    public RoadEntity getDirectRoadFromTo(CityEntity startCity, CityEntity finishCity) {
        return roadDao.getDirectRoadFromTo(startCity, finishCity);
    }
}
