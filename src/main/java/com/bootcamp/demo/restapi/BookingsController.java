package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.BookingService;
import com.bootcamp.demo.service.ScooterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/bookings")
public class BookingsController {
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);
    private final BookingService bookingService;
    private final ScooterService scooterService;

    public BookingsController(BookingService bookingService, ScooterService scooterService) {
        this.bookingService = bookingService;
        this.scooterService = scooterService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestBody final Booking booking) {
        try {
            bookingService.createBooking(booking);
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to create booking with id = {}.", booking.getId(), e.getMessage());
            return FAILURE_RESPONSE;
        }
    }

    @PostMapping("/bookScooter")
    @ResponseBody
    public void bookScooter(final Double lat, final Double longitude) throws ExecutionException, InterruptedException {
        bookAScooter(lat, longitude);

    }

    @GetMapping("/getBookingsByUserId/{userId}")
    public ResponseEntity<LinkedHashSet<Booking>> getBookingsByUserId(@PathVariable(value = "userId") long userId) {
        LinkedHashSet<Booking> bookingsByUserId = new LinkedHashSet<>();
        try {
            bookingsByUserId = bookingService.getBookings(userId);
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to find bookings by user id = {}.", userId, e.getMessage());
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking/{id}")
    public ResponseEntity<Object> getBooking(@PathVariable(value = "id") final String id) {
        Booking booking = new Booking();
        try {
            booking = bookingService.getBookingByID(id);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to get bookings by id = {}.", id, e.getMessage());
            return new ResponseEntity<>(booking, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/showAllBookings")
    public ResponseEntity<LinkedHashSet<Booking>> getAllBookings() {
        LinkedHashSet<Booking> bookings = new LinkedHashSet<>();
        try {
            bookings = bookingService.getAllBookings();
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to find all bookings.", e.getMessage());
            return new ResponseEntity<>(bookings, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteBooking(@RequestParam final String id) {
        try {
            bookingService.deleteBooking(id);
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to delete booking with id = {}.", id, e.getMessage());
            return FAILURE_RESPONSE;
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateBooking(@RequestParam final String id, @RequestParam("start") String endDate, PaymentStatus status) {
        try {
            bookingService.updateBooking(id, endDate, status);
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to update booking with id = {}.", id, e.getMessage());
            return FAILURE_RESPONSE;
        }
    }

    public Scooter bookAScooter(final Double lat, final Double longitude) throws ExecutionException, InterruptedException {
        Random random = new Random();
        Location userLocation = new Location(lat, longitude);
        Set<Scooter> allAvailableScooters = scooterService.getAvailableScooters(userLocation, 10.0);
        int randomIndexScooter = random.nextInt(allAvailableScooters.size());
        Scooter[] listOfScooters = allAvailableScooters.toArray(new Scooter[allAvailableScooters.size()]);
        Scooter pickedScooter = listOfScooters[randomIndexScooter];
        pickedScooter.setStatus(ScooterStatus.RESERVED);
        scooterService.updateScooter(pickedScooter.getSerialNumber(), pickedScooter.getCurrentLocation(), pickedScooter.getStatus(), pickedScooter.getBattery().getLevel());
        LOGGER.info("scooter:" + pickedScooter.toString());
        LinkedHashSet<Booking> bookings = bookingService.getAllBookings();
        int randomIndexBooking = random.nextInt(bookings.size());
        Booking[] listOfBookings = bookings.toArray(new Booking[bookings.size()]);
        Booking pickedBooking = listOfBookings[randomIndexBooking];
        pickedBooking.setSerialNumber(pickedScooter.getSerialNumber());
        String startDateBooking = pickedBooking.getStartDate();
        pickedBooking.setStartDate(startDateBooking);
        LOGGER.info("booking:" + pickedBooking.toString());
        bookingService.createBooking(pickedBooking);
        return pickedScooter;
    }
}