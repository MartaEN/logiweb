package com.marta.logistika.dao.impl;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.dao.api.CityDao;
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
}
