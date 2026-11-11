package server;

import model.*;
import service.requests.*;
import service.responses.*;

public class ServerFacade {

    // http connection and communication with server
    // Opening a connection to the server.
    // Serializing the request body to JSON.
    // Setting the correct HTTP method (POST, GET, etc.).
    // Parsing the response.
    // Mapping HTTP error codes (like 401 or 403) into meaningful client-side exceptions.
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() {}

    public RegisterResult register(RegisterRequest request) {
        return null;
    }

    public LoginResult login(LoginRequest request) {
        return null;
    }

    public void logout(LogoutRequest request) {}

    public CreateGameResult createGame(CreateGameRequest request) {
        return null;
    }

    public void joinGame(JoinGameRequest request) {}

    public ListGamesResult listGames(ListGamesRequest request) {
        return null;
    }
}
