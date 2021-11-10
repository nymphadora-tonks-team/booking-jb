package com.bootcamp.demo.model;

import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.model.util.Battery;
import com.bootcamp.demo.model.util.ScooterStatus;

import java.util.Objects;

public class Scooter {
    private String id;
    private Location currentLocation;
    private Battery battery;
    private ScooterStatus status;

    public Scooter(String id, Location currentLocation, Battery battery, ScooterStatus status) {
        this.id = id;
        this.currentLocation = currentLocation;
        this.battery = battery;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", currentLocation=" + currentLocation +
                ", battery=" + battery +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Scooter)) return false;
        Scooter scooter = (Scooter) o;
        return id.equals(scooter.id) && currentLocation.equals(scooter.currentLocation) && battery.equals(scooter.battery) && status == scooter.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currentLocation, battery, status);
    }
}
