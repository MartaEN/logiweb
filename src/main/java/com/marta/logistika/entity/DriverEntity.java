package com.marta.logistika.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "drivers")
public class DriverEntity extends AbstractEntity {

    @Column (nullable = false, unique = true, length = 12)
    private String personalId;

    @Column (nullable = false, length = 65)
    private String firstName;

    @Column (nullable = false, length = 65)
    private String lastName;

    @Column (nullable = false)
    private LocalDateTime bookedUntil;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "location")
    private CityEntity location;

    @PrePersist
    public void setDefaults() {
        bookedUntil = LocalDateTime.now();
    }

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

    public LocalDateTime getBookedUntil() {
        return bookedUntil;
    }

    public void setBookedUntil(LocalDateTime bookedUntil) {
        this.bookedUntil = bookedUntil;
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
        DriverEntity that = (DriverEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(personalId, that.personalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, personalId);
    }

    @Override
    public String toString() {
        return "DriverEntity{" +
                "personalId='" + personalId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
