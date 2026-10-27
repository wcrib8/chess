package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryDataAccess implements DataAccess {
    private HashMap<String, UserData> users = new HashMap<>();
    private HashMap<String, AuthData> authorized = new HashMap<>();
    private ArrayList<GameData> games = new ArrayList<>();

    public MemoryDataAccess() {}
    // void insertUser(UserData u) throws DataAccessException
    // from masteryLS for reference

    public void clear() {
        users.clear();
        authorized.clear();
        games.clear();
    }

    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public UserData getUser(String username) {
        return users.get(username);
    }

    public int numUsers() {
        return users.size();
    }

    public void createGame(GameData gameData) {
        games.add(gameData);
    }

    public GameData getGame(int gameID) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    public ArrayList<GameData> listGames() {
        return games;
    }

    public void updateGame(int gameID, GameData gameData) {
        for (GameData game : games) {
            if (game.gameID() == gameID) {
                game = gameData;
                return;
            }
        }
    }

    public void createAuth(AuthData authData) {
        authorized.put(authData.authToken(), authData);
    }

    public AuthData getAuth(String authToken) {
        return authorized.get(authToken);
    }

    public void deleteAuth(AuthData authData) {
        authorized.remove(authData.authToken());
    }

    public int numAuthTokens() {return authorized.size();}
}
