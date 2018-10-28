package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.enums.TripTicketStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;

@Repository("ticketRepository")
public class TripTicketDaoImpl extends AbstractDao<TripTicketEntity> implements TripTicketDao {

    @Override
    @Transactional
    public TripTicketEntity findById(long id) {
        return em.find(TripTicketEntity.class, id);
    }

    @Override
    public List<TripTicketEntity> listAll() {
        return em.createQuery(
                "SELECT t FROM TripTicketEntity t",
                TripTicketEntity.class)
                .getResultList();
    }

    @Override
    public List<TripTicketEntity> listAllUnapproved() {
        return em.createQuery(
                "SELECT t FROM TripTicketEntity t  WHERE t.status='CREATED'",
                TripTicketEntity.class)
                .getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public @Nullable
    TripTicketEntity findByDriverAndStatus(String personalId, TripTicketStatus status) {
        TripTicketEntity ticket = null;
        try {
            ticket = em.createQuery(
                    "SELECT t FROM TripTicketEntity t JOIN t.drivers d WHERE t.status=:status AND d.personalId=:personalId",
                    TripTicketEntity.class)
                    .setParameter("status", status)
                    .setParameter("personalId", personalId)
                    .getSingleResult();
        } catch (NoResultException e) {
            //ok to return null in case of no result
        }
        return ticket;
    }

    @Override
    public @Nullable
    TripTicketEntity findByTruckAndStatus(String regNumber, TripTicketStatus status) {
        TripTicketEntity ticket = null;
        try {
            ticket = em.createQuery(
                    "SELECT t FROM TripTicketEntity t JOIN t.drivers d WHERE t.status=:status AND t.truck.regNumber=:regNumber",
                    TripTicketEntity.class)
                    .setParameter("status", status)
                    .setParameter("regNumber", regNumber)
                    .getSingleResult();
        } catch (NoResultException e) {
            //ok to return null in case of no result
        }
        return ticket;
    }

}
