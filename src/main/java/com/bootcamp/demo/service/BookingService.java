package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.logging.Logger;

@Service
public class BookingService implements IBookingService {
    private static final Logger LOGGER = Logger.getLogger(BookingService.class.getName());
    private static final String COLLECTION_PATH = "bookings/databases/bookings";

    private final Firestore db;

    public BookingService(Firestore db) {
        this.db = db;
    }

    /**
     * Retrieves a Booking entity by its id.
     */
    @Override
    public Booking getBookingByID(final UUID bookingId) throws ExecutionException, InterruptedException, IllegalArgumentException {
        if (bookingId == null) throw new IllegalArgumentException();
        DocumentReference documentReference = db.collection(COLLECTION_PATH)
                .document(bookingId.toString());
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();
        Booking booking;
        if (document.exists()) {
            booking = document.toObject(Booking.class);
            return booking;
        }
        return null;
    }

    /**
     * Retrieves a Booking entity by its user's id.
     */
    @Override
    public LinkedHashSet<Booking> getBookings(final UUID userId) throws ExecutionException, InterruptedException, IllegalArgumentException {
        if (userId == null) throw new IllegalArgumentException();
        LinkedHashSet<Booking> userBookings;
        userBookings = db.collection(COLLECTION_PATH)
                .whereEqualTo("accountId", userId).get().get()
                .getDocuments()
                .stream().map(d -> d.toObject(Booking.class))
                .collect(Collectors.toSet())
                .stream().sorted(Comparator.comparing(Booking :: getStartDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return userBookings;
    }

    /**
     * Retrieves all entities of type Booking.
     */
    @Override
    public LinkedHashSet<Booking> getAllBookings() throws ExecutionException, InterruptedException {
        LinkedHashSet<Booking> allBookings;
        allBookings = db.collection(COLLECTION_PATH)
                .get()
                .get()
                .getDocuments()
                .stream().map(d -> d.toObject(Booking.class))
                .collect(Collectors.toSet())
                .stream().sorted(Comparator.comparing(Booking :: getStartDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return allBookings;
    }

    /**
     * Creates a new document containing the given Booking type param.
     */
    @Override
    public String createBooking(final Booking booking) throws ExecutionException, InterruptedException, IllegalArgumentException {
        if (booking == null) throw new IllegalArgumentException();
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_PATH)
                .document(booking.getId().toString())
                .set(booking);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public String deleteBooking(final UUID bookingId) throws ExecutionException, InterruptedException {
        if (bookingId == null) throw new IllegalArgumentException();
        ApiFuture<WriteResult> collectionApiFuture = db.collection(COLLECTION_PATH)
                .document(bookingId.toString())
                .delete();
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public String updateBooking(UUID bookingId, LocalDateTime endDate, PaymentStatus newStatus) throws ExecutionException, InterruptedException {
        if (bookingId == null) throw new IllegalArgumentException();
        DocumentReference docRef = db.collection(COLLECTION_PATH)
                .document(bookingId.toString());
        ApiFuture<WriteResult> collectionApiFuture = docRef
                .update("endDate", endDate,
                "payment", newStatus);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

}