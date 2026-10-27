package dataaccess;

import model.*;

import java.util.ArrayList;

public interface DataAccess {


    void clear();

    void createUser(UserData userData);

    UserData getUser(String username);

    void createGame(GameData gameData);

    GameData getGame(int gameID);

    ArrayList<GameData> listGames();

    void updateGame(GameData gameData, String playerColor, String username);

    void createAuth(AuthData authData);

    AuthData getAuth(String username);

    void deleteAuth(AuthData authData);
}
