package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.restapi.ScooterController;
import com.bootcamp.demo.service.assembler.ScooterAssembler;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScooterServiceImpl implements ScooterService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterController.class);

    private final Firestore db;
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private static final ResponseEntity<Object> SUCCESS_RESPONSE = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<Object> FAILURE_RESPONSE = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

    private ScooterServiceImpl(Firestore db) {
        this.db = db;
    }

    @Override
    public ResponseEntity<Scooter> findScooterById(String id) {
        LOGGER.info("FIND SCOOTER BY ID - service function invoked");
        return null;
    }

    @Override
    public Set<Scooter> findAllScooters() {
        LOGGER.info("FIND ALL SCOOTER - service function invoked");

        try {
            ApiFuture<QuerySnapshot> querySnapshotApiFuture  = db.collection(COLLECTION_SCOOTERS_PATH).get();

            Iterable<QueryDocumentSnapshot> scootersDocuments = querySnapshotApiFuture.get().getDocuments();

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
    public void createScooter(final Scooter scooter) {
        LOGGER.info("CREATE SCOOTER - service function invoked");

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

    private boolean isNearby(Location selectedLocation, Location scooterLocation) {
        if (scooterLocation.getLongitude() <= selectedLocation.getLongitude() + 0.01
            && (scooterLocation.getLongitude() > selectedLocation.getLongitude() - 0.01 )) {
            if ((selectedLocation.getLatitude() + 0.01 >= scooterLocation.getLatitude())
                && (selectedLocation.getLatitude() - 0.01 < scooterLocation.getLatitude())) {
                return true;
            }
        }
        LOGGER.info("The scooter is not nearby");
        return false;
    }

    @Override
    public Set<Scooter> getAvailableScooters(Location selectedLocation) {
        LOGGER.info("GET AVAILABLE SCOOTERS - service function invoked");
        Set<Scooter> allScooters = findAllScooters();

        Set<Scooter> availableScooters = allScooters.stream()
                .filter(scooter -> (scooter.getStatus() == ScooterStatus.AVAILABLE) &&
                        isNearby(selectedLocation, scooter.getCurrentLocation()))
                .collect(Collectors.toSet());

        LOGGER.info("Found scooters successfully");
        return availableScooters;
    }
}
