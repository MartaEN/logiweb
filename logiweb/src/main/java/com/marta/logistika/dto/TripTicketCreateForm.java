package com.marta.logistika.dto;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

public class TripTicketCreateForm {

    @Future
    private LocalDateTime departureDateTime;
    private Long toCity;
    private String truckRegNumber;

    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }

    public Long getToCity() {
        return toCity;
    }

    public void setToCity(Long toCity) {
        this.toCity = toCity;
    }

    public String getTruckRegNumber() {
        return truckRegNumber;
    }

    public void setTruckRegNumber(String truckRegNumber) {
        this.truckRegNumber = truckRegNumber;
    }
}
