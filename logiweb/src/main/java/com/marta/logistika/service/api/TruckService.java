package com.marta.logistika.service.api;

import com.marta.logistika.dto.TruckFilterForm;
import com.marta.logistika.dto.TruckRecord;

import java.util.LinkedHashMap;
import java.util.List;

public interface TruckService {

    void add(TruckRecord truck);

    void update(TruckRecord truck);

    void remove(TruckRecord truck);

    List<TruckRecord> listAll();

    List<TruckRecord> listAllFilteredBy(TruckFilterForm filter);

    TruckRecord findTruckByRegNum(String regNum);

    LinkedHashMap<String, Long> getTruckStatistics();
}
