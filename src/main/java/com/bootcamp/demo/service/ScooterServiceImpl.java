package com.bootcamp.demo.service;

import com.bootcamp.demo.FirebaseController;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.service.assembler.ScooterAssembler;
import com.bootcamp.demo.service.exception.ItemNotFoundException;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ScooterServiceImpl implements ScooterService{
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ScooterServiceImpl.class.getName());

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterServiceImpl.class);
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private final Firestore db;
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    private ScooterServiceImpl(Firestore db) {
        this.db = db;
    }

    @Override
    public Scooter findScooterById(String scooterId) {
        LOGGER.info(String.format("FIND SCOOTER BY ID - service function invoked: scooterId = %s", scooterId));

        try {
            DocumentSnapshot scooter = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooterId)
                    .get()
                    .get();

            if (!scooter.exists()) {
                LOGGER.error(String.format("Scooter with id = %s", scooterId));
                throw new ItemNotFoundException();
            }

            return scooter.toObject(Scooter.class);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }
    }

    @Override
    public List<Scooter> findAllScooter() {
        LOGGER.info("FIND ALL SCOOTER - service function invoked");

        try {
            Iterable<QueryDocumentSnapshot> scooters = db.collection(COLLECTION_SCOOTERS_PATH)
                    .get()
                    .get()
                    .getDocuments();

            LOGGER.info("Found scooters successfully");

            return StreamSupport
                    .stream(scooters.spliterator(), false)
                    .map(ScooterAssembler::documentToModel)
                    .collect(Collectors.toSet());
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }

    }

    @Override
    public void createScooter(final Scooter scooter) {
        LOGGER.info(String.format("CREATE SCOOTER - service function invoked: scooter info = %s", scooter.toString()));

        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooter.getSerialNumber())
                    .set(scooter);

            String update_time = collectionsApiFuture.get().getUpdateTime().toDate().toString();

            LOGGER.info("SCOOTER create successfully. Update_time: " + update_time);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }
    }

    private boolean approximateLocation(Location userLocation, Location scooterLocation) {
        if (scooterLocation.getLongitude() <= userLocation.getLongitude() + 0.01
            && (scooterLocation.getLongitude() > userLocation.getLongitude() - 0.01 )) {
            if ((userLocation.getLatitude() + 0.01 >= scooterLocation.getLatitude())
                && (userLocation.getLatitude() - 0.01 < scooterLocation.getLatitude())) {
                return true;
            }
        }
        System.out.println("The scooter is not nearby");
        return false;
    }

    @Override
    public Set<Scooter> getAvailableScooters(Location userLocation) {
        LOGGER.info("GET AVAILABLE SCOOTERS - service function invoked");
        Set<Scooter> allScooters = findAllScooters();
        Set<Scooter> availableScooters = new HashSet<>();
        for (Scooter scooter : allScooters) {
            if(Objects.equals(scooter.getStatus().getValue(), "AVAILABLE")) {
                if(approximateLocation(userLocation, scooter.getCurrentLocation())){
                    availableScooters.add(scooter);
                }
            }
        }
        LOGGER.info("Found scooters successfully");
        return availableScooters;
    }
}
