package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);
    private static final String GET_USERNAME_REQUEST = "SELECT user FROM members WHERE login = ? AND password = ?";
    private static final String DB_URL = "jdbc:sqlite:membersOfChat.db";
    private static final String CHANGE_USERNAME_REQUEST = "UPDATE members SET user = ? WHERE user = ?";
    private static final String ADD_NEW_USER_REQUEST = "INSERT INTO members(login, password, user) VALUES(?, ?, ?)";
    private Connection connection;
    private PreparedStatement getUsernameStatement;
    private PreparedStatement changeUsernameStatement;
    private PreparedStatement addNewUserStatement;

    public DatabaseService() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            getUsernameStatement = connection.prepareStatement(GET_USERNAME_REQUEST);
            changeUsernameStatement = connection.prepareStatement(CHANGE_USERNAME_REQUEST);
            addNewUserStatement = connection.prepareStatement(ADD_NEW_USER_REQUEST);
        } catch (SQLException e) {
            logger.error("Failed to database connection");
        }
    }

    public String getUsernameByLoginAndPassword(String login, String password) {
        String username = null;
        try {
            getUsernameStatement.setString(1, login);
            getUsernameStatement.setString(2, password);
            ResultSet resultSet = getUsernameStatement.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString("user");
            }
        } catch (SQLException e) {
            logger.error("Failed to database connection");
        }
        return username;
    }

    public void changeUsername(String newUsername, String oldUsername) {
        try {
            changeUsernameStatement.setString(1, newUsername);
            changeUsernameStatement.setString(2, oldUsername);
            changeUsernameStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Failed to database connection");
        }
    }

    public void addNewUser(String username, String login, String password) {
        try {
            System.out.println("start");
            addNewUserStatement.setString(1, login);
            addNewUserStatement.setString(2, password);
            addNewUserStatement.setString(3, username);
            addNewUserStatement.executeUpdate();
            System.out.println("end");
        } catch (SQLException e) {
            logger.error("Failed to database connection");
        }

    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                logger.info("Connection with DB closed.");
            }
        } catch (SQLException e) {
            logger.error("Failed to close database connection");
        }
    }
}
