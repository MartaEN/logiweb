package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;
import lombok.Data;

@Data
public class TruckRecord {

    private String regNumber;
    private int capacity;
    private int shiftSize;
    private CityEntity location;
    private boolean isParked;
    private boolean isServiceable;

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getShiftSize() {
        return shiftSize;
    }

    public void setShiftSize(int shiftSize) {
        this.shiftSize = shiftSize;
    }

    public CityEntity getLocation() {
        return location;
    }

    public void setLocation(CityEntity location) {
        this.location = location;
    }

    public boolean isParked() {
        return isParked;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    public boolean isServiceable() {
        return isServiceable;
    }

    public void setServiceable(boolean serviceable) {
        isServiceable = serviceable;
    }
}
