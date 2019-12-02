package org.shopzz.dao;

import org.shopzz.model.Shop;
import org.shopzz.shopzzapp.util.Location;
import org.shopzz.shopzzapp.util.LocationComparator;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
