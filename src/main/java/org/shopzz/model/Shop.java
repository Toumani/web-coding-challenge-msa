package org.shopzz.model;

import org.shopzz.shopzzapp.util.Location;

public class Shop {
    private int id;
    private String name, image;
    private Location location;

    public Shop() {

    }

    public Shop(int id, String name, String image, Location location) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
