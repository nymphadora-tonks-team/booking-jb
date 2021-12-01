package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.BookingService;
import com.bootcamp.demo.service.ScooterService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final ScooterService scooterService;

    public BookingsController(BookingService bookingService, ScooterService scooterService) {
        this.bookingService = bookingService;
        this.scooterService = scooterService;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<Object> createBooking(@RequestBody final Booking booking) {
        try {
            LOGGER.info("Booking created successfully. Update time: " + bookingService.createBooking(booking));
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
//            e.printStackTrace();
            LOGGER.info("Unfortunately, booking could not be  created .");
            return FAILURE_RESPONSE;
        }
    }

    @GetMapping("/getBookingsByUserId/{userId}")
    @ResponseBody
    public ResponseEntity<LinkedHashSet<Booking>> getBookingsByUserId(@PathVariable(value = "userId") String userId) {
        LinkedHashSet<Booking> bookingsByUserId = new LinkedHashSet<>();
        try {
            bookingsByUserId = bookingService.getBookings(userId);
            LOGGER.info("GET BOOKINGS BY userID - API endpoint invoked");
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
//            e.printStackTrace();
            LOGGER.info("Unfortunately,an error happened while trying to retrive a booking based on the user id.");
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking/{id}")
    @ResponseBody
    public ResponseEntity<Object> getBooking(@PathVariable(value = "id") final String id) {
        Booking booking = null;
        try {
            booking = bookingService.getBookingByID(id);
            LOGGER.info("GET BOOKING BY ID - API endpoint invoked");
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
//            e.printStackTrace();
            LOGGER.info("Unfortunately,an error happened while trying to get a booking.");

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
            // e.printStackTrace();
            LOGGER.info("Unfortunately,an error happened while trying to get all the bookings.");

            return new ResponseEntity<>(bookings, HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBooking(@RequestParam final String id) {
        try {
            bookingService.deleteBooking(id);
            LOGGER.info("DELETE BOOKING - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            // e.printStackTrace();
            LOGGER.info("Unfortunately,an error happened when trying to delete a booking.");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateBooking(@RequestParam final String id, @RequestParam("start")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, PaymentStatus status) {
        try {
            bookingService.updateBooking(id, endDate, status);
            Booking booking = bookingService.getBookingByID(id);
            Scooter scooter = scooterService.findScooterById(booking.getSerialNumber());

            if (status == PaymentStatus.SUCCESS) {
                endBookingAndUpdate(booking, scooter);
            }
            LOGGER.info("UPDATE BOOKING - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            // e.printStackTrace();
            LOGGER.info("Unfortunately,an error happened when trying to update the booking.");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public double endBookingAndUpdate(Booking booking, Scooter scooter) throws ExecutionException, InterruptedException {
        long bookingDuration = 0;

        String startDate = booking.getStartDate();
        String endDate = booking.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate, formatter);
        if (dateTimeEnd.isAfter(dateTimeStart)) {
            bookingDuration = dateTimeEnd.getMinute() - dateTimeStart.getMinute();
        }
        final double totalCostComputed = booking.getTotalCost(bookingDuration);
        bookingService.updateBooking(booking.getId(), dateTimeEnd, booking.getPayment());
        scooter.setCurrentLocation(new Location(scooter.getCurrentLocation().getLongitude(), scooter.getCurrentLocation().getLongitude()));
        scooterService.updateScooter(scooter.getSerialNumber(), scooter.getCurrentLocation(), ScooterStatus.AVAILABLE, scooter.getBattery().getLevel());

        return totalCostComputed;
    }

}