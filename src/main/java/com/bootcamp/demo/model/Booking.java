package com.bootcamp.demo.model;

import java.time.LocalDateTime;
import java.util.Objects;


public final class Booking {
    private String id;
    private String serialNumber;
    private long accountId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PaymentStatus payment;

    public Booking() {
    }

    public Booking(String id, String serialNumber, long accountId, LocalDateTime startDate, LocalDateTime endDate, PaymentStatus payment) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = payment;
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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
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

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDateTime endDate) {
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
