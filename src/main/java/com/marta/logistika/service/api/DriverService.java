package com.marta.logistika.service.api;

import com.marta.logistika.dto.DriverRecord;

import java.util.List;

public interface DriverService {

    void add(DriverRecord driver);
    void update(DriverRecord driver);
    void remove(DriverRecord driver);
    List<DriverRecord> listAll();
    DriverRecord findDriverByPersonalId(String personalId);

}
