package dataaccess;

import model.*;
import java.util.HashMap;
import java.util.List;

public class MemoryDataAccess implements DataAccess {
    private HashMap<String, UserData> users = new HashMap<>();

    public MemoryDataAccess() {}
    // void insertUser(UserData u) throws DataAccessException
    // from masteryLS for reference

    public void clear() {

    }

    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    public String getUser(String username) {
        return "";
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

    public void createAuth(UserData userData) {

    }

    public void getAuth(AuthData authData) {

    }

    public void deleteAuth(AuthData authData) {

    }

    // more methods?gf
}
