package com.marta.logistika.dao.api;

import com.marta.logistika.entity.DriverEntity;

import java.util.List;

public interface DriverDao extends GenericDao<DriverEntity> {

    boolean personalIdExists (String personalId);
    DriverEntity findByPersonalId (String personalId);
    List<DriverEntity> listAll();
}
