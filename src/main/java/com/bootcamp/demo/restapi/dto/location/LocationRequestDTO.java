package com.bootcamp.demo.restapi.dto.location;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

public class LocationRequestDTO {
    @Min(value = -90, message = "Latitude minimum -90 degree")
    @Max(value = 90, message = "Latitude maximum 90 degree")
    private Double latitude;

    @Min(value = -180, message = "Longitude minimum -180 degree")
    @Max(value = 180, message = "Longitude maximum 180 degree")
    private Double longitude;


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final var location = (LocationRequestDTO) o;
        return Objects.equals(this.getLatitude(), location.getLatitude())
                && Objects.equals(this.getLongitude(), location.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLatitude(), this.getLongitude());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("LocationRequestDTO{");
        sb.append("latitude=").append(this.getLatitude());
        sb.append(", longitude=").append(this.getLongitude()).append('}');
        return sb.toString();
    }

}
