package com.marta.logistika.dto;

import java.util.List;
import java.util.Map;

public class RouteJsonResponse {

    private List<RoadRecord> route;
    private Map<String, String> errorMessages;

    public List<RoadRecord> getRoute() {
        return route;
    }

    public void setRoute(List<RoadRecord> route) {
        this.route = route;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }


}
