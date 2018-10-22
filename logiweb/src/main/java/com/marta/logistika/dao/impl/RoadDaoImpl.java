package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.RoadDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.RoadEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("roadRepository")
public class RoadDaoImpl extends AbstractDao<RoadEntity> implements RoadDao {

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
    public List<RoadEntity> listAllRoadsTo(CityEntity toCity) {
        if(toCity == null) return null;
        return em.createQuery(
                "SELECT r FROM RoadEntity r WHERE r.toCity=:toCity ORDER BY r.distance",
                RoadEntity.class)
                .setParameter("toCity", toCity)
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

    @Override
    public List<CityEntity> listAllUnlinkedCities(CityEntity fromCity) {
        return em.createQuery(
                "SELECT c FROM CityEntity c WHERE c NOT IN " +
                        "(SELECT r.toCity FROM RoadEntity r WHERE r.fromCity=:fromCity) " +
                        "AND c NOT IN (:fromCity) ",
                CityEntity.class)
                .setParameter("fromCity", fromCity)
                .getResultList();
    }

}
