package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.service.ScooterService;
import com.bootcamp.demo.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping(path = "/api/scooters")
public class ScooterController {

    private final ScooterService scooterService;

    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Set<Scooter>> getScooters() {
        try {
            return new ResponseEntity<>(scooterService.findAllScooters(), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{scooterId}")
    @ResponseBody
    public ResponseEntity<Object> getScooterById(@PathVariable(value = "scooterId") String scooterId) {
        try {
            return new ResponseEntity<>(scooterService.findScooterById(scooterId), HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Object> postScooter(@RequestBody final Scooter scooter) {
        try {
            scooterService.createScooter(scooter);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
