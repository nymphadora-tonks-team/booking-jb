package com.bootcamp.demo.restapi.controller;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.restapi.assembler.ScooterAssembler;
import com.bootcamp.demo.restapi.dto.scooter.ScooterRequestDTO;
import com.bootcamp.demo.service.ScooterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/scooters")
public class ScooterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterController.class);
    private final ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping
    @ResponseBody
    public Iterable<Scooter> getScooters() {
        LOGGER.info("GET SCOOTERS - API endpoint invoked");
        return scooterService.findAllScooters();
    }

    @GetMapping("/{scooterId}")
    public Scooter getScooterById(@PathVariable(value = "scooterId") String scooterId) {
        LOGGER.info("GET SCOOTER BY ID - API endpoint invoked. scooterId = {}", scooterId);
        return scooterService.findScooterById(scooterId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Scooter postScooter(@Valid @RequestBody ScooterRequestDTO scooterRequestDTO) {
        LOGGER.info("POST SCOOTER - API endpoint invoked. Scooter = {}", scooterRequestDTO);
        return scooterService.createScooter(ScooterAssembler.toModel(scooterRequestDTO));
    }

    @DeleteMapping("/{scooterId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteScooterById(@PathVariable(value = "scooterId") String scooterId) {
        LOGGER.info("DELETE SCOOTER BY ID - API endpoint invoked. scooterId = {}", scooterId);
        scooterService.deleteScooterById(scooterId);
    }
}
