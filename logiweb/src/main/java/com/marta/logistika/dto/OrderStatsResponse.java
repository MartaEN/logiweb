package com.marta.logistika.dto;

import java.io.Serializable;
import java.util.List;

public class OrderStatsResponse implements Serializable {

    private ResponseType responseType;
    private String citiesFilter;
    private String citiesFilterParams;
    private String dateFilter;
    private List<OrderStatsLine> orderLines;
    private List<String> openDates;

    public enum ResponseType {
        SUMMARY,
        DRILLDOWN
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getCitiesFilter() {
        return citiesFilter;
    }

    public void setCitiesFilter(String citiesFilter) {
        this.citiesFilter = citiesFilter;
    }

    public String getCitiesFilterParams() {
        return citiesFilterParams;
    }

    public void setCitiesFilterParams(String citiesFilterParams) {
        this.citiesFilterParams = citiesFilterParams;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    public List<OrderStatsLine> getOrderLines() {
        return orderLines;
    }

    public void setOrderLines(List<OrderStatsLine> orderLines) {
        this.orderLines = orderLines;
    }

    public List<String> getOpenDates() {
        return openDates;
    }

    public void setOpenDates(List<String> openDates) {
        this.openDates = openDates;
    }
}
