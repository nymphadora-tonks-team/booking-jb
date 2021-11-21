package com.bootcamp.demo.service;

import com.bootcamp.demo.FirebaseController;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.service.assembler.ScooterAssembler;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private final Firestore db;
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    private ScooterServiceImpl(Firestore db) {
        this.db = db;
    }

    @Override
    public Scooter findScooterById(String id) {
        LOGGER.info("FIND SCOOTER BY ID - service function invoked");
        return null;
    }

    @Override
    public List<Scooter> findAllScooter() {
        LOGGER.info("FIND ALL SCOOTER - service function invoked");

        try {
            Firestore db = FirestoreClient.getFirestore();

            ApiFuture<QuerySnapshot> future = db.collection("bookings").get();
            Iterable<QueryDocumentSnapshot> scootersDocuments = future.get().getDocuments();

            LOGGER.info("Found scooters successfully");

            return StreamSupport
                    .stream(scootersDocuments.spliterator(), false)
                    .map(ScooterAssembler::documentToModel)
                    .collect(Collectors.toSet());
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }

    }

    @Override
    public void createScooter(Scooter scooter) {
        LOGGER.info("CREATE SCOOTER - service function invoked");
        try {
            Firestore db = FirestoreClient.getFirestore();

            // Add a new scooter (asynchronously) in collection "scooters"
            ApiFuture<WriteResult> collectionsApiFuture =
                    db.collection("bookings").document(scooter.getSerialNumber()).set(scooter);

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
