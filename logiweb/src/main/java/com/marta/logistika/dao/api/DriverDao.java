package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface DriverDao extends GenericDao<DriverEntity> {

    boolean personalIdExists (String personalId);
    boolean usernameExists (String username);
    DriverEntity findByPersonalId (String personalId);
    DriverEntity findByUsername (String username);
    List<DriverEntity> listAll();
    List<DriverEntity> listAllAvailable(CityEntity fromCity, LocalDateTime time);
}