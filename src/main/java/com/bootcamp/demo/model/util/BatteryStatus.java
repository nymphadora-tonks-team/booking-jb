package com.bootcamp.demo.model.util;

public enum BatteryStatus {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIG("HIGH");

    private final String value;

    BatteryStatus(String value) {
        this.value = value;
    }

    public String getValue() { return value; }
}
