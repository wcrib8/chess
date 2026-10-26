package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;

public interface DataAccess {


    void clear();

    void createUser(UserData userData);

    UserData getUser(String username);

    void createGame(GameData gameData);

    void getGame(GameData gameData);

    ArrayList<GameData> listGames();

    void updateGame(GameData gameData);

    void createAuth(AuthData authData);

    AuthData getAuth(String username);

    void deleteAuth(AuthData authData);
}
