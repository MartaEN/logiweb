package com.marta.logistika.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DriverSelectForm implements Serializable {

    private List<DriverRecord> drivers = new ArrayList<>();

    public List<DriverRecord> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverRecord> drivers) {
        this.drivers = drivers;
    }
}

