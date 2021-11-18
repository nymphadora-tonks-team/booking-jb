package com.bootcamp.demo.model.component;

public enum BatteryStatus {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    UNKNOWN("UNKNOWN");

    private final String value;

    BatteryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
