package com.marta.logistika.entity;

import javax.persistence.*;

@Entity
@Table(name = "roads")
public class RoadEntity extends AbstractEntity {

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "fromCity")
    private CityEntity fromCity;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "toCity")
    private CityEntity toCity;

    @Column
    private float distance;

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

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
