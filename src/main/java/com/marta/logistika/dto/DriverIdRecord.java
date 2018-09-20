package com.marta.logistika.dto;

import java.io.Serializable;

public class DriverIdRecord implements Serializable {

    private String personalId;

    public DriverIdRecord() {
    }

    public DriverIdRecord(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }
}
