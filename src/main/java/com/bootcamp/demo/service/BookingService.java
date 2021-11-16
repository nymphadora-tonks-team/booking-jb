package com.bootcamp.demo.service;

import com.bootcamp.demo.models.Booking;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class BookingService implements  IBookingService{
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());
    private static final String COLLECTION_PATH = "bookings";

    private Firestore db;
    @Override
    public String deleteBooking(Booking booking) throws ExecutionException, InterruptedException {
        if (booking == null) throw new IllegalArgumentException();
        ApiFuture<WriteResult> collectionApiFuture  = db.collection(COLLECTION_PATH)
                .document(booking.toString(booking.getId()))
                .delete();
        return collectionApiFuture.get().getUpdateTime().toString();
    }
}
