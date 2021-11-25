package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;
import com.bootcamp.demo.service.BookingService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/bookings")
public class BookingsController {
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    private static final Logger LOGGER = Logger.getLogger(BookingsController.class.getName());
    private final BookingService bookingService;

    public BookingsController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestBody final Booking booking) {
        try {
            LOGGER.info("Booking created suc cessfully. Update time: " + bookingService.createBooking(booking));
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return FAILURE_RESPONSE;
        }
    }

    @GetMapping("/getBookingsByUserId/{userId}")
    @ResponseBody
    public ResponseEntity<LinkedHashSet<Booking>> getBookingsByUserId(@PathVariable(value = "userId") UUID userId) {
        LinkedHashSet<Booking> bookingsByUserId = new LinkedHashSet<>();
        try {
            bookingsByUserId = bookingService.getBookings(userId);
            LOGGER.info("GET BOOKINGS BY userID - API endpoint invoked");
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking/{id}")
    @ResponseBody
    public ResponseEntity<Object> getBooking(@PathVariable(value = "id") final UUID id) {
        Booking booking = null;
        try {
            booking = bookingService.getBookingByID(id);
            LOGGER.info("GET BOOKING BY ID - API endpoint invoked");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(booking, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/showAllBookings")
    @ResponseBody
    public ResponseEntity<LinkedHashSet<Booking>> getAllBookings() {
        LinkedHashSet<Booking> bookings = new LinkedHashSet<>();
        try {
            bookings = bookingService.getAllBookings();
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(bookings, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/delete")
    public ResponseEntity<Object> deleteBooking(@RequestParam final UUID id) {
        try {
            bookingService.deleteBooking(id);
            LOGGER.info("DELETE BOOKING - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateBooking(@RequestParam final UUID id, @RequestParam("start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime endDate, PaymentStatus status) {
        try {
            bookingService.updateBooking(id, endDate, status);
            LOGGER.info("UPDATE BOOKING - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}