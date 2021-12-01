package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface ScooterService {
    Scooter findScooterById(String scooterId);

    Set<Scooter> findAllScooters();

    void createScooter(Scooter scooter);
    String updateScooter (final String scooterId, Location location, ScooterStatus newStatus, Double newBatteryLevel) throws ExecutionException, InterruptedException;
    void deleteScooterById(String scooterId);
}
