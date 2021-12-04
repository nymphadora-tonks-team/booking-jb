package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
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
            DocumentSnapshot scooter = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooterId)
                    .get()
                    .get();

            if (!scooter.exists()) {
                LOGGER.info("FIND SCOOTER BY ID - service function: Scooter does not exists! scooterId = {}", scooterId);
                return null;
            }

            return scooter.toObject(Scooter.class);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("FIND SCOOTER BY ID - service function: scooterId = {}.", scooterId, e);
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
            LOGGER.error("FIND ALL SCOOTER - service function.", e);
            throw new ServiceException(e);
        }

    }

    public void createScooter(final Scooter scooter) {
        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooter.getSerialNumber())
                    .set(scooter);

            String updateTime = collectionsApiFuture.get().getUpdateTime().toDate().toString();

            LOGGER.info("CREATE SCOOTER - created successfully. scooter = {}. Update_time: {}", scooter, updateTime);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("CREATE SCOOTER - service function: scooter = {}.", scooter, e);
            throw new ServiceException(e);
        }
    }
}
