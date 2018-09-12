package com.marta.logistika.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "trucks")
public class TruckEntity extends AbstractEntity {

    @Column (unique = true, nullable = false, length = 7)
    private String regNumber;

    @Column (nullable = false)
    private int capacity;

    @Column (nullable = false)
    private int shiftSize;

    @Column (nullable = false)
    private boolean isServiceable;

    @Column (nullable = false, columnDefinition="tinyint(1) default 1")
    private boolean isParked;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "location")
    private CityEntity location;

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

    public boolean isServiceable() {
        return isServiceable;
    }

    public void setServiceable(boolean serviceable) {
        isServiceable = serviceable;
    }

    public CityEntity getLocation() {
        return location;
    }

    public void setLocation(CityEntity location) {
        this.location = location;
    }
}
