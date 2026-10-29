package dataaccess;

import dataaccess.exceptions.*;
import model.*;

import java.util.ArrayList;

public class MySqlDataAccess implements DataAccess {

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

    private void configureDatabase() throws DataAccessException {

    }
}
