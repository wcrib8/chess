package dataaccess;

import model.*;

import java.util.List;

public interface DataAccess {


    void clear();

    void createUser(UserData userData);

    UserData getUser(String username);

    void createGame(AuthData authData, GameData gameData);

    void getGame(AuthData authData, GameData gameData);

    List<GameData> listGames(AuthData authData);

    void updateGame(AuthData authData, GameData gameData);

    void createAuth(AuthData authData);

    AuthData getAuth(String username);

    void deleteAuth(AuthData authData);
}
