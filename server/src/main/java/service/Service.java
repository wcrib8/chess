package service;

import chess.ChessGame;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.DataAccess;
import model.*;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;
import service.requests.*;
import service.responses.*;

import java.util.UUID;

public class Service {

    private final DataAccess dataAccess;
    private int gameIndex = 0;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public RegisterResult register(RegisterRequest request) throws AlreadyTakenException {
        // connect to data access, check if the name is already taken
        if (dataAccess.getUser(request.username()) != null) {
            throw new AlreadyTakenException("Error: already taken");
        }

        // create user and create auth for new player
        dataAccess.createUser(new UserData(request.username(), request.password(), request.email()));
        var authToken = generateToken();
        dataAccess.createAuth(new AuthData(request.username(), authToken));
        return new RegisterResult(request.username(), authToken);
    }

    public LoginResult login(LoginRequest request) throws UnauthorizedException {
        // if no user with this info, send error
        UserData user = dataAccess.getUser(request.username());
        if (user == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // check username and password matches
        if (!request.username().equals(user.username()) || !request.password().equals(user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        // create new authData
        AuthData authData = new AuthData(user.username(), generateToken());
        dataAccess.createAuth(authData);

        return new LoginResult(request.username(), authData.authToken());
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

    public CreateGameResult createGame(CreateGameRequest request) throws UnauthorizedException, BadRequestException {
        if (authorized(request.authToken())) {
            if (request.gameName() == null || request.gameName().isEmpty()) {
                throw new BadRequestException("Error: bad request");
            }

            // create new game
            gameIndex++;
            dataAccess.createGame(new GameData(gameIndex, null, null, request.gameName(), new ChessGame()));

            return new CreateGameResult(gameIndex);
        }
        return null;
    }

    public void joinGame(JoinGameRequest request) throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        if (authorized(request.authToken())) {
            var game = dataAccess.getGame(request.gameID());

            // check game exists, then check playerColor is white or black
            String color = request.playerColor();
            if (game == null || color == null || color.isEmpty() || (!color.equals("WHITE") && !color.equals("BLACK"))) {
                throw new BadRequestException("Error: bad request");
            }

            // add to game, throw if that color is already taken
            if (color.equals("WHITE")) {
                if (game.whiteUsername() != null) {
                    throw new AlreadyTakenException("Error: already taken");
                }
            } else if (game.blackUsername() != null) {
                throw new AlreadyTakenException("Error: already taken");
            }

            dataAccess.updateGame(game, color, dataAccess.getAuth(request.authToken()).username());
        }
    }

    public void clear() {
        dataAccess.clear();
    }

    @SuppressWarnings("SameReturnValue")
    public boolean authorized(String authToken) throws UnauthorizedException {
        if (dataAccess.getAuth(authToken) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }
        return true;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
