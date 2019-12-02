package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.Location;

import java.util.List;

public interface ShopDAO {
    List<Shop> getShopsSortedByDistance(Location userLocation);
    List<Shop> getFavoriteShops();
    Shop likeShop(int id);
    Shop dislikeShop(int id);
}
