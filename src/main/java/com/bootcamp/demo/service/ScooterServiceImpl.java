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

    /**
     * This method has the role of comparing latitude / longitude according to the given radius
     * @param selectedCoord is selected location latitude or selected location longitude
     * @param scooterCoord is scooter location latitude or scooter location longitude
     * @param convertedRadius searchRadius converted from m to lat / lon
     * @return true if scooter is in selected search radius, false if not
     */
    private boolean isWithinRadius(Double selectedCoord, Double scooterCoord, Double convertedRadius){
        return (scooterCoord <= selectedCoord + convertedRadius)
                && (scooterCoord > selectedCoord - convertedRadius);
    }

    /**
     * This method is intended to determine if a scooter is nearby by comparing the location
     * of the scooter with the location and search radius selected by the user.
     * Considering that 1 m = 0.00000636 lat/lon, we will convert searchRadius from m to lat / lon
     * @param selectedLocation location selected by user
     * @param scooterLocation the location of the scooter
     * @param searchRadius search radius selected by user
     * @return true if scooter is nearby, false if not
     */
    private boolean isNearby(Location selectedLocation, Location scooterLocation, Double searchRadius) {
        Double convertedRadius = searchRadius * 0.00000636;
        return isWithinRadius(selectedLocation.getLongitude(), scooterLocation.getLongitude(), convertedRadius)
                && isWithinRadius(selectedLocation.getLatitude(), scooterLocation.getLatitude(), convertedRadius);
    }

    /**
     * This method has the role of finding available scooters that are nearby
     * @param selectedLocation location selected by user
     * @param searchRadius search radius selected by user
     * @return list with available scooters
     */
    @Override
    public Set<Scooter> getAvailableScooters(Location selectedLocation, Double searchRadius) {
        Set<Scooter> availableScooters = findAllScooters().stream()
                .filter(scooter -> (scooter.getStatus() == ScooterStatus.AVAILABLE) &&
                        isNearby(selectedLocation, scooter.getCurrentLocation(), searchRadius))
                .collect(Collectors.toSet());

        LOGGER.info("Found scooters successfully");
        return availableScooters;
    }
}
