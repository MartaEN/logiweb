package com.marta.logistika.service.api;

import com.marta.logistika.dto.DriverRecord;

import java.util.List;

public interface DriverService {

    void add(DriverRecord driver);
    void update(DriverRecord driver);
    void remove(String personalId);
    boolean personalIdExists (String personalId);
    boolean usernameExists (String personalId);
    DriverRecord findDriverByPersonalId(String personalId);
    String findPersonalIdByUsername(String username);
    List<DriverRecord> listAll();
    List<DriverRecord> findDrivers(long ticketId);
}
