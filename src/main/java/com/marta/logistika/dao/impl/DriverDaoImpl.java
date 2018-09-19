package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.DriverDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Repository("driverRepository")
public class DriverDaoImpl extends AbstractDao<DriverEntity> implements DriverDao {

    @Override
    public boolean personalIdExists(String personalId) {
        Long count = em.createQuery("SELECT COUNT (d) FROM DriverEntity d WHERE d.personalId=:personalId", Long.class)
                .setParameter("personalId", personalId)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public DriverEntity findByPersonalId(String personalId) {
        if(personalId == null) return null;
        return em.createQuery(
                "SELECT d FROM DriverEntity d WHERE d.personalId=:personalId",
                DriverEntity.class)
                .setParameter("personalId", personalId)
                .getSingleResult();
    }

    @Override
    public List<DriverEntity> listAll() {
        return em.createQuery(
                "SELECT d FROM DriverEntity d ORDER BY d.lastName",
                DriverEntity.class)
                .getResultList();
    }

//    @Override
//    public List<DriverEntity> listAll(CityEntity fromCity, Map<YearMonth, Long> requiredMinutes) {
//        return em.createQuery(
//                "SELECT d FROM DriverEntity d WHERE" +
//                        "" +
//                        "" +
//                        "ORDER BY d.lastName",
//                DriverEntity.class)
//                .getResultList();
//
//
//
//        return null;
//    }
}
