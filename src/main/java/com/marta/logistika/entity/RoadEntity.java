package com.marta.logistika.entity;

import javax.persistence.*;

@Entity
@Table(name = "roads")
public class RoadEntity extends AbstractEntity {

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "fromCity")
    private CityEntity fromCity;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "toCity")
    private CityEntity toCity;

    @Column (nullable = false)
    private int distance;

    public CityEntity getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityEntity fromCity) {
        this.fromCity = fromCity;
    }

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
