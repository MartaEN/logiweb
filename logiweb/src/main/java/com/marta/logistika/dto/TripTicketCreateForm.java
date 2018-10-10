package com.marta.logistika.dto;

public class TripTicketCreateForm {

    private String departureDateTime;
    private Long toCity;
    private String truckRegNumber;

    public String getDepartureDateTime() {
        return departureDateTime;
    }

    public void setDepartureDateTime(String departureDateTime) {
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
