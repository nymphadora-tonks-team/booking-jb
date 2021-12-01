package com.bootcamp.demo.model.component;

public enum ScooterStatus {
    AVAILABLE("AVAILABLE"),
    RESERVED("RESERVED");

    private final String value;

    ScooterStatus(String value) {
        this.value = value;
    }

    public static boolean contains(String test) {

        for (ScooterStatus c : ScooterStatus.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }
        return false;
    }

    public String getValue() {
        return value;
    }
}
