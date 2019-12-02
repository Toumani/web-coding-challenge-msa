package org.shopzz.dao;

import org.shopzz.model.Connection;
import org.shopzz.shopzzapp.util.SignInRequest;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AuthenticationImpl implements AuthenticationDAO {


    private static String url = "jdbc:mysql://192.168.1.7:3306/SHOPZZ";
    private static String dbuser = "shopzz";
    private static String dbpass = "c6423kd";

    private static String resetDelay = "0:00:20";

    private static java.sql.Connection connection = null;

    public AuthenticationImpl() {
        /* Loading Mariadb JDBC driver */
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
        }
    }

    @Override
    public Connection signIn(SignInRequest request) {
        connectToDatabse();


        return null;
    }

    @Override
    public Connection register(SignInRequest request) {
        return null;
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