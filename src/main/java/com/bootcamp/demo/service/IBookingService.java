package com.bootcamp.demo.service;
import com.bootcamp.demo.model.Booking;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface IBookingService {
    Booking getBookingByID(final UUID id) throws ExecutionException, InterruptedException;
    Set<Booking> getBookings(final UUID userId) throws ExecutionException, InterruptedException;
    Set<Booking> getAllBookings() throws ExecutionException, InterruptedException;
    String createBooking(final Booking booking) throws ExecutionException, InterruptedException;
    String deleteBooking(final UUID bookingId) throws ExecutionException, InterruptedException;
}
