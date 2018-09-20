package com.marta.logistika.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class TripTicketRecord implements Serializable {

    private long id;
    private TruckShortRecord truck;
    private List<StopoverShortRecord> stopovers;
    private String departureDateTime;

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

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
        this.departureDateTime = departureDateTime;
    }
}
