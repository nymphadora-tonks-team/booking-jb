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
public class BookingService {
    private static final String COLLECTION_PATH = "bookings/databases/bookings";
    private final Firestore db;
    private final ScooterService scooterService;
    private final PaymentService paymentService;
    private static final Random RANDOM = new Random();

    public BookingService(Firestore db, ScooterService scooterService, PaymentService paymentService) {
        this.db = db;
        this.scooterService = scooterService;
        this.paymentService = paymentService;
    }

    /**
     * Retrieves a Booking entity by its id.
     */
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

    public String updateBooking(String bookingId, String endDate, PaymentStatus newStatus, Double cost) throws ExecutionException, InterruptedException {
        if (bookingId == null){
            throw new IllegalArgumentException("Parameter 'bookingId' cannot be null");
        }
        return db.collection(COLLECTION_PATH)
                .document(bookingId)
                .update("endDate", endDate, "paymentStatus", newStatus, "cost", cost)
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
        Booking booking = new Booking(scooter.getSerialNumber(), bookScooter.getAccountId(), LocalDateTime.now().toString(), null, 0.0, null);
        createBooking(booking);
        return booking;
    }

    private Scooter reserveScooter(final List<Scooter> allAvailableScooters) throws ExecutionException, InterruptedException {
        final Scooter pickedScooter = allAvailableScooters.get(RANDOM.nextInt(allAvailableScooters.size()));
        pickedScooter.setStatus(ScooterStatus.RESERVED);
        scooterService.updateScooter(pickedScooter.getSerialNumber(), pickedScooter.getCurrentLocation(), pickedScooter.getStatus(), pickedScooter.getBattery().getLevel());
        return pickedScooter;
    }

    public Double computeTotalCost(Booking booking, String endDate){
        long bookingDuration = 0;
        String startDate = booking.getStartDate();
        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate);
        if (dateTimeEnd.isAfter(dateTimeStart)) {
            bookingDuration = dateTimeEnd.getMinute() - dateTimeStart.getMinute();
        }
        return booking.getTotalCost(bookingDuration);
    }

    public Boolean endBookingAndUpdate(Booking booking, String endDate, Location newLocation) throws ExecutionException, InterruptedException {
        Scooter scooter = scooterService.findScooterById(booking.getSerialNumber());
        Double totalCostComputed = computeTotalCost(booking, endDate);
        PaymentStatus paymentStatus = paymentService.getPaymentStatus();
        if (paymentStatus == PaymentStatus.SUCCESS) {
            updateBooking(booking.getId(), endDate, paymentStatus, totalCostComputed);
            scooterService.updateScooter(scooter.getSerialNumber(), newLocation, ScooterStatus.AVAILABLE, scooter.getBattery().getLevel());
            return true;
        }
        return false;
    }
}