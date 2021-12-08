package com.bootcamp.demo.service;

import com.bootcamp.demo.model.BookingScooter;
import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.PaymentStatus;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class BookingService implements IBookingService {
    private static final String COLLECTION_PATH = "bookings/databases/bookings";
    private final Firestore db;
    private final ScooterService scooterService;
    private static final Random RANDOM = new Random();

    public BookingService(Firestore db, ScooterService scooterService) {
        this.db = db;
        this.scooterService = scooterService;
    }

    /**
     * Retrieves a Booking entity by its id.
     */
    @Override
    public Booking getBookingByID(final String bookingId) throws ExecutionException, InterruptedException, IllegalArgumentException {
        if (bookingId == null) {
            throw new IllegalArgumentException("Parameter 'bookingId' cannot be null");
        }
        DocumentReference documentReference = db.collection(COLLECTION_PATH)
                .document(bookingId);
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
    public LinkedHashSet<Booking> getBookings(final String userId) throws ExecutionException, InterruptedException, IllegalArgumentException {

        return db.collection(COLLECTION_PATH)
                .whereEqualTo("accountId", userId).get().get()
                .getDocuments()
                .stream().map(d -> d.toObject(Booking.class))
                .collect(Collectors.toSet())
                .stream().sorted(Comparator.comparing(Booking::getStartDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Retrieves all entities of type Booking.
     */
    @Override
    public LinkedHashSet<Booking> getAllBookings() throws ExecutionException, InterruptedException {
        return db.collection(COLLECTION_PATH)
                .get()
                .get()
                .getDocuments()
                .stream().map(d -> d.toObject(Booking.class))
                .collect(Collectors.toSet())
                .stream().sorted(Comparator.comparing(Booking::getStartDate))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * Creates a new document containing the given Booking type param.
     */
    @Override
    public String createBooking(final Booking booking) throws ExecutionException, InterruptedException, IllegalArgumentException {
        if (booking == null){
            throw new IllegalArgumentException("Parameter 'booking' cannot be null");
        }
        return db.collection(COLLECTION_PATH)
                .document(booking.getId())
                .set(booking)
                .get()
                .getUpdateTime()
                .toString();
    }

    @Override
    public String deleteBooking(final String bookingId) throws ExecutionException, InterruptedException {
        if (bookingId == null){
            throw new IllegalArgumentException("Parameter 'bookingId' cannot be null");
        }
        return db.collection(COLLECTION_PATH)
                .document(bookingId)
                .delete()
                .get()
                .getUpdateTime()
                .toString();
    }

    @Override
    public String updateBooking(String bookingId, String endDate, PaymentStatus newStatus) throws ExecutionException, InterruptedException {
        if (bookingId == null){
            throw new IllegalArgumentException("Parameter 'bookingId' cannot be null");
        }
        return db.collection(COLLECTION_PATH)
                .document(bookingId)
                .update("endDate", endDate, "paymentStatus", newStatus)
                .get()
                .getUpdateTime()
                .toString();
    }

    public Booking bookAScooter(BookingScooter bookingScooter) throws ExecutionException, InterruptedException {
        List<Scooter> allAvailableScooters = scooterService.getAvailableScooters(bookingScooter.getLocation(), bookingScooter.getSearchRadius());
        return !allAvailableScooters.isEmpty()
                ? bookRandomScooter(allAvailableScooters, bookingScooter)
                : null;
    }

    private Booking bookRandomScooter(List<Scooter> allAvailableScooters, BookingScooter bookScooter) throws ExecutionException, InterruptedException {
        final Scooter pickedScooter = reserveScooter(allAvailableScooters);
        return createBookingScooter(pickedScooter, bookScooter);
    }

    private Booking createBookingScooter(Scooter scooter, BookingScooter bookScooter) throws ExecutionException, InterruptedException {
        Booking booking = new Booking(scooter.getSerialNumber(), bookScooter.getAccountId(), LocalDateTime.now().toString(), null, null);
        createBooking(booking);
        return booking;
    }

    private Scooter reserveScooter(final List<Scooter> allAvailableScooters) throws ExecutionException, InterruptedException {
        final Scooter pickedScooter = allAvailableScooters.get(RANDOM.nextInt(allAvailableScooters.size()));
        pickedScooter.setStatus(ScooterStatus.RESERVED);
        scooterService.updateScooter(pickedScooter.getSerialNumber(), pickedScooter.getCurrentLocation(), pickedScooter.getStatus(), pickedScooter.getBattery().getLevel());
        return pickedScooter;
    }

    public Double endBookingAndUpdate(Booking booking, String endDate) throws ExecutionException, InterruptedException {
        long bookingDuration = 0;
        Scooter scooter = scooterService.findScooterById(booking.getSerialNumber());
        String startDate = booking.getStartDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate, formatter);
        if (dateTimeEnd.isAfter(dateTimeStart)) {
            bookingDuration = dateTimeEnd.getMinute() - dateTimeStart.getMinute();
        }
        final double totalCostComputed = booking.getTotalCost(bookingDuration);
        updateBooking(booking.getId(), endDate, booking.getPaymentStatus());
        scooter.setCurrentLocation(new Location(scooter.getCurrentLocation().getLongitude(), scooter.getCurrentLocation().getLongitude()));
        scooterService.updateScooter(scooter.getSerialNumber(), scooter.getCurrentLocation(), ScooterStatus.AVAILABLE, scooter.getBattery().getLevel());

        return totalCostComputed;
    }
}