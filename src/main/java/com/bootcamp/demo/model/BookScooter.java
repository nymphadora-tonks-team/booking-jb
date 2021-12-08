package com.bootcamp.demo.model;

public class BookScooter {
    private String id;
    private String accountId;
    private Double latitude;
    private Double longitude;
    private Double searchRadius;

    public BookScooter(){}

    public BookScooter(String id, String accountId, Double latitude, Double longitude, Double searchRadius) {
        this.id = id;
        this.accountId = accountId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.searchRadius = searchRadius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

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

    public Double getSearchRadius() {
        return searchRadius;
    }

    public void setSearchRadius(Double searchRadius) {
        this.searchRadius = searchRadius;
    }
}
