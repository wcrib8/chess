package service;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.DataAccess;
import model.*;
import service.exceptions.UnauthorizedException;
import service.requests.*;
import service.responses.*;

import java.util.UUID;

public class Service {

    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    // Each service method receives a Request object containing all the information it needs to do its work

    public RegisterResult register(RegisterRequest registerRequest) throws AlreadyTakenException {
        // connect to data access, check if the name is already taken
        if (dataAccess.getUser(registerRequest.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        // create user and create auth for new player
        dataAccess.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        var authToken = generateToken();
        dataAccess.createAuth(new AuthData(registerRequest.username(), authToken));
        return new RegisterResult(registerRequest.username(), authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws UnauthorizedException {
        // if no user with this info, send error
        UserData user = dataAccess.getUser(loginRequest.username());
        if (user == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // check username and password matches
        if (!loginRequest.username().equals(user.username()) | !loginRequest.password().equals(user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // create new authData
        AuthData authData = new AuthData(user.username(), generateToken());
        dataAccess.createAuth(authData);

        return new LoginResult(loginRequest.username(), authData.authToken());
    }

    public void logout(LogoutRequest request) throws UnauthorizedException {
        if (authorized(request.authToken())) {
            dataAccess.deleteAuth(dataAccess.getAuth(request.authToken()));
        }
    }

    public ListGamesResult listGames(ListGamesRequest request) throws UnauthorizedException {
        if (authorized(request.authToken())) {
            return new ListGamesResult(dataAccess.listGames());
        }
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws UnauthorizedException {
        return null;

        // should also check if game already in db
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws UnauthorizedException {}

    public void clear() {
        dataAccess.clear();
    }

    public boolean authorized(String authToken) throws UnauthorizedException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return true;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // when service fails, have error handling, throw exception
}
