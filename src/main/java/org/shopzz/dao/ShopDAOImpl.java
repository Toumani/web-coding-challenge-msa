package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.*;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ShopDAOImpl extends DAOImplementor implements ShopDAO {

    private static String resetDelay = "0:00:20";

    @Override
    public List<Shop> getShopsSortedByDistance(ListRequest request) {
        connectToDatabase();

        int userId = getUserId(request);

        ArrayList<Shop> shops = null;

        try {
            if (userId > 0) {
                Statement statement = connection.createStatement();
                /* First we reset the status of all disliked shop by the current user only
                 * In fact there's no need to reset for all users which may require useless resources*/
                String resetQuery = "UPDATE User_Shop " +
                        "SET interaction = null " +
                        "WHERE TIMEDIFF(NOW(), date) > '" + resetDelay + "' " +
                        "AND interaction = 0 " +
                        "AND user_id = " + userId;
                statement.executeUpdate(resetQuery);

                ResultSet allShopsResultSet = statement.executeQuery("SELECT s.id, s.name, s.latitude, s.longitude, s.image FROM Shop s");
                ArrayList<Shop> allShops = fillShopList(allShopsResultSet);

                ResultSet filteredShopsResultSet = statement.executeQuery("SELECT s.id, s.name, s.latitude, s.longitude, s.image " +
                                                                    "FROM Shop s " +
                                                                    "INNER JOIN User_Shop us " +
                                                                    "ON s.id = us.shop_id " +
                                                                    "WHERE user_id = " + userId + " " +
                                                                    "AND interaction IS NOT NULL ");
                ArrayList<Shop> filteredShops = fillShopList(filteredShopsResultSet);

                /* Filtering out disliked shops
                * Mariadb current stable release (10.1.30) doesn't support EXCEPT SQL keyword */
                shops = new ArrayList<>();

                int[] filteringId = new int[filteredShops.size()];
                int i = 0;
                for (Shop shop : filteredShops) {
                    filteringId[i++] = shop.getId();
                }

                for (Shop shop : allShops) {
                    boolean keep = true;
                    for (int index : filteringId) {
                        if (shop.getId() == index) {
                            keep = false;
                            break;
                        }
                    }
                    if (keep)
                        shops.add(shop);
                }

                shops.sort(new LocationComparator(request.getLocation()));
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException when getting shops: " + ex.getMessage());
        }
        finally {
            closeConnection();
        }
        return shops;
    }

    @Override
    public List<Shop> getFavoriteShops(ListRequest request) {
        connectToDatabase();

        int userId = getUserId(request);
        ArrayList<Shop> shops = null;

        if (userId > 0) {
            try {
                Statement statement = connection.createStatement();
                String query = "SELECT s.id, s.name, s.latitude, s.longitude, s.image " +
                                "FROM Shop s " +
                                "INNER JOIN User_Shop us " +
                                "ON s.id = us.shop_id " +
                                "WHERE us.interaction = 1 " +
                                "AND us.user_id = " + userId;

                ResultSet resultSet = statement.executeQuery(query);
                shops = fillShopList(resultSet);
                shops.sort(new LocationComparator(request.getLocation()));
            } catch (SQLException ex) {
                System.out.println("SQLException when getting favorite shops: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }
        return shops;
    }

    @Override
    public Shop likeShop(ActionRequest request) {
        return interactWithShop(request, Interaction.LIKE);
    }

    /**
     * Dislike a shop
     * @param request
     * @return the corresponding shop. Null if no shop found
     */
    @Override
    public Shop dislikeShop(ActionRequest request) {
        return interactWithShop(request, Interaction.DISLIKE);
    }

    /**
     * Removes a shop from favorites
     * @param request
     * @return the corresponding shop. Null if no shop found
     */
    @Override
    public Shop removeFromFavorite(ActionRequest request) {
        return interactWithShop(request, Interaction.RESET);
    }

    private ArrayList<Shop> fillShopList(ResultSet resultSet) {
        ArrayList<Shop> shops = new ArrayList<>();

        try {
            while (resultSet.next()) {
                shops.add(
                        new Shop(resultSet.getInt("id"),
                                resultSet.getString("name"),
                                resultSet.getString("image"),
                                new Location(resultSet.getFloat("latitude"),
                                        resultSet.getFloat("longitude"))
                        ));
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException when getting shops: " + ex.getMessage());
        }

        return shops;
    }

    /**
     * Likes, dislikes or resets interaction between user and shop
     * @param request client request
     * @param interaction like, dislike or reset
     * @return the corresponding shop. Null if no shop found
     */
    private Shop interactWithShop(ActionRequest request, Interaction interaction) {
        String interactionCode;
        switch (interaction) {
            case LIKE:
                interactionCode = "1";
                System.out.print("Liking: ");
                break;
            case DISLIKE:
                interactionCode = "0";
                System.out.print("Disiking: ");
                break;
            case RESET:
            default:
                interactionCode = "null";
                break;
        }

        connectToDatabase();

        Shop shop = request.getShop();

        int userId = getUserId(request);
        int shopId = shopExists(shop) ? shop.getId() : 0;

        if (shopId != 0 && userId != 0) {
            try {
                /* Checking if interaction exists between user and shop *
                / If exist, update. Else create.
                 */
                PreparedStatement statement = connection.prepareStatement("UPDATE User_Shop SET interaction = " + interactionCode + ", date = NOW() WHERE user_id = ? AND shop_id = ?");

                statement.setInt(1, userId);
                statement.setInt(2, shopId);
                if (statement.executeUpdate() > 0);
                else { // Then create
                    statement = connection.prepareStatement("INSERT INTO User_Shop VALUES (?, ?, 1, NOW())");
                    statement.setInt(1, userId);
                    statement.setInt(2, shopId);
                    statement.executeUpdate();
                }
                return shop;
            } catch (SQLException ex) {
                System.out.println("SQLException when liking a shop: " + ex.getMessage());
            } finally {
                closeConnection();
            }
        }

        return null;
    }

    /**
     * Get user actual id in database according to the authentication hashed string
     * @param request client request
     * @return user's id if found 0 if not
     */
    private int getUserId(Request request) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT user_id FROM Connection WHERE checksum = ?");
            statement.setString(1, request.getHashcode());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("user_id");
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        return 0;
    }

    /**
     * Check whether a shop exists or not.
     * The method searches only based on the shop's id. But a further investigation may be applied
     * @param shop the shop to check on
     * @return true if found false if not or if exception throw
     */
    private boolean shopExists(Shop shop) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT id FROM Shop WHERE id = ?");
            statement.setInt(1, shop.getId());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        return false;
    }
}
