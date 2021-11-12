package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.ScooterService;
import com.bootcamp.demo.service.assembler.ScooterAssembler;
import com.bootcamp.demo.service.exception.ServiceException;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.google.auth.oauth2.GoogleCredentials.fromStream;
import static com.google.firebase.FirebaseApp.initializeApp;
import static java.lang.System.getProperty;

@RestController
@RequestMapping(path = "/api/scooters")
@Slf4j
public class ScooterController {

    private final ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping
    public Set<Scooter> getScooters() {
        LOGGER.info("GET SCOOTERS - API endpoint invoked");
        return scooterService.findAllScooters();
    }

    @GetMapping("/{scooterId}")
    public Scooter getScooterById(@PathVariable(value = "scooterId") String scooterId) {
        LOGGER.info("GET SCOOTER BY ID - API endpoint invoked");
        return scooterService.findScooterById(scooterId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postScooter(@RequestBody final Scooter scooter) {
        LOGGER.info("POST SCOOTER - API endpoint invoked");
        scooterService.createScooter(scooter);
    }
}
