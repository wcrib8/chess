package dataaccess;

import model.*;

import java.util.List;

public interface DataAccess {


    void clear();

    void createUser(UserData userData);

    String getUser(String username);

    void createGame(AuthData authData, GameData gameData);

    void getGame(AuthData authData, GameData gameData);

    List<GameData> listGames(AuthData authData);

    void updateGame(AuthData authData, GameData gameData);

    void createAuth(UserData userData);

    void getAuth(AuthData authData);

    void deleteAuth(AuthData authData);
}
