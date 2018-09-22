package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;
import com.marta.logistika.validator.UniquePersonalId;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class DriverRecord implements Serializable {

    @NotNull
    @Size(min = 6, max = 6)
    @Pattern(regexp = "^[0-9]{6}$")
    @UniquePersonalId
    private String personalId;

    @NotNull
    @Size(min = 1, max = 65)
    @Pattern(regexp = "^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$")
    private String firstName;

    @NotNull
    @Size(min = 1, max = 65)
    @Pattern(regexp = "^[А-Яа-яЁё]+[-]?[А-Яа-яЁё]+$")
    private String lastName;

    @NotNull
    private CityEntity location;

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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
        DriverRecord that = (DriverRecord) o;
        return Objects.equals(personalId, that.personalId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(personalId);
    }

    @Override
    public String toString() {
        return "DriverRecord{" +
                "personalId='" + personalId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
