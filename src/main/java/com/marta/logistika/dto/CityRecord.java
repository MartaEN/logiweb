package com.marta.logistika.dto;

import lombok.Data;

@Data
public class CityRecord {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
