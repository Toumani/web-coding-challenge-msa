package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.ActionRequest;
import org.shopzz.shopzzapp.util.ListRequest;
import org.shopzz.shopzzapp.util.Location;

import java.util.List;

public interface ShopDAO {
    List<Shop> getShopsSortedByDistance(ListRequest request);
    List<Shop> getFavoriteShops(ListRequest request);
    Shop likeShop(ActionRequest actionRequest);
    Shop dislikeShop(ActionRequest actionRequest);
    Shop removeFromFavorite(ActionRequest actionRequest);
}
