package com.marta.logistika.dao.api;

import com.marta.logistika.entity.TruckEntity;

import java.util.List;

public interface TruckDao extends GenericDao<TruckEntity> {

    TruckEntity findByRegNumber (String regNumber);
    List<TruckEntity> listAll();

}
