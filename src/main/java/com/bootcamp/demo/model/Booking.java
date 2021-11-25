package com.bootcamp.demo.model;

import nonapi.io.github.classgraph.json.Id;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;


public final class Booking {
    @Id
    private UUID id;
    private UUID serialNumber;
    private UUID accountId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private PaymentStatus payment;

    public Booking() {}

    public Booking(UUID id,UUID serialNumber, UUID accountId,LocalDateTime startDate,LocalDateTime endDate,PaymentStatus payment) {
        this.id = id;
        this.serialNumber = serialNumber;
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = payment;
    }

    public UUID getId() {
        return id;
    }

    public UUID getSerialNumber() {
        return serialNumber;
    }

    public UUID getAccountId() {
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

    public void setId(UUID id) {
        this.id = id;
    }

    public void setSerialNumber(UUID serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setAccountId(UUID accountId) {
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

    public double getTotalCost( long minutesSpentOnScooter)
    {
        double startCost = 0.60;
        double pricePerMin = 1.20;
        return startCost + (pricePerMin * minutesSpentOnScooter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, accountId);
    }
}
