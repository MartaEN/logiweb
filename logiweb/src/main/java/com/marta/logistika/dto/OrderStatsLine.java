package com.marta.logistika.dto;

import com.marta.logistika.entity.CityEntity;

public class OrderStatsLine {

    private CityEntity fromCity;
    private CityEntity toCity;
    private long numberOfOrders;
    private long orderId;
    private String date;
    private long totalWeight;
    private long distance;

    public OrderStatsLine() {}

    public OrderStatsLine(CityEntity fromCity, CityEntity toCity, long numberOfOrders, long totalWeight) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.numberOfOrders = numberOfOrders;
        this.totalWeight = totalWeight;
    }

    public OrderStatsLine(CityEntity fromCity, CityEntity toCity, long orderId, String date, long totalWeight, long distance) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.numberOfOrders = 1;
        this.orderId = orderId;
        this.date = date;
        this.totalWeight = totalWeight;
        this.distance = distance;
    }

    public CityEntity getFromCity() {
        return fromCity;
    }

    public void setFromCity(CityEntity fromCity) {
        this.fromCity = fromCity;
    }

    public CityEntity getToCity() {
        return toCity;
    }

    public void setToCity(CityEntity toCity) {
        this.toCity = toCity;
    }

    public long getNumberOfOrders() {
        return numberOfOrders;
    }

    public void setNumberOfOrders(long numberOfOrders) {
        this.numberOfOrders = numberOfOrders;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(long totalWeight) {
        this.totalWeight = totalWeight;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
