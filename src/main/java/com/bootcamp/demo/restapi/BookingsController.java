package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.BookingScooter;
import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.PaymentStatus;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedHashSet;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping(path = "/api/bookings")
public class BookingsController {
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    private static final Logger LOGGER = LoggerFactory.getLogger(BookingsController.class);
    private final BookingService bookingService;

    public BookingsController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/getBookingsByUserId/{userId}")
    public ResponseEntity<LinkedHashSet<Booking>> getBookingsByUserId(@PathVariable(value = "userId") String userId) {
        try {
            return new ResponseEntity<>(bookingService.getBookings(userId), HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to find bookings by user id = " + userId, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable(value = "id") final String id) {
        try {
            return new ResponseEntity<>(bookingService.getBookingByID(id), HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to get bookings by id = " + id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/showAllBookings")
    public ResponseEntity<LinkedHashSet<Booking>> getAllBookings() {
        try {
            return new ResponseEntity<>(bookingService.getAllBookings(), HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to find all bookings. {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/bookScooter")
    public ResponseEntity<Booking> bookScooter(@RequestBody final BookingScooter bookingScooter) throws ExecutionException, InterruptedException {
        try {
            return new ResponseEntity<>(bookingService.bookAScooter(bookingScooter), HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBooking(@RequestParam final String id) {
        try {
            bookingService.deleteBooking(id);
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Failed to delete booking with id = " + id, e.getMessage());
            return FAILURE_RESPONSE;
        }
    }

    @PutMapping("/endOfBooking")
    public ResponseEntity<String> updateBooking(@RequestBody Location newLocation, @RequestParam final String id,
                                                @RequestParam String endDate){
        try {
            Booking booking = bookingService.getBookingByID(id);
            if(booking != null) {
                if (bookingService.endBookingAndUpdate(booking, endDate, newLocation)) {
                    return new ResponseEntity<>("Payment success", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Payment failed", HttpStatus.BAD_REQUEST);
                }
            }
            return new ResponseEntity<>("The booking doesn't exist", HttpStatus.BAD_REQUEST);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("Unfortunately,an error happened when trying to update the booking. {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}