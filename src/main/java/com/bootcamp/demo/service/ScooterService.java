package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import org.springframework.http.ResponseEntity;
import com.bootcamp.demo.model.util.Location;

import java.util.List;
import java.util.Set;

public interface ScooterService {
    ResponseEntity<Scooter> findScooterById(String id);
    Set<Scooter> findAllScooters();

    void createScooter(Scooter scooter);
    Set<Scooter> getAvailableScooters(Location userLocation);
}
