package com.bootcamp.demo.service;

import com.bootcamp.demo.models.Booking;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class BookingService implements IBookingService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());
    private static final String COLLECTION_PATH = "bookings";

    private Firestore db;

    public void setDb(Firestore db) {
        this.db = db;
    }

    /**
     * Retrieves a Booking entity by its id.
     */
    @Override
    public Booking getBookingByID(final UUID bookingId) throws ExecutionException, InterruptedException, IllegalArgumentException {
        if (bookingId == null) throw new IllegalArgumentException();
        DocumentReference documentReference =  db.collection(COLLECTION_PATH)
                .document(bookingId.toString());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        Booking booking;
        if(document.exists()){
            booking = document.toObject(Booking.class);
            return booking;
        }
        return null;
    }

    /**
     * Retrieves a Booking entity by its user's id.
     */
    @Override
    public Set<Booking> getBookings(final UUID userId) throws ExecutionException, InterruptedException, IllegalArgumentException  {
        if (userId == null) throw new IllegalArgumentException();
        Set<Booking> userBookings;
        userBookings = db.collection(COLLECTION_PATH)
                .whereEqualTo("accountId", userId).get().get()
                .getDocuments()
                .stream().map(d -> d.toObject(Booking.class))
                .collect(Collectors.toSet());
        return userBookings;
    }

    /**
     * Retrieves all entities of type Booking.
     */
    @Override
    public Set<Booking> getAllBookings() throws ExecutionException, InterruptedException {
        Set<Booking> allBookings;
        allBookings = db.collection(COLLECTION_PATH)
                .get()
                .get()
                .getDocuments()
                .stream().map(d -> d.toObject(Booking.class))
                .collect(Collectors.toSet());
        return allBookings;
    }

    /**
     * Creates a new document containing the given Booking type param.
     */
    @Override
    public String createBooking(final Booking booking) throws ExecutionException, InterruptedException,  IllegalArgumentException  {
        if (booking == null) throw new IllegalArgumentException();
        ApiFuture<WriteResult> collectionApiFuture  = db.collection(COLLECTION_PATH)
                .document(booking.getId().toString())
                .set(booking);
        return collectionApiFuture.get().getUpdateTime().toString();
    }
}
