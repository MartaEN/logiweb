package com.marta.logistika.service.impl;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.exception.EntityNotFoundException;
import com.marta.logistika.exception.SuchEntityAlreadyExistsException;
import com.marta.logistika.service.api.RoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("roadService")
public class RoadServiceImpl extends AbstractService implements RoadService {

    private final RoadDao roadDao;

    @Autowired
    public RoadServiceImpl(RoadDao roadDao) {
        this.roadDao = roadDao;
    }

    @Override
    @Transactional
    public void add(RoadEntity road) {

        //check road parameters validity
        if(road == null) return;
        if(road.getFromCity() == null || road.getToCity() == null) throw new IllegalArgumentException("Road id" + road.getId() + " invalid: null end points");
        if(road.getFromCity().equals(road.getToCity())) throw new IllegalArgumentException("Road id" + road.getId() + " invalid: coinciding end points");
        if(roadDao.getDirectRoadFromTo(road.getFromCity(), road.getToCity()) != null) throw new SuchEntityAlreadyExistsException(road.getId(), RoadEntity.class);
        if(road.getDistance() < 0.1f) throw new IllegalArgumentException("Road id" + road.getId() + ": distance " + road.getDistance() + " invalid; should be positive");

        //create return road
        RoadEntity returnRoad = new RoadEntity();
        returnRoad.setFromCity(road.getToCity());
        returnRoad.setToCity(road.getFromCity());
        returnRoad.setDistance(road.getDistance());

        //persist both roads
        roadDao.add(road);
        roadDao.add(returnRoad);
    }

    @Override
    @Transactional
    public void remove(long roadId) {
        //find requested road
        RoadEntity road = roadDao.findById(roadId);
        if (road == null) throw new EntityNotFoundException(roadId, RoadEntity.class);

        //find return road
        RoadEntity returnRoad = roadDao.getDirectRoadFromTo(road.getToCity(), road.getFromCity());

        //delete both roads
        roadDao.remove(road);
        roadDao.remove(returnRoad);
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
    public RoadEntity getDirectRoadFromTo(CityEntity fromCity, CityEntity toCity) {
        return roadDao.getDirectRoadFromTo(fromCity, toCity);
    }
}
