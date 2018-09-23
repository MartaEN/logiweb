package com.marta.logistika.dto;

import com.marta.logistika.enums.TripTicketStatus;

import java.io.Serializable;
import java.util.List;

public class TripTicketRecord implements Serializable {

    private long id;
    private TruckShortRecord truck;
    private List<StopoverShortRecord> stopovers;
    private List<DriverRecord> drivers;
    private String departureDateTime;
    private TripTicketStatus status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TruckShortRecord getTruck() {
        return truck;
    }

    public void setTruck(TruckShortRecord truck) {
        this.truck = truck;
    }

    public List<StopoverShortRecord> getStopovers() {
        return stopovers;
    }

    public void setStopovers(List<StopoverShortRecord> stopovers) {
        this.stopovers = stopovers;
    }

    public List<DriverRecord> getDrivers() {
        return drivers;
    }

    public void setDrivers(List<DriverRecord> drivers) {
        this.drivers = drivers;
    }

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public TripTicketStatus getStatus() {
        return status;
    }

    public void setStatus(TripTicketStatus status) {
        this.status = status;
    }
}
