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
    @Autowired
    ShopDAO shopDAO;

    @PostMapping(path = "/shops")
    public List<Shop> getNearbyShops(@RequestBody ListRequest request) {
        return shopDAO.getShopsSortedByDistance();
    }

    @PostMapping(path = "/favorite")
    public List<Shop> getFavoriteShops(@RequestBody ListRequest request) {
        return shopDAO.getFavoriteShops();
    }

    @PostMapping(path = "/like")
    public void likeShop(@RequestBody ActionRequest request) {
        shopDAO.likeShop(request.getShop().getId());
    }

    @PostMapping(path = "/dislike")
    public void dislikeShop(@RequestBody ActionRequest request) {
        shopDAO.dislikeShop(request.getShop().getId());
    }

}
