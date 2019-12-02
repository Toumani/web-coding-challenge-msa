package org.shopzz.shopzzapp.util;

import org.shopzz.model.Shop;

import java.util.Comparator;

public class LocationComparator implements Comparator<Shop> {
    private Location userLocation;

    /**
     * Actually compares distance between user and a shops. I also sets the distanceToUser property in the shop object
     * @param userLocation: the user location
     */
    public LocationComparator(Location userLocation) {
        this.userLocation = userLocation;
    }

    @Override
    public int compare(Shop shop1, Shop shop2) {
        double distance1 = shop1.getLocation().distanceTo(userLocation);
        double distance2 = shop2.getLocation().distanceTo(userLocation);

        shop1.setDistanceToUser(distance1);
        shop2.setDistanceToUser(distance2);

        double difference = distance1 - distance2 ;
        if (difference < 0)
            return -1;
        if (difference == 0)
            return 0;
        else
            return 1;
    }
}
