package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

import java.io.Serializable;

public class StopoverShortRecord implements Serializable {
    private int sequenceNo;
    private CityEntity city;
    private int totalWeight;

    public int getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(int sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public CityEntity getCity() {
        return city;
    }

    public void setCity(CityEntity city) {
        this.city = city;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }
}
