package com.bootcamp.demo.service;

import com.bootcamp.demo.model.Scooter;

import java.util.List;

public interface ScooterService {
    Scooter findScooterById(String id);
    List<Scooter> findAllScooter();
    void createScooter(Scooter scooter);
}
