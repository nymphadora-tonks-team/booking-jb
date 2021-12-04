package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Booking;
import com.bootcamp.demo.model.PaymentStatus;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.User;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.BookingService;
import com.bootcamp.demo.service.ScooterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
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
            LOGGER.info("Unfortunately, booking could not be  created .");
            return FAILURE_RESPONSE;
        }
    }

    @GetMapping("/getBookingsByUserId/{userId}")
    @ResponseBody
    public ResponseEntity<LinkedHashSet<Booking>> getBookingsByUserId(@PathVariable(value = "userId") long userId) {
        LinkedHashSet<Booking> bookingsByUserId = new LinkedHashSet<>();
        try {
            LOGGER.info("GET BOOKINGS BY userID - API endpoint invoked");

            bookingsByUserId = bookingService.getBookings(userId);
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.info("Unfortunately,an error happened while trying to retrive a booking based on the user id.");
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking/{id}")
    @ResponseBody
    public ResponseEntity<Object> getBooking(@PathVariable(value = "id") final String id) {
        Booking booking = new Booking();
        try {
            LOGGER.info("GET BOOKING BY ID - API endpoint invoked");

            booking = bookingService.getBookingByID(id);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
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
            LOGGER.info("Unfortunately,an error happened while trying to get all the bookings.");

            return new ResponseEntity<>(bookings, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping ("/bookScooter")
    @ResponseBody
    public void bookScooter(final Double lat, final Double longitude) throws ExecutionException, InterruptedException {
        bookAScooter(lat, longitude);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteBooking(@RequestParam final String id) {
        try {
            LOGGER.info("DELETE BOOKING - API endpoint invoked");
            bookingService.deleteBooking(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.info("Unfortunately,an error happened when trying to delete a booking.");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateBooking(@RequestParam final String id, @RequestParam("end")
            String endDate, PaymentStatus status) {
        try {
            LOGGER.info("UPDATE BOOKING - API endpoint invoked");

            Booking booking = bookingService.getBookingByID(id);
            Scooter scooter = scooterService.findScooterById(booking.getSerialNumber());

            if (status == PaymentStatus.SUCCESS) {
                endBookingAndUpdate(booking, scooter, endDate);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.info("Unfortunately,an error happened when trying to update the booking.");

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    public double endBookingAndUpdate(Booking booking, Scooter scooter, String endDate) throws ExecutionException, InterruptedException {
        long bookingDuration = 0;

        String startDate = booking.getStartDate();
        //Date endDate = booking.getEndDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTimeStart = LocalDateTime.parse(startDate, formatter);
        LocalDateTime dateTimeEnd = LocalDateTime.parse(endDate, formatter);
        if (dateTimeEnd.isAfter(dateTimeStart)) {
            bookingDuration = dateTimeEnd.getMinute() - dateTimeStart.getMinute();
        }
        final double totalCostComputed = booking.getTotalCost(bookingDuration);
        bookingService.updateBooking(booking.getId(), endDate, booking.getPayment());
        scooter.setCurrentLocation(new Location(scooter.getCurrentLocation().getLongitude(), scooter.getCurrentLocation().getLongitude()));
        scooterService.updateScooter(scooter.getSerialNumber(), scooter.getCurrentLocation(), ScooterStatus.AVAILABLE, scooter.getBattery().getLevel());

        return totalCostComputed;
    }

    public Scooter bookAScooter(final Double lat, final Double longitude) throws ExecutionException, InterruptedException {
        Random random = new Random();
        Location userLocation= new Location(lat,longitude);
        Set<Scooter> allAvailableScooters = scooterService.getAvailableScooters(userLocation,10.0);
        int randomIndexScooter = random.nextInt(allAvailableScooters.size());
        Scooter[] listOfScooters = allAvailableScooters.toArray(new Scooter[allAvailableScooters.size()]);
        Scooter pickedScooter = listOfScooters[randomIndexScooter];
        pickedScooter.setStatus(ScooterStatus.RESERVED);
        scooterService.updateScooter(pickedScooter.getSerialNumber(), pickedScooter.getCurrentLocation(), pickedScooter.getStatus(), pickedScooter.getBattery().getLevel());
         LOGGER.info("scooter:" +pickedScooter.toString());
        LinkedHashSet<Booking> bookings = bookingService.getAllBookings();
        int randomIndexBooking = random.nextInt(bookings.size());
        Booking[] listOfBookings = bookings.toArray(new Booking[bookings.size()]);
        Booking pickedBooking = listOfBookings[randomIndexBooking];
        pickedBooking.setSerialNumber(pickedScooter.getSerialNumber());
        String startDateBooking = pickedBooking.getStartDate();
        pickedBooking.setStartDate(startDateBooking);
        LOGGER.info("booking:" +pickedBooking.toString());
        bookingService.createBooking(pickedBooking);
        return pickedScooter;
    }

}