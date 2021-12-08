package com.bootcamp.demo.restapi;

import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.component.Location;
import com.bootcamp.demo.model.component.ScooterStatus;
import com.bootcamp.demo.service.ScooterService;
import com.bootcamp.demo.service.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

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

    @PutMapping("/updateScooter")
    public ResponseEntity<Object> updateScooter(final String scooterId, final Location location, final ScooterStatus newStatus, final Double newBatteryLevel) {
        try {
            scooterService.updateScooter(scooterId, location, newStatus, newBatteryLevel);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ExecutionException | InterruptedException | IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/available/{searchRadius}")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Scooter> getAvailableScooters(@PathVariable(value = "searchRadius") Double searchRadius, @RequestBody final Location selectedLocation) {
        return scooterService.getAvailableScooters(selectedLocation, searchRadius);
    }
}