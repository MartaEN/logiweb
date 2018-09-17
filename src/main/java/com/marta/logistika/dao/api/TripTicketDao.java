package com.marta.logistika.dao.api;

import com.marta.logistika.entity.TripTicketEntity;

public interface TripTicketDao extends GenericDao<TripTicketEntity> {

    TripTicketEntity findById (long id);
}
