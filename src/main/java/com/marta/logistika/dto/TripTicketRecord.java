package com.marta.logistika.dto;

import com.marta.logistika.entity.OrderEntity;

import java.io.Serializable;
import java.util.List;

public class TripTicketRecord implements Serializable {

    private long id;
    private TruckShortRecord truck;
    private List<StopoverShortRecord> stopovers;
    private String departureDate;

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

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }
}
