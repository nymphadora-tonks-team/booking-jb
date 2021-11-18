package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.ScooterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/scooters")
public class ScooterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterController.class);
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
        scooter.getBattery().setStatus();
        scooterService.createScooter(scooter);
    }
}
