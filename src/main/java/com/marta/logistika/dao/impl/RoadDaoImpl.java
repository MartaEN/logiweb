package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roadRepository")
public class RoadDaoImpl extends AbstractDao implements RoadDao {

    @Override
    public void add(RoadEntity road) {
        em.persist(road);
    }

    @Override
    public void update(RoadEntity road) {
        em.merge(road);
    }

    @Override
    public void remove(RoadEntity road) {
        em.remove(road);
    }

    @Override
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
    public List<RoadEntity> listAllRoadsFrom(CityEntity startCity) {
        if(startCity == null) return null;
        return em.createQuery(
                "SELECT r FROM RoadEntity r WHERE r.fromCity=:startCity ORDER BY r.distance",
                RoadEntity.class)
                .setParameter("startCity", startCity)
                .getResultList();
    }

    @Override
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
