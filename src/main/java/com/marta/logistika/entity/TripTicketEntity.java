package com.marta.logistika.entity;

import com.marta.logistika.enums.TripTicketStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
@Table(name = "trips")
public class TripTicketEntity extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private TripTicketStatus status;

    @Column (name = "departure", nullable = false)
    private LocalDateTime departureDate;

    @Column (name = "arrival")
    private LocalDateTime arrivalDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "truck")
    private TruckEntity truck;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="shifts",  joinColumns={@JoinColumn(name="trip")},
            inverseJoinColumns={@JoinColumn(name="driver")})
    private List<DriverEntity> drivers = new ArrayList<>();

    //про EAGER: я знаю, что так нельзя, просто хотела оттестировать бизнес-логику
    //до того как углубиться в настройки JPA. Поправлю
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "trip", nullable = false)
    private List<StopoverEntity> stopovers = new ArrayList<>();

    public TripTicketStatus getStatus() {
        return status;
    }

    public void setStatus(TripTicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDateTime arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public TruckEntity getTruck() {
        return truck;
    }

    public void setTruck(TruckEntity truck) {
        this.truck = truck;
    }

    public List<DriverEntity> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverEntity> drivers) {
        this.drivers = drivers;
    }

    public List<StopoverEntity> getStopovers() {
        stopovers.sort(StopoverEntity::compareTo);
        return stopovers;
    }

    public void setStopovers(List<StopoverEntity> stopovers) {
        this.stopovers = stopovers;
    }

    public StopoverEntity getStopoverWithSequenceNo (int sequenceNo) {
        return stopovers.stream()
                .filter(s -> s.getSequenceNo() == sequenceNo)
                .findFirst().orElse(null);
    }

    public List<CityEntity> getCities() {
        return getStopovers().stream().map(StopoverEntity::getCity).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripTicketEntity that = (TripTicketEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TripTicketEntity{" +
                "id=" + id +
                ", status=" + status.name() +
                ", startingDate=" + departureDate +
                ", truck=" + truck.getRegNumber() +
                ", drivers=" + drivers +
                ", stopovers=" + stopovers +
                '}';
    }
}
