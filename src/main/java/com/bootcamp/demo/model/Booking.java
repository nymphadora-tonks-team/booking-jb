package com.bootcamp.demo.model;

import java.util.Objects;
import java.util.UUID;
import com.bootcamp.demo.model.component.PaymentStatus;

public final class Booking {
    private String id;
    private String serialNumber;
    private String accountId;
    private String startDate;
    private String endDate;
    private Double cost;
    private PaymentStatus paymentStatus;

    public Booking() {}

    public Booking(String serialNumber, String accountId, String startDate,
                   String endDate, Double cost, PaymentStatus paymentStatus) {
        this.id = UUID.randomUUID().toString();

        this.serialNumber = serialNumber;
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cost = cost;
        this.paymentStatus = paymentStatus;
    }

    public String getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public Double getCost() { return cost; }

    public void setCost(Double cost) { this.cost = cost; }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setPayment(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
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

    public double getTotalCost(long minutesSpentOnScooter) {
        double startCost = 0.60;
        double pricePerMin = 1.20;
        return startCost + (pricePerMin * minutesSpentOnScooter);
    }

    @Override
    public String toString() {
        return "Booking: " + "Id = " + id + ", " +
                "Serial Number = " + serialNumber + ", " +
                "Account id = " + accountId + ", " +
                "Start date = " + startDate + ", " +
                "End date = " + endDate + ", " +
                "Payment = " + paymentStatus + ", ";
    }
}
