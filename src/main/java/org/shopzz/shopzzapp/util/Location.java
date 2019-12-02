package org.shopzz.shopzzapp.util;

import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Location class defines latitude and longitude for a shop and a method to calculate its actual distance from a given
 * location
 */
public class Location implements Serializable {
    private float longitude, latitude;

    private static int earthRadius = 6371;

    public Location(float latitude, float longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public Double distanceTo(Location otherLocation) {
        // Converting to radian

        double latitude1 = this.latitude*Math.PI/180;
        double latitude2 =  otherLocation.latitude*Math.PI/180;

        double longitude1 = this.longitude*Math.PI/180;
        double longitude2 = otherLocation.longitude*Math.PI/180;

        // Haversine formula
        double a = Math.pow(Math.sin((latitude1 - latitude2)/2), 2) +
                    Math.cos(latitude1)*Math.cos(latitude2)*
                    Math.pow(Math.sin((longitude1 - longitude2)/2), 2);

        double c = 2*Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return earthRadius*c;
    }

    public float getLongitude() { return longitude; }

    public void setLongitude(float longitude) { this.longitude = longitude; }

    public float getLatitude() { return latitude; }

    public void setLatitude(float latitude) { this.latitude = latitude; }

    static final long serialVersionUID = 1L;
}
