package dataaccess;

import model.UserData;
import model.AuthData;
import java.util.HashMap;

public class MemoryDataAccess {
    private HashMap<String, UserData> users = new HashMap<>();


    // void insertUser(UserData u) throws DataAccessException
    // from masteryLS for reference

    @Override
    public void clear() {

    }

    @Override
    public void createUser(UserData userData, AuthData authData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void getUser(String username) {

    }

    public void createGame(String username) {

    }

    public void getGame(String username) {

    }

    public void listGames(String username) {

    }

    public void updateGame(String username) {

    }

    public void createAuth(String username) {

    }

    public void getAuth(String username) {

    }

    public void deleteAuth(String username) {

    }

    // more methods?gf
}
