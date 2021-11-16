package com.bootcamp.demo;

import com.bootcamp.demo.models.Booking;
import com.bootcamp.demo.service.BookingService;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.auth.oauth2.GoogleCredentials.fromStream;
import static com.google.firebase.FirebaseApp.initializeApp;
import static com.google.firebase.cloud.FirestoreClient.getFirestore;
import static java.lang.System.getProperty;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Sample RestController
 * Demo - Used for testing purposes
 */
@ConditionalOnProperty("firebaseKey")
@RestController
@RequestMapping(path = "/firebase", produces = APPLICATION_JSON_VALUE)
public class FirebaseController {
    private Firestore firestoreDB;
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    private static final Logger LOGGER = Logger.getLogger(FirebaseController.class.getName());
    private final BookingService bookingService = new BookingService();

    @PostConstruct
    private void initFirestore() throws IOException {
        final var serviceAccount = new ByteArrayInputStream(getProperty("firebaseKey").getBytes(UTF_8));

        final var options = FirebaseOptions.builder()
                .setCredentials(fromStream(serviceAccount))
                .build();
        initializeApp(options);

        firestoreDB = getFirestore();
    }

    /**
     * Returns the path for all collections stored in Firestore
     */
    @GetMapping("/getAllPaths")
    public Set<String> getAllPaths() {
        return StreamSupport.stream(firestoreDB.listCollections().spliterator(), false)
                .map(CollectionReference::getPath)
                .collect(Collectors.toUnmodifiableSet());

    }
    public ResponseEntity<Object> createBooking(final Booking booking){
        try {
            LOGGER.info("Booking deleted successfully. Update time: " + bookingService.deleteBooking(booking));
            return SUCCESS_RESPONSE;
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return FAILURE_RESPONSE;
        }
    }
    /*
    @Override
    public String endBooking(final Booking booking) throws ExecutionException, InterruptedException,  IllegalArgumentException  {
        if (booking == null) throw new IllegalArgumentException();
        ApiFuture<WriteResult> collectionApiFuture  = db.collection(COLLECTION_PATH)
                .document(booking.getId().toString())
                .delete();
        return collectionApiFuture.get().getUpdateTime().toString();
    }
     */
}
