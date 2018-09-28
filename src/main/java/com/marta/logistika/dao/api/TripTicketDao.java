package com.marta.logistika.dao.api;

import com.marta.logistika.entity.TripTicketEntity;
import com.marta.logistika.enums.TripTicketStatus;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;

public interface TripTicketDao extends GenericDao<TripTicketEntity> {

    TripTicketEntity findById (long id);
    List<TripTicketEntity> listAll();
    List<TripTicketEntity> listAllUnapproved();
    @Nullable TripTicketEntity findByDriverAndStatus(String personalId, TripTicketStatus status);
    @Nullable TripTicketEntity findByTruckAndStatus(String regNumber, TripTicketStatus status);

}
