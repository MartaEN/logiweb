package com.marta.logistika.entity;

import javax.persistence.*;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoadEntity that = (RoadEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "RoadEntity{" +
                "fromCity=" + fromCity +
                ", toCity=" + toCity +
                ", distance=" + distance +
                '}';
    }
}
