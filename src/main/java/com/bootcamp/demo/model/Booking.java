package com.bootcamp.demo.model;

import java.util.Objects;
import java.util.UUID;

public final class Booking {
    private String id;
    private String serialNumber;
    private long accountId;
    private String startDate;
    private String endDate;
    private PaymentStatus payment;

    public Booking() {
    }

    public Booking(String serialNumber, long accountId, String startDate, String endDate, PaymentStatus paymentStatus) {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.serialNumber = serialNumber;
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = paymentStatus;
    }

    public String getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public PaymentStatus getPayment() {
        return payment;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPayment(PaymentStatus payment) {
        this.payment = payment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id)
                && Objects.equals(serialNumber, booking.serialNumber)
                && Objects.equals(accountId, booking.accountId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, accountId);
    }

    @Override
    public String toString() {
        return "Booking: " + "Id = " + id + ", " +
                "Serial Number = " + serialNumber + ", " +
                "Account id = " + accountId + ", " +
                "Start date = " + startDate + ", " +
                "End date = " + endDate + ", " +
                "Payment = " + payment + ", ";
    }
}
