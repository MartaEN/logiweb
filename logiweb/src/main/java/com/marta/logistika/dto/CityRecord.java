package com.marta.logistika.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

public class CityRecord implements Serializable {

    @NotNull
    @Pattern(regexp = "(^[А-ЯЁ][А-Яа-яЁё0-9\\-\\s]+$)|(^[A-Z][A-Za-z0-9\\-\\s]+$)")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
