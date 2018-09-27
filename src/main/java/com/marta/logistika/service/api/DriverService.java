package com.marta.logistika.service.api;

import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.dto.TripTicketRecord;

import java.util.List;

public interface DriverService {

    void add(DriverRecord driver);
    void update(DriverRecord driver);
    void remove(String personalId);
    boolean personalIdExists (String personalId);
    DriverRecord findDriverByPersonalId(String personalId);
    String findPersonalIdByUsername(String username);
    List<DriverRecord> listAll();
    List<DriverRecord> findDrivers(long ticketId);
}
