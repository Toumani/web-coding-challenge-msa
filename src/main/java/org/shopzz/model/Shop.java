package org.shopzz.model;

import org.shopzz.shopzzapp.util.Location;

import java.io.Serializable;

public class Shop implements Serializable {
    private int id;

    private String name, image;
    private Location location;

    private double distanceToUser;

    public Shop() {

    }

    public Shop(String name, String image, Location location) {
        this.name = name;
        this.image = image;
        this.location = location;
    }

    public Shop(int id, String name, String image, Location location) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.location = location;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getDistanceToUser() { return distanceToUser; }

    public void setDistanceToUser(double distanceToUser) { this.distanceToUser = distanceToUser; }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    static final long serialVersionUID = 1L;
}
