package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.entity.CityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("cityRepository")
public class CityDaoImpl extends AbstractDao<CityEntity> implements CityDao {

    @Override
    public CityEntity findById(long id) {
        return em.find(CityEntity.class, id);
    }

    @Override
    public List<CityEntity> listAll() {
        return em.createQuery(
                "SELECT c FROM CityEntity c ORDER BY c.name",
                CityEntity.class)
                .getResultList();
    }

    @Override
    public boolean cityNameExists(String cityName) {
        Long count = em.createQuery("SELECT COUNT (c) FROM CityEntity c WHERE c.name=:cityName", Long.class)
                .setParameter("cityName", cityName)
                .getSingleResult();
        return count > 0;
    }
}
