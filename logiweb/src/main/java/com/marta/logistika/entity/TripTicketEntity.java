package com.marta.logistika.entity;

import com.marta.logistika.enums.TripTicketStatus;
import org.dozer.Mapping;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "tickets")
public class TripTicketEntity extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private TripTicketStatus status;

    @Column(name = "departure", nullable = false)
    private LocalDateTime departureDateTime;

    @Column(name = "arrival")
    private LocalDateTime arrivalDateTime;

    @ManyToOne(optional = false)
    @JoinColumn(name = "truck")
    private TruckEntity truck;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name = "shifts", joinColumns = {@JoinColumn(name = "trip")},
            inverseJoinColumns = {@JoinColumn(name = "driver")})
    private List<DriverEntity> drivers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "trip", nullable = false)
    private Set<StopoverEntity> stopovers = new TreeSet<>();

    @Column(name = "step")
    private int currentStep;

    private int avgLoad;

    public TripTicketStatus getStatus() {
        return status;
    }

    public void setStatus(TripTicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDate) {
        this.departureDateTime = departureDate;
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDateTime;
    }

    public void setArrivalDateTime(LocalDateTime arrivalDate) {
        this.arrivalDateTime = arrivalDate;
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

    public Set<StopoverEntity> getStopovers() {
        return stopovers;
    }

    public void setStopovers(Set<StopoverEntity> stopovers) {
        this.stopovers = stopovers;
    }

    public StopoverEntity getStopoverWithSequenceNo(int sequenceNo) {
        return stopovers.stream()
                .filter(s -> s.getSequenceNo() == sequenceNo)
                .findFirst().orElse(null);
    }

    public List<CityEntity> getCities() {
        return getStopovers().stream().sorted().map(StopoverEntity::getCity).collect(Collectors.toList());
    }

    @Mapping("stopoversSorted")
    public List<StopoverEntity> getStopoversSorted() {
        return getStopovers().stream().sorted().collect(Collectors.toList());
    }

    public int getAvgLoad() {
        return avgLoad;
    }

    public void setAvgLoad(int avgLoad) {
        this.avgLoad = avgLoad;
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(int currentStopoverNo) {
        this.currentStep = currentStopoverNo;
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
                ", startingDate=" + departureDateTime +
                ", truck=" + truck.getRegNumber() +
                ", drivers=" + drivers +
                ", stopovers=" + stopovers +
                '}';
    }
}
