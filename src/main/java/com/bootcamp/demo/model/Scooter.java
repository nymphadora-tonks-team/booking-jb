package com.bootcamp.demo.model;

import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.model.util.Battery;
import com.bootcamp.demo.model.util.ScooterStatus;

import java.io.Serializable;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class Scooter implements Serializable {
    private Location currentLocation;
    private Battery battery;
    private ScooterStatus status;

    private Scooter() {}

    public Scooter(String id, Location currentLocation, Battery battery, ScooterStatus status) {
        this.currentLocation = requireNonNull(currentLocation);
        this.battery = requireNonNull(battery);
        this.status = requireNonNull(status);
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(Battery battery) {
        this.battery = battery;
    }

    public ScooterStatus getStatus() {
        return status;
    }

    public void setStatus(ScooterStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Scooter{" +
                "currentLocation=" + currentLocation +
                ", battery=" + battery +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scooter)) return false;
        Scooter scooter = (Scooter) o;
        return currentLocation.equals(scooter.currentLocation) && battery.equals(scooter.battery) && status == scooter.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentLocation, battery, status);
    }
}
