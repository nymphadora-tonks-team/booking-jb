package com.bootcamp.demo.models;
import java.util.Date;
import java.util.Objects;


public class Booking {
    private long id;
    private long serialNumber;
    private long accountId;
    private Date startDate;
    private Date endDate;
    private PaymentStatus payment;
    private final double pricePerMin=1.20;
    private final double startCost=0.60;
    public double getTotalCost( long minutesSpentOnScooter)
    {
        return startCost + (pricePerMin*minutesSpentOnScooter);
    }

    public String toString(long id ) {
        return "Booking{" +
                "id=" + id +
                '}';
    }

    public Booking(){}
    public Booking(long id,long serialNumber, long accountId,Date startDate,Date endDate,PaymentStatus payment){
        this.id = id;
        this.serialNumber = serialNumber;
        this.accountId = accountId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.payment = payment;
    }

    //Getters
    public long getId() {
        return id;
    }

    public long getSerialNumber() {
        return serialNumber;
    }

    public long getAccountId() {
        return accountId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public PaymentStatus getPayment() {
        return payment;
    }

    //Setters

    public void setId(long id) {
        this.id = id;
    }

    public void setSerialNumber(long serialNumber) {
        this.serialNumber = serialNumber;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
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
        return id == booking.id && serialNumber == booking.serialNumber && accountId == booking.accountId && startDate.equals(booking.startDate) && endDate.equals(booking.endDate) && payment == booking.payment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serialNumber, accountId, startDate, endDate, payment);
    }
}
