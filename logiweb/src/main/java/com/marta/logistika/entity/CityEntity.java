package com.marta.logistika.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Objects;

@Entity
@Table(name = "cities")
public class CityEntity extends AbstractEntity {

    @Column(unique = true, nullable = false)
    @NotNull
    @Pattern(regexp = "(^[А-ЯЁ][А-Яа-яЁё0-9\\-\\s]+$)|(^[A-Z][A-Za-z0-9\\-\\s]+$)")
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
