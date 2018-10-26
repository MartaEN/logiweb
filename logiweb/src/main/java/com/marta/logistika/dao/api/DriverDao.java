package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;
import com.marta.logistika.enums.DriverStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DriverDao extends GenericDao<DriverEntity> {

    boolean personalIdExists(String personalId);

    boolean usernameExists(String username);

    DriverEntity findByPersonalId(String personalId);

    DriverEntity findByUsername(String username);

    List<DriverEntity> listAll();

    List<DriverEntity> listAllAvailable(CityEntity fromCity, LocalDateTime time);

    Map<DriverStatus, Integer> getDriverStatistics();
}
