package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Repository
public class ShopDAOImpl implements ShopDAO {

    private static String url = "jdbc:mysql://192.168.1.7:3306/SHOPZZ";
    private static String dbuser = "shopzz";
    private static String dbpass = "c6423kd";

    private static Connection connection = null;

    public ShopDAOImpl() {
        /* Loading Mariadb JDBC driver */
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
        }
    }

    @Override
    public List<Shop> getShopsSortedByDistance(Location userLocation) {
        connectToDatabse();

        ArrayList<Shop> shops = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Shop");

            while (resultSet.next()) {
                shops.add(
                        new Shop(resultSet.getString("name"),
                                resultSet.getString("image"),
                                new Location(resultSet.getFloat("latitude"),
                                            resultSet.getFloat("longitude"))
                                ));
            }

            shops.sort(new LocationComparator(userLocation));

            return shops;
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
        connectToDatabse();

        ArrayList<Shop> shops = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Shop");
        }
        catch (SQLException ex) {
                System.out.println("SQLException when getting favorite shops: " + ex.getMessage());
        }
        finally {
            closeConnection();
        }
        return shops;
    }

    @Override
    public Shop likeShop(ActionRequest request) {
        return interactWithShop(request, Interaction.LIKE);
    }

    @Override
    public Shop dislikeShop(ActionRequest request) {
        return interactWithShop(request, Interaction.DISLIKE);
    }

    private Shop interactWithShop(ActionRequest request, Interaction interaction) {
        String interactionCode;
        switch (interaction) {
            case LIKE:
                interactionCode = "1";
                break;
            case DISLIKE:
                interactionCode = "0";
                break;
            case RESET:
            default:
                interactionCode = "null";
                break;
        }

        connectToDatabse();

        Shop shop = request.getShop();

        int userId = getUserId(request);
        int shopId = shopExists(shop) ? shop.getId() : 0;

        // System.out.println("User Id: " + userId);
        // System.out.println("Shop Id: " + shopId);

        if (shopId != 0 && userId != 0) {
            try {
                /* Checking if interaction exists between user and shop *
                / If exist, update. Else create.
                 */
                PreparedStatement statement = connection.prepareStatement("UPDATE User_Shop SET interaction = " + interactionCode + ", date = NOW() WHERE user_id = ? AND shop_id = ?");
                statement.setInt(1, userId);
                statement.setInt(2, shopId);
                if (statement.executeUpdate() > 0) {
                    System.out.println("Update successfully");
                }
                else { // Then create
                    statement = connection.prepareStatement("INSERT INTO User_Shop VALUES (?, ?, 1, NOW())");
                    statement.setInt(1, userId);
                    statement.setInt(2, shopId);
                    statement.executeUpdate();
                    System.out.println("Create successfully");
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

    private void connectToDatabse() {
        /* Connecting to database */
        try {
            connection = DriverManager.getConnection(url, dbuser, dbpass);
            System.out.println("Connection successful");
        }
        catch (SQLException ex) {
            System.out.println("SQLException at connection: " + ex.getMessage());
        }
    }

    /**
     * Properly closes the connection
     */
    private void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
            }
        }
    }

    /**
     * Hashes for user password
     */
    private String sha1(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            byte[] result = messageDigest.digest(input.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        }
        catch (NoSuchAlgorithmException ex) {
            return "No such algorithm";
        }

    }
}
