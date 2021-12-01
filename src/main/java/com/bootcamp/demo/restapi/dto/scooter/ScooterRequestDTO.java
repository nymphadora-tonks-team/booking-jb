package com.bootcamp.demo.restapi.dto.scooter;

import com.bootcamp.demo.restapi.dto.battery.BatteryRequestDTO;
import com.bootcamp.demo.restapi.dto.location.LocationRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

public class ScooterRequestDTO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScooterRequestDTO.class);

    @NotEmpty(message = "serialNumber can't be empty")
    private String serialNumber;

    @Valid
    private LocationRequestDTO currentLocation;

    @Valid
    private BatteryRequestDTO battery;

    @NotEmpty(message = "Please provide a status")
    @Pattern(regexp = "^(AVAILABLE|RESERVED)$", message = "Status valid values: AVAILABLE or RESERVED")
    private String status;

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public LocationRequestDTO getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(final LocationRequestDTO currentLocation) {
        this.currentLocation = currentLocation;
    }

    public BatteryRequestDTO getBattery() {
        return battery;
    }

    public void setBattery(final BatteryRequestDTO battery) {
        this.battery = battery;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ScooterRequestDTO{");
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
        final var scooter = (ScooterRequestDTO) o;
        return Objects.equals(this.getSerialNumber(), scooter.getSerialNumber())
                && Objects.equals(this.getCurrentLocation(), scooter.getCurrentLocation())
                && Objects.equals(this.getBattery(), scooter.getBattery())
                && Objects.equals(this.getStatus(), scooter.getStatus());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSerialNumber(), this.getCurrentLocation(), this.getBattery(), this.getStatus());
    }
}
