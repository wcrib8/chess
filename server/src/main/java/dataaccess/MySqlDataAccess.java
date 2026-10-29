package dataaccess;

import dataaccess.exceptions.*;
import model.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws SQLException, DataAccessException {
        configureDatabase();
    }
    /**
     * users table~
     *      username(primary key), hashed password, email
     *      username, UserData?
     *
     * auth table~
     *      authToken(primary key), username
     *      authToken, AuthData?
     *
     * game table~
     *      gameID(primary key), whiteUsername, blackUsername, gameName, game
     *      GameData?
     */

    public void clear() {}

    public void createUser(UserData userData) {}

    public UserData getUser(String username) {
        return null;
    }

    public void createGame(GameData gameData) {}

    public GameData getGame(int gameID) {
        return null;
    }

    public ArrayList<GameData> listGames() {
        return null;
    }

    public void updateGame(GameData gameData, String playerColor, String username) {}

    public void createAuth(AuthData authData) {}

    public AuthData getAuth(String username) {
        return null;
    }

    public void deleteAuth(AuthData authData) {}



//    void storeUserPassword(String username, String clearTextPassword) {
//        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
//
//        // Write the hashed password to the database along with the user's other information
//        writeHashedPasswordToDatabase(username, hashedPassword);
//    }
//
//    boolean verifyUser(String username, String providedClearTextPassword) {
//        // Read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            `userID` int NOT NULL AUTO_INCREMENT,
            `username` varchar(256) NOT NULL,
            `password` char(60) NOT NULL,
            `email` varchar(256) NOT NULL,
            PRIMARY KEY (`userID`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
            `authID` int NOT NULL AUTO_INCREMENT,
            `authToken` varchar(256) NOT NULL,
            `username` varchar(256) NOT NULL,
            PRIMARY KEY (`authID`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
            `gameID` int NOT NULL AUTO_INCREMENT,
            `whiteUsername` varchar(256),
            `blackUsername` varchar(256),
            `gameName` varchar(256),
            `game` text,
            PRIMARY KEY (`gameID`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws DataAccessException, SQLException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (DataAccessException error) {
            // throw? then service can catch exception?
        } catch (SQLException error) {

        }
    }
}
