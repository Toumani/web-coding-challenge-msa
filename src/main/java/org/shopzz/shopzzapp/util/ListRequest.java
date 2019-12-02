package org.shopzz.shopzzapp.util;

/**
 * A request that need the location of the user to be correctly performed
 */
public class ListRequest extends Request {
    private Location location;

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }
}
