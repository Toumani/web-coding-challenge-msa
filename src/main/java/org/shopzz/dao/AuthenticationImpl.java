package org.shopzz.dao;

import org.shopzz.model.Connection;
import org.shopzz.shopzzapp.util.Location;
import org.shopzz.shopzzapp.util.SignInRequest;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class AuthenticationImpl extends DAOImplementor implements AuthenticationDAO {

    public AuthenticationImpl() {
    }

    @Override
    public Connection signIn(SignInRequest request) {
        connectToDatabase();
        Connection connection = null;

        try {
            PreparedStatement statement = DAOImplementor.connection.prepareStatement("SELECT * FROM User WHERE email = ? AND password = ?");
            statement.setString(1, request.getEmail());
            statement.setString(2, sha1(request.getPassword()));
            ResultSet resultSet = statement.executeQuery();

            // We're sure we have only one result or nothing
            if (resultSet.next()) {
                // Let's generate a random string
                Random random = new Random();
                String randomString = sha1(random.nextInt(Integer.MAX_VALUE) + "");
                connection = new Connection(randomString, request.getLocation());
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException while auth");
        }
        return connection;
    }

    @Override
    public Connection register(SignInRequest request) {
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