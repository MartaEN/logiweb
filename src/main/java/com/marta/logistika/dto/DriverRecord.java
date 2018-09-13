package com.marta.logistika.dto;

import java.util.Objects;

public class DriverRecord {

    private String personalId;
    private String firstName;
    private String lastName;
    private String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
