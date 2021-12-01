package com.bootcamp.demo.service;
import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface IBookingService {
    Booking getBookingByID(final String id) throws ExecutionException, InterruptedException;
    Set<Booking> getBookings(final String userId) throws ExecutionException, InterruptedException;
    Set<Booking> getAllBookings() throws ExecutionException, InterruptedException;
    String createBooking(final Booking booking) throws ExecutionException, InterruptedException;
    String deleteBooking(final String bookingId) throws ExecutionException, InterruptedException;
    String updateBooking(final String bookingId, LocalDateTime endDate, PaymentStatus newStatus) throws ExecutionException, InterruptedException;
}
