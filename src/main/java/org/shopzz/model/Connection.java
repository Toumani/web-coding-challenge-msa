package org.shopzz.model;

import org.shopzz.shopzzapp.util.Location;

public class Connection {
    private String checksum;
    int userId;
    private Location location;

    public String getChecksum() { return checksum; }

    public void setChecksum(String checksum) { this.checksum = checksum; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    public Location getLocation() { return location; }

    public void setLocation(Location location) { this.location = location; }
}
