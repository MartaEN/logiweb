package com.marta.logistika.dao.impl;

import com.marta.logistika.dao.api.TripTicketDao;
import com.marta.logistika.entity.TripTicketEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("tripRepository")
public class TripTicketDaoImpl extends AbstractDao<TripTicketEntity> implements TripTicketDao {

    @Override
    @Transactional
    public TripTicketEntity findById(long id) {
        return em.find(TripTicketEntity.class, id);
    }
}
