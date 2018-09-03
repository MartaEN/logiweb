package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import com.marta.logistika.exception.SuchEntityAlreadyExistsException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("roadRepository")
public class RoadDaoImpl extends AbstractDao implements RoadDao {

    @Override
    @Transactional
    public void add(RoadEntity road) {
        if(road == null) return;

        RoadEntity tmpRoad = getDirectRoadFromTo(road.getFromCity(), road.getToCity());
        if(tmpRoad != null) throw new SuchEntityAlreadyExistsException(tmpRoad.getId(), RoadEntity.class);

        RoadEntity returnRoad = new RoadEntity();
        returnRoad.setFromCity(road.getToCity());
        returnRoad.setToCity(road.getFromCity());
        returnRoad.setDistance(road.getDistance());

        em.persist(road);
        em.persist(returnRoad);
    }

    @Override
    @Transactional
    public void remove(RoadEntity road) {
        if(road == null) return;
        em.remove(getDirectRoadFromTo(road.getToCity(), road.getFromCity()));
        em.remove(road);
    }

    @Override
    @Transactional
    public void setDistance(RoadEntity road, float distance) {
        if(road == null) return;

        road.setDistance(distance);
        em.merge(road);

        RoadEntity returnRoad = getDirectRoadFromTo(road.getToCity(), road.getFromCity());
        returnRoad.setDistance(distance);
        em.merge(returnRoad);
    }

    @Override
    @Transactional(readOnly = true)
    public RoadEntity findById(long id) {
        return em.find(RoadEntity.class, id);
    }

    @Override
    public List<RoadEntity> listAll() {
        return em.createQuery(
                "SELECT r FROM RoadEntity r",
                RoadEntity.class)
                .getResultList();
    }

    @Override
    @Transactional (readOnly = true)
    public List<RoadEntity> listAllRoadsFrom(CityEntity startCity) {
        if(startCity == null) return null;
        return em.createQuery(
                "SELECT r FROM RoadEntity r WHERE r.fromCity=:startCity ORDER BY r.distance",
                RoadEntity.class)
                .setParameter("startCity", startCity)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoadEntity getDirectRoadFromTo(CityEntity startCity, CityEntity finishCity) {
        if (startCity == null || finishCity == null || startCity.equals(finishCity)) return null;
        return em.createQuery(
                "SELECT r FROM RoadEntity r WHERE r.fromCity=:startCity AND r.toCity=:finishCity",
                RoadEntity.class)
                .setParameter("startCity", startCity)
                .setParameter("finishCity", finishCity)
                .getSingleResult();
    }

}
