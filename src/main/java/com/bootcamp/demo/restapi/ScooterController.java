package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.service.ScooterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/scooters")
@Slf4j
public class ScooterController {

    private final ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping("/{scooterId}")
    public ResponseEntity<Scooter> getScooterById(@PathVariable(value = "scooterId") String id) {
        LOGGER.info("GET SCOOTER BY ID - API endpoint invoked");
        return scooterService.findScooterById(id);
    }


    @GetMapping
    public Set<Scooter> getScooters() {
        LOGGER.info("GET SCOOTERS - API endpoint invoked");
        return scooterService.findAllScooters();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postScooter(@RequestBody final Scooter scooter) {
        LOGGER.info("POST SCOOTER - API endpoint invoked");
        scooterService.createScooter(scooter);
    }

    @PostMapping("/available")
    @ResponseStatus(value = HttpStatus.OK)
    public Set<Scooter> getAvailableScooters(@RequestBody final Location selectedLocation) {
        LOGGER.info("POST SCOOTER - API endpoint invoked");
        return scooterService.getAvailableScooters(selectedLocation);
    }
}
