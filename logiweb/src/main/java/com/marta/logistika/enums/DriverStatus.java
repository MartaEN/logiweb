package com.marta.logistika.enums;

public enum DriverStatus {
    OFFLINE("OFFLINE"),
    DRIVING("ONLINE"),
    SECONDING("ONLINE"),
    HANDLING("ONLINE"),
    ROAD_BREAK("ONLINE"),
    STOPOVER_BREAK("ONLINE"),
    WAITING("ONLINE");

    private String briefStatValue;

    DriverStatus(String briefStatValue) {
        this.briefStatValue = briefStatValue;
    }

    public String getBriefStatValue() {
        return briefStatValue;
    }

    private void setBriefStatValue(String briefStatValue) {
        this.briefStatValue = briefStatValue;
    }
}