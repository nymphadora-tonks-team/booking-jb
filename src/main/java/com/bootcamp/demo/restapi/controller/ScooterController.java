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

    private final ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping
    @ResponseBody
    public Iterable<Scooter> getScooters() {
        return scooterService.findAllScooters();
    }

    @GetMapping("/{scooterId}")
    public Scooter getScooterById(@PathVariable(value = "scooterId") String scooterId) {
        return scooterService.findScooterById(scooterId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Scooter postScooter(@Valid @RequestBody ScooterRequestDTO scooterRequestDTO) {
        return scooterService.createScooter(ScooterAssembler.toModel(scooterRequestDTO));
    }

    @DeleteMapping("/{scooterId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteScooterById(@PathVariable(value = "scooterId") String scooterId) {
        scooterService.deleteScooterById(scooterId);
    }
}
