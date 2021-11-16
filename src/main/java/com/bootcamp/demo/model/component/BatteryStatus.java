package com.bootcamp.demo.model.component;

public enum BatteryStatus {
    LOW("LOW", 0.0),
    MEDIUM("MEDIUM", 30.0),
    HIG("HIGH", 60.0);

    private final String value;
    private Double level;

    BatteryStatus(String value, Double level) {
        this.value = value;
        this.level = level;
    }

    public String getValue() {
        return this.value;
    }

    public Double getLevel() {
        return level;
    }
}
