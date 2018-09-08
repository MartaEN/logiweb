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
    public List<RoadEntity> listAllRoadsFrom(CityEntity fromCity) {
        if(fromCity == null) return null;
        return em.createQuery(
                "SELECT r FROM RoadEntity r WHERE r.fromCity=:fromCity ORDER BY r.distance",
                RoadEntity.class)
                .setParameter("fromCity", fromCity)
                .getResultList();
    }

    @Override
    public RoadEntity getDirectRoadFromTo(CityEntity fromCity, CityEntity toCity) {
        if (fromCity == null || toCity == null || fromCity.equals(toCity)) return null;
        return em.createQuery(
                "SELECT r FROM RoadEntity r WHERE r.fromCity=:fromCity AND r.toCity=:toCity",
                RoadEntity.class)
                .setParameter("fromCity", fromCity)
                .setParameter("toCity", toCity)
                .getSingleResult();
    }

}
