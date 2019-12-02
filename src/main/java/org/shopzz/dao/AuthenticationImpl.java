package org.shopzz.dao;

import org.shopzz.model.Connection;
import org.shopzz.shopzzapp.util.Location;
import org.shopzz.shopzzapp.util.RegisterRequest;
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
            ResultSet resultSet = checkUserAndPassword(request);

            // We're sure we have only one result or nothing
            if (resultSet.next()) {
                // Let's generate a random string
                Random random = new Random();
                String randomString = sha1(random.nextInt(Integer.MAX_VALUE) + "");
                connection = new Connection(randomString, request.getLocation());
                PreparedStatement statement = DAOImplementor.connection.prepareStatement("INSERT INTO `Connection` VALUES (?, ?, ?, ?)");
                statement.setString(1, randomString);
                statement.setInt(2, resultSet.getInt("id"));
                statement.setFloat(3, request.getLocation().getLatitude());
                statement.setFloat(4, request.getLocation().getLongitude());
                statement.executeQuery();
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException while auth: " + ex.getMessage());
        }

        closeConnection();

        return connection;
    }

    @Override
    public Connection register(RegisterRequest request) {
        connectToDatabase();
        Connection connection = null;

        try {
            ResultSet resultSet = checkUser(request);

            // We should proceed insertion only if the user doesn't exist yet
            if (!resultSet.next()) {
                PreparedStatement statement = DAOImplementor.connection.prepareStatement("INSERT INTO User (name, email, password) VALUES (?, ?, ?)");
                statement.setString(1, request.getName());
                statement.setString(2, request.getEmail());
                statement.setString(3, sha1(request.getPassword()));
                statement.executeQuery();
            }
        }
        catch (SQLException ex) {
            System.out.println("SQLException while registering: " + ex.getMessage());
        }

        closeConnection();

        return signIn(request);
    }

    private ResultSet checkUserAndPassword(SignInRequest request) throws SQLException {
        PreparedStatement statement = DAOImplementor.connection.prepareStatement("SELECT * FROM User WHERE email = ? AND password = ?");
        statement.setString(1, request.getEmail());
        statement.setString(2, sha1(request.getPassword()));
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
    }

    private ResultSet checkUser(SignInRequest request) throws SQLException {
        PreparedStatement statement = DAOImplementor.connection.prepareStatement("SELECT * FROM User WHERE email = ?");
        statement.setString(1, request.getEmail());
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
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