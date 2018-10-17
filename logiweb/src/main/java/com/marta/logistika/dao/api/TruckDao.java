package com.marta.logistika.dao.api;

import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.entity.TruckEntity;

import java.util.List;

public interface TruckDao extends GenericDao<TruckEntity> {

    boolean regNumberExists (String regNumber);
    TruckEntity findByRegNumber (String regNumber);
    List<TruckEntity> listAll();
    List<TruckEntity> listAllFilteredBy(TruckFilterForm filter);
    long countOnline();
    long countOffline();
    long countUnserviceable();

}
