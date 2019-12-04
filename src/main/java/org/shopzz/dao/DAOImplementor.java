package org.shopzz.dao;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

public class DAOImplementor {
    private static String url = "jdbc:mysql://localhost:3306/SHOPZZ"; //"jdbc:mysql://192.168.1.7:3306/SHOPZZ";
    private static String dbuser = "shopzz";
    private static String dbpass = "c6423kd";

    protected static Connection connection = null;

    protected void connectToDatabase() {
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
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            }
            catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
            }
        }
    }
}
