package com.bootcamp.demo.service;
import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface IBookingService {
    Booking getBookingByID(final String id) throws ExecutionException, InterruptedException;
    Set<Booking> getBookings(final long userId) throws ExecutionException, InterruptedException;
    Set<Booking> getAllBookings() throws ExecutionException, InterruptedException;
    String createBooking(final Booking booking) throws ExecutionException, InterruptedException;
    String deleteBooking(final String bookingId) throws ExecutionException, InterruptedException;
    String updateBooking(final String bookingId, String endDate, PaymentStatus newStatus) throws ExecutionException, InterruptedException;
    String updateBookingForScooterBooking(final String bookingId, String serialNr) throws ExecutionException, InterruptedException;
}
