package com.marta.logistika.dto;

public class CityRecord {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CityRecord{" +
                "name='" + name + '\'' +
                '}';
    }
}
