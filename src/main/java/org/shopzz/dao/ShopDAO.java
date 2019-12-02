package org.shopzz.dao;

import org.shopzz.model.Shop;

import java.util.List;

public interface ShopDAO {
    List<Shop> getShopsSortedByDistance();
    List<Shop> getFavoriteShops();
    Shop likeShop(int id);
    Shop dislikeShop(int id);
}
