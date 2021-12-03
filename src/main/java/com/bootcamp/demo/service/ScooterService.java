package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScooterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterService.class);
    private static final String COLLECTION_SCOOTERS_PATH = "bookings/databases/scooters";
    private final Firestore db;

    private ScooterService(Firestore db) {
        this.db = db;
    }

    public Scooter findScooterById(String scooterId) {
        LOGGER.info("FIND SCOOTER BY ID - service function invoked: scooterId = {}", scooterId);

        try {
            DocumentSnapshot scooter = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooterId)
                    .get()
                    .get();

            if (!scooter.exists()) {
                LOGGER.error("FIND SCOOTER BY ID - service function: Scooter does not exists! scooterId = {}", scooterId);
                throw new IllegalArgumentException();
            }

            return scooter.toObject(Scooter.class);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("FIND SCOOTER BY ID - service function: scooterId = {}. Error message: {}", scooterId, e.getMessage());
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
        }
    }

    public Set<Scooter> findAllScooters() {
        LOGGER.info("FIND ALL SCOOTER - service function invoked");

        try {
            Iterable<QueryDocumentSnapshot> scooters = db.collection(COLLECTION_SCOOTERS_PATH)
                    .get()
                    .get()
                    .getDocuments();

            return StreamSupport
                    .stream(scooters.spliterator(), false)
                    .map(queryDocumentSnapshot -> queryDocumentSnapshot.toObject(Scooter.class))
                    .collect(Collectors.toSet());
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("FIND ALL SCOOTER - service function. Error message: {}", e.getMessage());
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
        }

    }

    public void createScooter(final Scooter scooter) {
        LOGGER.info("CREATE SCOOTER - service function invoked: scooter = {}", scooter);

        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooter.getSerialNumber())
                    .set(scooter);

            String update_time = collectionsApiFuture.get().getUpdateTime().toDate().toString();

            LOGGER.info("CREATE SCOOTER - created successfully. scooter = {}. Update_time: {}", scooter, update_time);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("CREATE SCOOTER - service function: scooter = {}. Error message: {}", scooter, e.getMessage());
            LOGGER.error(ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
        }
    }
}
