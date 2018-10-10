package com.marta.logistika.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "cities")
public class CityEntity extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CityEntity that = (CityEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CityEntity{" +
                "name='" + name + '\'' +
                '}';
    }
}
