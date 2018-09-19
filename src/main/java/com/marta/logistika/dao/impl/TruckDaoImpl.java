package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.entity.TruckEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("truckRepository")
public class TruckDaoImpl extends AbstractDao<TruckEntity> implements TruckDao {

    @Override
    public boolean regNumberExists(String regNumber) {
        Long count = em.createQuery("SELECT COUNT (t) FROM TruckEntity t WHERE t.regNumber=:regNumber", Long.class)
                .setParameter("regNumber", regNumber)
                .getSingleResult();
        return count > 0;
    }

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

    @Override
    public List<TruckEntity> listAllFilteredBy(TruckFilterForm filter) {
        return em.createQuery(
                "SELECT t FROM TruckEntity t WHERE " +
                        "t.isServiceable = true " +
                        "AND t.bookedUntil < :departureDate " +
                        "AND t.capacity >= :minCapacity " +
                        "AND t.capacity <= :maxCapacity " +
                        "AND t.location = :fromCity",
                TruckEntity.class)
                .setParameter("departureDate", filter.getDepartureDate())
                .setParameter("minCapacity", filter.getMinCapacity())
                .setParameter("maxCapacity", filter.getMaxCapacity())
                .setParameter("fromCity", filter.getFromCity())
                .getResultList();

    }
}
