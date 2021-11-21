package com.bootcamp.demo.restapi;

import com.bootcamp.demo.FirebaseController;
import com.bootcamp.demo.model.Scooter;
import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.service.ScooterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.logging.Logger;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/scooters")
public class ScooterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterController.class);
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
    public Iterable<Scooter> getScooters() {
        LOGGER.info("GET SCOOTERS - API endpoint invoked");
        return scooterService.findAllScooter();
    }

    @GetMapping("/{scooterId}")
    public Scooter getScooterById(@PathVariable(value = "scooterId") String scooterId) {
        LOGGER.info("GET SCOOTER BY ID - API endpoint invoked");
        return scooterService.findScooterById(scooterId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public void postScooter(@RequestBody Scooter scooter) {
        LOGGER.info("POST SCOOTER - API endpoint invoked");
        scooter.getBattery().setStatus();
        scooterService.createScooter(scooter);
    }

    @GetMapping("/available")
    public Set<Scooter> getAvailableScooters(){
        Location userLocation = new Location();
        userLocation.setLatitude(0.0);
        userLocation.setLongitude(0.0);
        return scooterService.getAvailableScooters(userLocation);
    }
}
