package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.ScooterService;
import com.bootcamp.demo.service.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

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
    public ResponseEntity<Set<Scooter>> getScooters() {
        LOGGER.info("GET SCOOTERS - API endpoint invoked");
        Set<Scooter> scooters = null;
        try {
            scooters = scooterService.findAllScooters();
            return new ResponseEntity<>(scooters, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(scooters, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{scooterId}")
    @ResponseBody
    public ResponseEntity<Object> getScooterById(@PathVariable(value = "scooterId") String scooterId) {
        LOGGER.info("GET SCOOTER BY ID - API endpoint invoked");

        try {
            Scooter scooter = scooterService.findScooterById(scooterId);
            return new ResponseEntity<>(scooter, HttpStatus.OK);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<Object> postScooter(@RequestBody final Scooter scooter) {
        LOGGER.info("POST SCOOTER - API endpoint invoked");
        try {
            scooter.getBattery().setStatus();
            scooterService.createScooter(scooter);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping("/updateScooter")
    public ResponseEntity<Object> updateScooter (final String scooterId, final Location location, final ScooterStatus newStatus, final Double newBatteryLevel)
    {
        try {
            scooterService.updateScooter(scooterId, location, newStatus, newBatteryLevel);
            LOGGER.info("UPDATE SCOOTER - API endpoint invoked");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }
    @DeleteMapping("/{scooterId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteScooterById(@PathVariable(value = "scooterId") String scooterId){
        LOGGER.info("DELETE SCOOTER BY ID - API endpoint invoked. scooterId = {}", scooterId);
        scooterService.deleteScooterById(scooterId);
    }
}