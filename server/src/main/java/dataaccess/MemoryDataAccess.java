package dataaccess;

import model.UserData;
import model.AuthData;
import java.util.HashMap;

public class MemoryDataAccess {
    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public void clear() {

    }

    @Override
    public void register(UserData userData, AuthData authData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void getUser(String username) {

    }
}
