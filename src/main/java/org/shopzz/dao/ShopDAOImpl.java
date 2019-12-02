package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ShopDAOImpl implements ShopDAO {

    static {
        // Connection to database goes here
    }

    @Override
    public List<Shop> getShopsSortedByDistance() {
        return null;
    }

    @Override
    public List<Shop> getFavoriteShops() {
        return null;
    }

    @Override
    public Shop likeShop(int id) {
        return null;
    }

    @Override
    public Shop dislikeShop(int id) {
        return null;
    }
}
