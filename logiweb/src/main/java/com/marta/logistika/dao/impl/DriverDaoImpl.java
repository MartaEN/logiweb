package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.DriverDao;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.enums.DriverStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
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
    public boolean usernameExists(String username) {
        Long count = em.createQuery("SELECT COUNT (d) FROM DriverEntity d WHERE d.username=:username", Long.class)
                .setParameter("username", username)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public DriverEntity findByPersonalId(String personalId) {
        if (personalId == null) return null;
        return em.createQuery(
                "SELECT d FROM DriverEntity d WHERE d.personalId=:personalId",
                DriverEntity.class)
                .setParameter("personalId", personalId)
                .getSingleResult();
    }

    @Override
    public DriverEntity findByUsername(String username) {
        if (username == null) return null;
        return em.createQuery(
                "SELECT d FROM DriverEntity d WHERE d.username=:username",
                DriverEntity.class)
                .setParameter("username", username)
                .getSingleResult();
    }

    @Override
    public List<DriverEntity> listAll() {
        return em.createQuery(
                "SELECT d FROM DriverEntity d ORDER BY d.lastName",
                DriverEntity.class)
                .getResultList();
    }

    @Override
    public List<DriverEntity> listAllAvailable(CityEntity fromCity, LocalDateTime time) {
        return em.createQuery(
                "SELECT d FROM DriverEntity d " +
                        "WHERE d.location =: fromCity AND d.bookedUntil <= :time " +
                        "ORDER BY d.lastName",
                DriverEntity.class)
                .setParameter("fromCity", fromCity)
                .setParameter("time", time)
                .getResultList();
    }

    @Override
    public Map<DriverStatus, Integer> getDriverStatistics() {
        Map<DriverStatus, Integer> driverStats = new HashMap<>();
        List<Object[]> results = em.createQuery(
                "SELECT d.status AS status, COUNT(d) AS total FROM DriverEntity d GROUP BY d.status")
                .getResultList();
        for (Object[] result : results) {
            driverStats.put((DriverStatus) result[0], ((Number) result[1]).intValue());
        }
        return driverStats;
    }
}
