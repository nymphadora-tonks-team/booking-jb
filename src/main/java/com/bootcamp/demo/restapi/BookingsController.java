package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;
import com.bootcamp.demo.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/bookings")
public class BookingsController {
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class.getName());
    private final BookingService bookingService;

    public BookingsController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestBody final Booking booking) {
        try {
            LOGGER.info("Booking created successfully. Update time: " + bookingService.createBooking(booking));
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return FAILURE_RESPONSE;
        }
    }

    @GetMapping("/getBookingsByUserId/{userId}")
    public ResponseEntity<LinkedHashSet<Booking>> getBookingsByUserId(@PathVariable(value = "userId") String userId) {
        LinkedHashSet<Booking> bookingsByUserId = new LinkedHashSet<>();
        try {
            bookingsByUserId = bookingService.getBookings(userId);
            LOGGER.info("GET BOOKINGS BY userID - API endpoint invoked");
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking/{id}")
    public ResponseEntity<Object> getBooking(@PathVariable(value = "id") final String id) {
        Booking booking = null;
        try {
            booking = bookingService.getBookingByID(id);
            LOGGER.info("GET BOOKING BY ID - API endpoint invoked");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
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
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(bookings, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> deleteBooking(@RequestParam final String id) {
        try {
            bookingService.deleteBooking(id);
            LOGGER.info("DELETE BOOKING - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateBooking(@RequestParam final String id, @RequestParam("start") String endDate, PaymentStatus status) {
        try {
            bookingService.updateBooking(id, endDate, status);
            LOGGER.info("UPDATE BOOKING - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}