package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.entity.DriverEntity;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface DriverDao extends GenericDao<DriverEntity> {

    boolean personalIdExists (String personalId);
    DriverEntity findByPersonalId (String personalId);
    List<DriverEntity> listAll();
//    List<DriverEntity> listAll(CityEntity fromCity, Map<YearMonth, Long> requiredMinutes);
}
