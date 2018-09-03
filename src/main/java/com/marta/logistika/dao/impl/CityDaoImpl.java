package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.entity.CityEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("cityRepository")
public class CityDaoImpl extends AbstractDao implements CityDao {

    @Override
    @Transactional
    public void add(CityEntity city) {
        em.persist(city);
    }

    @Override
    @Transactional
    public void update(CityEntity city) {
        em.merge(city);
    }

    @Override
    @Transactional
    public void remove(CityEntity city) {
        em.remove(city);
    }

    @Override
    @Transactional (readOnly = true)
    public CityEntity findById(long id) {
        return em.find(CityEntity.class, id);
    }

    @Override
    @Transactional (readOnly = true)
    public List<CityEntity> listAll() {
        return em.createQuery(
                "SELECT c FROM CityEntity c ORDER BY c.name",
                CityEntity.class)
                .getResultList();
    }
}
