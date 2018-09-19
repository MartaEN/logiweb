package com.marta.logistika.dto;

public class TripTicketCreateForm {

    private String departureDate;
    private Long toCity;
    private String truckRegNumber;

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
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
