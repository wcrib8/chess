package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.*;

import java.util.ArrayList;

public interface DataAccess {


    void clear() throws DataAccessException;

    void createUser(UserData userData) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    void updateGame(GameData gameData, String playerColor, String username) throws DataAccessException;

    void createAuth(AuthData authData) throws DataAccessException;

    AuthData getAuth(String username) throws DataAccessException;

    void deleteAuth(AuthData authData) throws DataAccessException;
}
