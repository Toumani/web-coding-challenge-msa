package org.shopzz.shopzzapp.util;

import java.io.Serializable;

/**
 * A request that need the location of the user to be correctly performed
 */
public class ListRequest extends Request implements Serializable {
    private Location location;

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }

    public static final long serialVersionUID = 1L;
}
