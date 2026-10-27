package dataaccess;

import model.*;

import java.util.ArrayList;
import java.util.List;

public interface DataAccess {


    void clear();

    void createUser(UserData userData);

    UserData getUser(String username);

    void createGame(GameData gameData);

    GameData getGame(int gameID);

    ArrayList<GameData> listGames();

    void updateGame(int gameID, GameData gameData);

    void createAuth(AuthData authData);

    AuthData getAuth(String username);

    void deleteAuth(AuthData authData);
}
