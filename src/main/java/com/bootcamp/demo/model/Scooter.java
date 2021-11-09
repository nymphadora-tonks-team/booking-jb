package com.bootcamp.demo.model;

import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.model.util.Battery;
import com.bootcamp.demo.model.util.ScooterStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scooter {
    private String id;
    private Location currentLocation;
    private Battery battery;
    private ScooterStatus status;
}
