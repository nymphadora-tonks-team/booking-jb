package com.bootcamp.demo.service;

import com.bootcamp.demo.FirebaseController;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.assembler.ScooterAssembler;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
public class ScooterServiceImpl implements ScooterService{
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ScooterServiceImpl.class.getName());

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
                    .collect(Collectors.toList());
        } catch (ExecutionException | InterruptedException e) {
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
            throw new ServiceException();
        }
    }
}
