package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;

public interface ScooterService {
    Scooter findScooterById(String scooterId);
    Set<Scooter> findAllScooters();
    void createScooter(Scooter scooter);
}
