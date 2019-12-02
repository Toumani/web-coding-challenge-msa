package org.shopzz.shopzzapp.web.controller;

import org.shopzz.dao.ShopDAO;
import org.shopzz.dao.ShopDAOImpl;
import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.ActionRequest;
import org.shopzz.shopzzapp.util.ListRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShopController {

    private ShopDAO shopDAO = new ShopDAOImpl();

    @PostMapping(path = "/shops")
    public List<Shop> getNearbyShops(@RequestBody ListRequest request) {
        return shopDAO.getShopsSortedByDistance(request);
    }

    @PostMapping(path = "/favorite")
    public List<Shop> getFavoriteShops(@RequestBody ListRequest request) {
        return shopDAO.getFavoriteShops(request);
    }

    @PostMapping(path = "/like")
    public Shop likeShop(@RequestBody ActionRequest request) {
        return shopDAO.likeShop(request);
    }

    @PostMapping(path = "/dislike")
    public Shop dislikeShop(@RequestBody ActionRequest request) {
        return shopDAO.dislikeShop(request);
    }

    @PostMapping(path = "/remove")
    public Shop removeFromFavorite(@RequestBody ActionRequest request) {
        return  shopDAO.removeFromFavorite(request);
    }

}
