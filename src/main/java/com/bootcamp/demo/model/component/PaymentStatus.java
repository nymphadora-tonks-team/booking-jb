package com.bootcamp.demo.model.component;

public enum PaymentStatus {
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private final String status;

    PaymentStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
