package com.bootcamp.demo.controller;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.model.util.ScooterStatus;
import com.bootcamp.demo.models.Booking;
import com.bootcamp.demo.models.PaymentStatus;

public class Controller {
    private Booking booking;
    private Scooter scooter;

    public Controller(Booking booking, Scooter scooter) {
        this.booking = booking;
        this.scooter = scooter;
    }

    public double endOfBooking(Booking booking, Scooter scooter)
    {
        long bookingDuration=0;
        long bookingDurationInMinutes=0;
        double totalCostComputed=0.0;
        if(booking.getEndDate().after(booking.getEndDate())) {
            bookingDuration = (booking.getEndDate().getTime() - booking.getStartDate().getTime());
            bookingDurationInMinutes = bookingDuration / (60 * 1000);
        }
        totalCostComputed=booking.getTotalCost(bookingDurationInMinutes);
        if(totalCostComputed>0) {
            scooter.setStatus(ScooterStatus.AVAILABLE);
            booking.setPayment(PaymentStatus.SUCCESS);
            scooter.setCurrentLocation(new Location(scooter.getCurrentLocation().getLongitude(),scooter.getCurrentLocation().getLongitude()));
        }
        return totalCostComputed;
    }
}
