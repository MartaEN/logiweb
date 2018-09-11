package com.marta.logistika.dao.api;

import com.marta.logistika.entity.CityEntity;

import java.util.List;

public interface CityDao extends GenericDao<CityEntity> {

    CityEntity findById (long id);
    List<CityEntity> listAll();
}
