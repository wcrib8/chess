package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MemoryDataAccess implements DataAccess {
    private HashMap<String, UserData> users = new HashMap<>();
    private HashMap<String, AuthData> authorized = new HashMap<>();
    private ArrayList<HashMap<String, String>> games = new ArrayList<>();

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

    public void createGame(AuthData authData, GameData gameData) {

    }

    public void getGame(AuthData authData, GameData gameData) {

    }

    public List<GameData> listGames(AuthData authData) {
        return null;
    }

    public void updateGame(AuthData authData, GameData gameData) {

    }

    public void createAuth(AuthData authData) {
        authorized.put(authData.username(), authData);
    }

    public AuthData getAuth(String username) {
        return authorized.get(username);
    }

    public void deleteAuth(AuthData authData) {
        authorized.remove(authData.username()   );
    }

    // more methods?gf
}
