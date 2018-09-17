package com.marta.logistika.service;

import com.marta.logistika.dao.api.TruckDao;
import com.marta.logistika.dao.impl.TruckDaoImpl;
import com.marta.logistika.dto.TruckRecord;
import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.service.api.TruckService;
import com.marta.logistika.service.impl.TruckServiceImpl;
import org.junit.Assert;
import org.junit.Test;

public class TruckServiceTest {

    private TruckDao truckDao = new TruckDaoImpl();
    private TruckService truckService = new TruckServiceImpl(truckDao);

}
