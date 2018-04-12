package pl.dmcs.gpslocationsystem.model;

import java.io.Serializable;

/**
 * Created by Damian on 26.10.2016.
 */
public class Coordinates implements Serializable{

    private double lat;
    private double lng;

    public Coordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}

