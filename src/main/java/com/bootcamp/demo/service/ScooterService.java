package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class ScooterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterService.class);
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private final Firestore db;

    private ScooterService(Firestore db) {
        this.db = db;
    }

    public Scooter findScooterById(String scooterId) {
        try {
            return db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooterId)
                    .get()
                    .get().toObject(Scooter.class);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to find scooter with id = {}.", scooterId, e);
            throw new ServiceException(e);
        }
    }

    public Set<Scooter> findAllScooters() {
        try {
            return db.collection(COLLECTION_SCOOTERS_PATH)
                    .get()
                    .get()
                    .getDocuments()
                    .stream()
                    .map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(Scooter.class))
                    .collect(Collectors.toSet());
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to find all scooters.", e);
            throw new ServiceException(e);
        }

    }

    public void createScooter(final Scooter scooter) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooter.getSerialNumber())
                    .set(scooter);

            String updateTime = collectionsApiFuture.get().getUpdateTime().toDate().toString();

            LOGGER.info("Created scooter with id = {} at = {}", scooter, updateTime);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("Failed to create scooter with id = {}.", scooter, e);
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
    public Set<Scooter> getAvailableScooters(Location selectedLocation, Double searchRadius) {
        Set<Scooter> availableScooters = findAllScooters().stream()
                .filter(scooter -> (scooter.getStatus() == ScooterStatus.AVAILABLE) &&
                        isNearby(selectedLocation, scooter.getCurrentLocation(), searchRadius))
                .collect(Collectors.toSet());

        LOGGER.info("Found scooters successfully");
        return availableScooters;
    }
}