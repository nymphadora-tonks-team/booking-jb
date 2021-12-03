package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import org.springframework.http.ResponseEntity;

import java.util.Set;

public interface ScooterService {
    ResponseEntity<Scooter> findScooterById(String id);
    Set<Scooter> findAllScooters();
    void createScooter(Scooter scooter);
    Set<Scooter> getAvailableScooters(Location userLocation);
}
