package com.bootcamp.demo.model.util;

public enum ScooterStatus {
    AVAILABLE("AVAILABLE"),
    RESERVED("RESERVED");

    private final String value;

    ScooterStatus(String value) { this.value = value; }

    public String getValue() { return value;}
}
