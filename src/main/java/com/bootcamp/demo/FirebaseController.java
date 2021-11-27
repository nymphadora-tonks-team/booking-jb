package com.bootcamp.demo;

import com.bootcamp.demo.models.Booking;
import com.bootcamp.demo.service.BookingService;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;


import static com.google.auth.oauth2.GoogleCredentials.fromStream;
import static com.google.firebase.FirebaseApp.initializeApp;
import static com.google.firebase.cloud.FirestoreClient.getFirestore;
import static java.lang.System.getProperty;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.StreamSupport.stream;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Sample RestController
 * Demo - Used for testing purposes
 */
@ConditionalOnProperty("firebaseKey")
@RestController
@RequestMapping(path = "/firebase", produces = APPLICATION_JSON_VALUE)
public class FirebaseController {
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    private static final Logger LOGGER = Logger.getLogger(FirebaseController.class.getName());
    private Firestore firestoreDB;
    private final BookingService bookingService = new BookingService();

    @PostConstruct
    private void initFirestore() throws IOException {
        final var serviceAccount = new ByteArrayInputStream(getProperty("firebaseKey").getBytes(UTF_8));

        final var options = FirebaseOptions.builder()
                .setCredentials(fromStream(serviceAccount))
                .build();
        initializeApp(options);

        firestoreDB = getFirestore();
        bookingService.setDb(firestoreDB);
    }

    /**
     * Returns the path for all collections stored in Firestore
     */
    @GetMapping("/getAllPaths")
    public Set<String> getAllPaths() {
        return stream(firestoreDB.listCollections().spliterator(), false)
                .map(CollectionReference::getPath)
                .collect(Collectors.toUnmodifiableSet());

    }

    @PostMapping("/createBooking")
    public ResponseEntity<Object> createBooking(final Booking booking){
        try {
            LOGGER.info("Booking created successfully. Update time: " + bookingService.createBooking(booking));
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return FAILURE_RESPONSE;
        }
    }

    @GetMapping("/getBookingsByUserId")
    @ResponseBody
    public ResponseEntity<Set<Booking>> getBookingsByUserId(final UUID userId) {
        Set<Booking> bookingsByUserId = new HashSet<>();
        try {
            bookingsByUserId = bookingService.getBookings(userId);
             return new ResponseEntity<>(bookingsByUserId, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(bookingsByUserId, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getBooking")
    @ResponseBody
    public ResponseEntity<Object> getBooking(final UUID id) {
        Booking booking = null;
        try {
            booking = bookingService.getBookingByID(id);
            return new ResponseEntity<>(booking, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(booking, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/showAllBookings")
    @ResponseBody
    public ResponseEntity<Object> getAllBookings() {
        Set<Booking> bookings = new HashSet<>();
        try {
            bookings = bookingService.getAllBookings();
            return new ResponseEntity<>(bookings, HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return new ResponseEntity<>(bookings, HttpStatus.BAD_REQUEST);
        }
    }
}
