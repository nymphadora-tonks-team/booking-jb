package com.bootcamp.demo.model;

import com.bootcamp.demo.model.util.Location;
import com.bootcamp.demo.model.util.Battery;
import com.bootcamp.demo.model.util.ScooterStatus;

import java.util.Objects;

public final class Scooter {
    private String serialNumber;
    private Location currentLocation;
    private Battery battery;
    private ScooterStatus status;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(final String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(final Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Battery getBattery() {
        return battery;
    }

    public void setBattery(final Battery battery) {
        this.battery = battery;
    }

    public ScooterStatus getStatus() {
        return status;
    }

    public void setStatus(final ScooterStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Scooter{");
        sb.append("serialNumber='").append(serialNumber).append(", ");
        sb.append("currentLocation=").append(currentLocation).append(", ");
        sb.append("battery=").append(battery).append(", ");
        sb.append("status=").append(status).append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final var scooter = (Scooter) o;
        return this.getSerialNumber().equals(scooter.getSerialNumber())
                && this.getCurrentLocation().equals(scooter.getCurrentLocation())
                && this.getBattery().equals(scooter.getBattery())
                && this.getStatus() == scooter.getStatus();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSerialNumber(), this.getCurrentLocation(), this.getBattery(), this.getStatus());
    }
}
