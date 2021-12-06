package com.bootcamp.demo.restapi;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Sample RestController
 * Demo - Used for testing purposes
 */
@RestController
@RequestMapping(path = "/firebase", produces = APPLICATION_JSON_VALUE)
public class FirebaseController {

    private final Firestore firestoreDB;

    private FirebaseController(Firestore firestoreDB) {
        this.firestoreDB = firestoreDB;
    }

    /**
     * Returns the path for all collections stored in Firestore
     */
    @GetMapping("/getAllPaths")
    public Set<String> getAllPaths() {
        return StreamSupport.stream(firestoreDB.listCollections().spliterator(), false)
                .map(CollectionReference::getPath)
                .collect(Collectors.toUnmodifiableSet());

    }
}