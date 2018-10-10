package com.marta.logistika.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
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

    @Column (nullable = false)
    private boolean isParked;

    @Column (nullable = false)
    private LocalDateTime bookedUntil;


    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "location")
    private CityEntity location;

    @PrePersist
    public void setDefaults() {
        isParked = true;
        bookedUntil = LocalDateTime.now();
    }

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

    public boolean isParked() {
        return isParked;
    }

    public void setParked(boolean parked) {
        isParked = parked;
    }

    public LocalDateTime getBookedUntil() {
        return bookedUntil;
    }

    public void setBookedUntil(LocalDateTime bookedUntil) {
        this.bookedUntil = bookedUntil;
    }

    public CityEntity getLocation() {
        return location;
    }

    public void setLocation(CityEntity location) {
        this.location = location;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TruckEntity that = (TruckEntity) o;
        return id == that.id &&
                Objects.equals(regNumber, that.regNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, regNumber);
    }

    @Override
    public String toString() {
        return "TruckEntity{" +
                "regNumber='" + regNumber + '\'' +
                ", capacity=" + capacity +
                ", shiftSize=" + shiftSize +
                ", isServiceable=" + isServiceable +
                ", isParked=" + isParked +
                ", location=" + location +
                '}';
    }
}
