package com.marta.logistika.dao.api;

import com.marta.logistika.entity.TripTicketEntity;

import java.util.List;

public interface TripTicketDao extends GenericDao<TripTicketEntity> {

    TripTicketEntity findById (long id);
    List<TripTicketEntity> listAllUnapproved();
}
