package com.marta.logistika.service.api;

import com.marta.logistika.dto.DriverRecord;
import com.marta.logistika.exception.checked.DriverHasUnfinishedTicketsException;

import java.util.LinkedHashMap;
import java.util.List;

public interface DriverService {

    void add(DriverRecord driver);

    void update(DriverRecord driver);

    void remove(String personalId) throws DriverHasUnfinishedTicketsException;

    boolean personalIdExists(String personalId);

    boolean usernameExists(String personalId);

    DriverRecord findDriverByPersonalId(String personalId);

    List<DriverRecord> listAll();

    List<DriverRecord> findDrivers(long ticketId);

    LinkedHashMap<String, Integer> getDriverStatistics();
}
