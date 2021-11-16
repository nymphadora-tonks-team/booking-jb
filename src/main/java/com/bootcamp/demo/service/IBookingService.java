package com.bootcamp.demo.service;

import com.bootcamp.demo.models.Booking;

import java.util.concurrent.ExecutionException;

public interface IBookingService {
    String deleteBooking(final Booking booking) throws ExecutionException, InterruptedException;
}
