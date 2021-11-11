package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.ScooterService;
import com.google.firebase.database.annotations.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/scooters")
@Slf4j
public class ScooterController {

    private final ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping("/{scooterId}")
    public Scooter getScooterById(@PathVariable(value = "scooterId") String id) {
        LOGGER.info("GET SCOOTER BY ID - API endpoint invoked");
        return scooterService.findScooterById(id);
    }

    @GetMapping
    public Iterable<Scooter> getScooters() {
        LOGGER.info("GET SCOOTERS - API endpoint invoked");
        return scooterService.findAllScooter();
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postScooter(@RequestBody Scooter scooter) {
        LOGGER.info("POST SCOOTER - API endpoint invoked");
        scooterService.createScooter(scooter);
    }
}
