package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DatabaseManager;
import dataaccess.exceptions.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlDataAccess implements DataAccess {

    public MySqlDataAccess() throws DataAccessException, SQLException {
        configureDatabase();
    }

    public void clear() throws DataAccessException {
        try {
            executeUpdate("TRUNCATE auth");
            executeUpdate("TRUNCATE games");
            executeUpdate("DELETE FROM users");
            executeUpdate("ALTER TABLE games AUTO_INCREMENT = 1");
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        try {
            executeUpdate(statement, userData.username(), hashedPassword, userData.email());
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public UserData getUser(String username) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM users WHERE username=?";
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
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
        return null;
    }

    public int numUsers() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT COUNT(*) AS userCount FROM users";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("userCount");
                    }
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
        return -1;
    }

    public void createGame(GameData gameData) throws DataAccessException {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        String json = new Gson().toJson(gameData.game());
        try {
            executeUpdate(
                    statement,
                    gameData.gameID(),
                    gameData.whiteUsername(),
                    gameData.blackUsername(),
                    gameData.gameName(),
                    json);
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        //var chessGame =
                        return new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class));
                    }
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
        return null;
    }

    public ArrayList<GameData> listGames() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            ArrayList<GameData> gamesList = new ArrayList<>();
            var statement = "SELECT * FROM games";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        gamesList.add( new GameData(
                                rs.getInt("gameID"),
                                rs.getString("whiteUsername"),
                                rs.getString("blackUsername"),
                                rs.getString("gameName"),
                                new Gson().fromJson(rs.getString("game"), ChessGame.class)
                        ));
                    }
                }
            }
            return gamesList;
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException {
        try {
            String statement;
            if (playerColor.equals("WHITE")) {
                statement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
            } else {
                statement = "UPDATE games SET blackUsername=? WHERE gameID=?";
            }
            executeUpdate(statement, username, gameData.gameID());
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public void createAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (authToken, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, authData.authToken(), authData.username());
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth WHERE authToken=?";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(
                                rs.getString("username"),
                                rs.getString("authToken"));
                    }
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }

        return null;
    }

    public void deleteAuth(AuthData authData) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        try {
            executeUpdate(statement, authData.authToken());
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    public int numAuth() throws DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            var statement = "SELECT COUNT(*) AS authCount FROM auth";
            try (PreparedStatement ps = connection.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("authCount");
                    }
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
        return -1;
    }

    private void executeUpdate(String statement, Object... params) throws SQLException, DataAccessException {
        try (Connection connection = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = connection.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                iterateParameters(params, ps);
                ps.executeUpdate();
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }

    private static void iterateParameters(Object[] params, PreparedStatement ps) throws SQLException {
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
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            `username` VARCHAR(256) NOT NULL UNIQUE,
            `password` CHAR(60) NOT NULL,
            `email` VARCHAR(256) NOT NULL,
            PRIMARY KEY (`username`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS auth (
            `authToken` VARCHAR(256) NOT NULL,
            `username` VARCHAR(256) NOT NULL,
            PRIMARY KEY (`authToken`),
            FOREIGN KEY (`username`) REFERENCES users(`username`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS games (
            `gameID` INT NOT NULL AUTO_INCREMENT,
            `whiteUsername` VARCHAR(256),
            `blackUsername` VARCHAR(256),
            `gameName` VARCHAR(256),
            `game` TEXT,
            PRIMARY KEY (`gameID`),
            FOREIGN KEY (`whiteUsername`) REFERENCES users(`username`),
            FOREIGN KEY (`blackUsername`) REFERENCES users(`username`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection connection = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = connection.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException error) {
            throw new DataAccessException("Error: failed to connect to database. ", error);
        }
    }
}
