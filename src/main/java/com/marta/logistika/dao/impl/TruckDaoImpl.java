package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.entity.TruckEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("truckRepository")
public class TruckDaoImpl extends AbstractDao<TruckEntity> implements TruckDao {

    @Override
    public TruckEntity findByRegNumber(String regNumber) {
        if(regNumber == null) return null;
        return em.createQuery(
                "SELECT t FROM TruckEntity t WHERE t.regNumber=:regNumber",
                TruckEntity.class)
                .setParameter("regNumber", regNumber)
                .getSingleResult();
    }

    @Override
    public List<TruckEntity> listAll() {
        return em.createQuery(
                "SELECT t FROM TruckEntity t",
                TruckEntity.class)
                .getResultList();
    }
}
