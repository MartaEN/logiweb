package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.CityDao;
import com.marta.logistika.entity.CityEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("cityRepository")
public class CityDaoImpl extends AbstractDao implements CityDao {

    @Override
    public void add(CityEntity city) {
        em.getTransaction().begin();
        em.persist(city);
        em.getTransaction().commit();
    }

    @Override
    public void update(CityEntity city) {
        em.getTransaction().begin();
        em.merge(city);
        em.getTransaction().commit();
    }

    @Override
    public void remove(CityEntity city) {
        em.getTransaction().begin();
        em.remove(city);
        em.getTransaction().commit();
    }

    @Override
    public CityEntity getCityById(long id) {
        em.getTransaction().begin();
        CityEntity city = em.find(CityEntity.class, id);
        em.getTransaction().commit();
        return city;
    }

    @Override
    public List<CityEntity> listAll() {
        em.getTransaction().begin();
        List<CityEntity> cityList = em.createQuery("SELECT e FROM CityEntity e", CityEntity.class).getResultList();
        em.getTransaction().commit();
        return cityList;
    }
}
