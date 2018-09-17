package com.marta.logistika.service.api;

import com.marta.logistika.dto.TruckRecord;

import java.util.List;

public interface TruckService {

    void add(TruckRecord truck);
    void update(TruckRecord truck);
    void remove(TruckRecord truck);
    List<TruckRecord> listAll();
    TruckRecord findTruckByRegNum(String regNum);
}
