package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.assembler.ScooterAssembler;
import com.bootcamp.demo.service.exception.ItemNotFoundException;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScooterServiceImpl implements ScooterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterServiceImpl.class);
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private final Firestore db;

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
                LOGGER.error(String.format("Scooter with id = %s does not exists!", scooterId));
                throw new IllegalArgumentException();
            }

            return scooter.toObject(Scooter.class);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }
    }

    @Override
    public Set<Scooter> findAllScooters() {
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

            LOGGER.info("SCOOTER created successfully. Update_time: " + update_time);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error(e.getMessage());
            throw new ServiceException();
        }
    }

    @Override
    public String updateScooter(final String scooterId, Location location, ScooterStatus newStatus, Double newBatteryLevel) throws ExecutionException, InterruptedException {
        LOGGER.info("UPDATE SCOOTER - service function invoked:");
        DocumentReference docRef = db.collection(COLLECTION_SCOOTERS_PATH)
                .document(scooterId);
        Scooter newScooter = new Scooter(scooterId, location, newBatteryLevel, newStatus);
        ApiFuture<WriteResult> collectionApiFuture = docRef
                .set(newScooter);
        return collectionApiFuture.get().getUpdateTime().toString();
    }

    @Override
    public void deleteScooterById(String scooterId) {
        LOGGER.info("DELETE SCOOTER - service function invoked. scooterId = {}", scooterId);
        try {
            DocumentSnapshot scooter = db.collection(COLLECTION_SCOOTERS_PATH).document(scooterId).get().get();

            if (!scooter.exists()) {
                LOGGER.error("DELETE SCOOTER - Scooter does not exist. scooterId = {}", scooterId);
                throw new ItemNotFoundException(String.format("Scooter does not exist. scooterId = %s", scooterId));
            }

            ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_SCOOTERS_PATH).document(scooterId).delete();

            LOGGER.info("DELETE SCOOTER - service function. Update time : {} | scooterId = {}", writeResult.get().getUpdateTime(), scooterId);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("DELETE SCOOTER - service function. scooterId = {}", scooterId);
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
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