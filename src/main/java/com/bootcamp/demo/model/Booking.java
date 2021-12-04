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

    public Booking( String serialNumber, long accountId, String startDate, String endDate, PaymentStatus payment) {
        this.id =  UUID.randomUUID().toString().replace("-", "");
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


    public double getTotalCost(long minutesSpentOnScooter) {
        double startCost = 0.60;
        double pricePerMin = 1.20;
        return startCost + (pricePerMin * minutesSpentOnScooter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, accountId);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Booking{");
        sb.append("id='").append(id).append(", ");
        sb.append("serialNumber='").append(serialNumber).append(", ");
        sb.append("accountId='").append(accountId).append(", ");
        sb.append("startDate=").append(startDate).append(", ");
        sb.append("endDate=").append(endDate).append(", ");
        sb.append("payment=").append(payment).append('}');
        return sb.toString();
    }
}
