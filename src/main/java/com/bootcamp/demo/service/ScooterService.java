package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.exception.ItemNotFoundException;
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
        LOGGER.info("FIND SCOOTER BY ID - service function invoked. scooterId = {}", scooterId);

        try {
            DocumentSnapshot scooter = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooterId)
                    .get()
                    .get();

            if (!scooter.exists()) {
                LOGGER.warn("FIND SCOOTER BY ID - Scooter does not exist! scooterId = {}", scooterId);
                throw new ItemNotFoundException(String.format("Scooter does not exist. scooterId = %s", scooterId));
            }

            return scooter.toObject(Scooter.class);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("FIND SCOOTER BY ID - service function: scooterId = {}.\n Error message: {}.\n StackTrace: {}", scooterId, e.getMessage(), ExceptionUtils.getStackTrace(e));
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
                    .map((queryDocumentSnapshot -> queryDocumentSnapshot.toObject(Scooter.class)))
                    .collect(Collectors.toSet());
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("FIND ALL SCOOTER - service function. Error message: {}.\n StackTrace: {}", e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
        }

    }

    public Scooter createScooter(final Scooter scooter) {
        LOGGER.info("CREATE SCOOTER - service function invoked. Scooter = {}", scooter.toString());

        try {
            ApiFuture<WriteResult> collectionsApiFuture = db.collection(COLLECTION_SCOOTERS_PATH)
                    .document(scooter.getSerialNumber())
                    .set(scooter);

            LOGGER.info("CREATE SCOOTER - created successfully. Update time: {}", collectionsApiFuture.get().getUpdateTime().toDate());

            return scooter;

        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            LOGGER.error("CREATE SCOOTER - service function: scooter = {}.\n Error message: {}.\n StackTrace: {}", scooter, e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
        }
    }

    public void deleteScooterById(String scooterId) {
        LOGGER.info("DELETE SCOOTER - service function invoked. scooterId = {}", scooterId);
        try {
            DocumentSnapshot scooter = db.collection(COLLECTION_SCOOTERS_PATH).document(scooterId).get().get();

            if (!scooter.exists()) {
                LOGGER.warn("DELETE SCOOTER - Scooter does not exist! scooterId = {}", scooterId);
                throw new ItemNotFoundException(String.format("Scooter does not exist. scooterId = %s", scooterId));
            }

            ApiFuture<WriteResult> writeResult = db.collection(COLLECTION_SCOOTERS_PATH).document(scooterId).delete();

            LOGGER.info("DELETE SCOOTER - service function. Update time : {} | scooterId = {}", writeResult.get().getUpdateTime(), scooterId);
        } catch (ExecutionException | InterruptedException e) {
            LOGGER.error("DELETE SCOOTER - service function. scooterId = {}.\n Error message: {}. \n StackTrace: {}", scooterId, e.getMessage(), ExceptionUtils.getStackTrace(e));
            throw new ServiceException(e);
        }
    }
}
