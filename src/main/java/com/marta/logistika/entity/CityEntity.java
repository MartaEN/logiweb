package com.marta.logistika.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "cities")
public class CityEntity extends AbstractEntity {

    @Column(name = "name", unique = true, nullable = false)
    private String name;

}
