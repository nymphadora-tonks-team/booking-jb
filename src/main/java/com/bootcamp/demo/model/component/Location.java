package com.bootcamp.demo.model.component;

import java.util.Objects;

public final class Location {
    private Double latitude;
    private Double longitude;

    public Location() {
    }

    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(final Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(final Double longitude) {
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
        final var location = (Location) o;
        return Objects.equals(this.getLatitude(), location.getLatitude())
                && Objects.equals(this.getLongitude(), location.getLongitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getLatitude(), this.getLongitude());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Location{");
        sb.append("latitude=").append(this.getLatitude());
        sb.append(", longitude=").append(this.getLongitude()).append('}');
        return sb.toString();
    }
}
