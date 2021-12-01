package com.bootcamp.demo.restapi.assembler;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Battery;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.restapi.dto.scooter.ScooterRequestDTO;

public class ScooterAssembler {

    public static Scooter toModel(ScooterRequestDTO dto) {
        // ScooterRequestDTO is assumed that is already validated
        Scooter scooter = new Scooter();

        scooter.setSerialNumber(dto.getSerialNumber());
        scooter.setCurrentLocation(new Location(dto.getCurrentLocation().getLatitude(), dto.getCurrentLocation().getLongitude()));
        scooter.setBattery(new Battery(dto.getBattery().getLevel()));
        scooter.setStatus(ScooterStatus.valueOf(dto.getStatus()));

        return scooter;
    }
}
