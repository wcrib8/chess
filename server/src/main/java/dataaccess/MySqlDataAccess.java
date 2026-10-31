package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.exceptions.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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

    public void clear() {
        for (String table : tableNames) {
            var statement = "TRUNCATE " + table;
            try {
                executeUpdate(statement);
            } catch (DataAccessException e) {
                throw new RuntimeException(e); // temp fix
            }
        }
    }

    public void createUser(UserData userData) {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String json = new Gson().toJson(userData);
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        try {
            int id = executeUpdate(statement, userData.username(), hashedPassword, userData.email(), json);
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // temp fix
        }
    }

    public UserData getUser(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password FROM users WHERE username=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"));
                    }
                }
            }
        } catch (Exception error) {
            throw new RuntimeException(); // for now
        }
        return null;
    }

    public void createGame(GameData gameData) {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        String json = new Gson().toJson(gameData);
        try {
            executeUpdate(
                    statement,
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    gameData.game(),
                    json);
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // temp fix
        }
    }

    public GameData getGame(int gameID) {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                (ChessGame) rs.getObject("game"));
                    }
                }
            }
        } catch (Exception error) {
            throw new RuntimeException(); // temp
        }
        return null;
    }

    public ArrayList<GameData> listGames() {
        var statement = "SELECT";
        return null;
    }

    public void updateGame(GameData gameData, String playerColor, String username) {
    }

    public void createAuth(AuthData authData) {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        String json = new Gson().toJson(authData);
        try {
            executeUpdate(statement, authData.username(), authData.authToken(), json);
        } catch (DataAccessException error) {
            throw new RuntimeException(error); // temp fix
        }
    }

    public AuthData getAuth(String username) {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM auth WHERE username=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(
                                rs.getString("authToken"),
                                rs.getString("username"));
                    }
                }
            }
        } catch (Exception error) {
            throw new RuntimeException(error); // temp fix
        }

        return null;
    }

    public void deleteAuth(AuthData authData) {
        var statement = "DELETE FROM auth WHERE username=?";
        try {
            executeUpdate(statement, authData.username());
        } catch (DataAccessException e) {
            throw new RuntimeException(e); // temp fix
        }
    }



    void storeUserPassword(String username, String clearTextPassword) {
        String hashedPassword = BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());

        // Write the hashed password to the database along with the user's other information
        //writeHashedPasswordToDatabase(username, hashedPassword);
    }

//    boolean verifyUser(String username, String providedClearTextPassword) {
//        // Read the previously hashed password from the database
//        var hashedPassword = readHashedPasswordFromDatabase(username);
//
//        return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
//    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: ", error);
        } catch (DataAccessException error) {
            // what to do
            throw new RuntimeException(); // for now
        }
    }

    private final String[] tableNames = {
            "users", "auth", "games"
    };

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
