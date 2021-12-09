package com.bootcamp.demo.model;

import com.bootcamp.demo.model.component.Location;

public class BookingScooter {
    private String accountId;
    private Location location;
    private Double searchRadius;

    public BookingScooter(){}

    public BookingScooter(String accountId, Location location, Double searchRadius) {
        this.accountId = accountId;
        this.location = location;
        this.searchRadius = searchRadius;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(Double searchRadius) {
        this.searchRadius = searchRadius;
    }
}
