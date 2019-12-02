package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.ListRequest;
import org.shopzz.shopzzapp.util.Location;
import org.shopzz.shopzzapp.util.LocationComparator;
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
        /* Chargement du driver JDBC pour Mariadb */
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
        }

        try {
            connection = DriverManager.getConnection(url, dbuser, dbpass);
            System.out.println("Connection successful");
        }
        catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    @Override
    public List<Shop> getShopsSortedByDistance(Location userLocation) {
        ArrayList<Shop> shops = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Shop");

            while (resultSet.next()) {
                // System.out.println("Adding result");
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
            System.out.println("SQLException: " + ex.getMessage());
        }
        finally {
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("Sha1 test: " + sha1("password"));
                    System.out.println("Sha1 test: " + sha1("adfmp5£4#es."));
                }
                catch (SQLException ex) {
                    System.out.println("SQLException: " + ex.getMessage());
                }
            }
        }
        return shops;
    }

    @Override
    public List<Shop> getFavoriteShops(ListRequest request) {
        ArrayList<Shop> shops = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Shop");
        }
        catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
        }
        finally {
                if (connection != null) {
                    try {
                        connection.close();
                        System.out.println("CoSine of pi: " + Math.cos(Math.PI));
                    }
                    catch (SQLException ex) {
                        System.out.println("SQLException: " + ex.getMessage());
                    }
                }
            }
        return shops;
    }

    @Override
    public Shop likeShop(int id) {
        return null;
    }

    @Override
    public Shop dislikeShop(int id) {
        return null;
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
