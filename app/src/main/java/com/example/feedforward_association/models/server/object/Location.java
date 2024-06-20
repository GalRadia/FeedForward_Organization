package com.example.feedforward_association.models.server.object;



public class Location {

    private double lat;
    private double lng;

    public Location() {
    }

    public Location(Double latitude, Double longitude) {
        super();
        this.lat = latitude;
        this.lng = longitude;
    }
    public Double getLatitude() {
        return lat;
    }
    public void setLatitude(Double latitude) {
        this.lat = latitude;
    }
    public Double getLongitude() {
        return lng;
    }
    public void setLongitude(Double longitude) {
        this.lng = longitude;
    }
    @Override
    public String toString() {
        return "Location [latitude=" + lat + ", longitude=" + lng + "]";
    }

}
