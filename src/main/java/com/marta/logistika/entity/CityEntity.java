package com.marta.logistika.entity;

import javax.persistence.*;

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
}
