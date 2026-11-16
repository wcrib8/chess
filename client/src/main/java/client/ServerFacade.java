package client;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import model.requests.*;
import model.responses.*;


public class ServerFacade {

    // Mapping HTTP error codes (like 401 or 403) into meaningful client-side exceptions.

    private final String serverUrl;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws Exception {
        var request = buildRequest("DELETE", "/db", null, null);
        sendRequest(request);
    }


    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        var request = buildRequest("POST", "/user", registerRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class, "register");
    }

    public LoginResult login(LoginRequest loginRequest) throws Exception {
        var request = buildRequest("POST", "/session", loginRequest, null);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class, "login");
    }

    public void logout(LogoutRequest logoutRequest) throws Exception {
        var request = buildRequest("DELETE", "/session", logoutRequest, logoutRequest.authToken());
        sendRequest(request);
        // should handle....?
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws Exception {
        var request = buildRequest("POST", "/game", createGameRequest, createGameRequest.authToken());
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class, "create");
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws Exception {
        var request = buildRequest("PUT", "/game", joinGameRequest, joinGameRequest.authToken());
        var response = sendRequest(request);
        handleResponse(response, null, "join");
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws Exception {
        var request = buildRequest("GET", "/game", listGamesRequest, listGamesRequest.authToken());
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class, "list");
    }

    private HttpRequest buildRequest(String method, String path, Object body, String authToken) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        if (authToken != null) {
            request.setHeader("authorization", authToken);
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws Exception {
        try {
            return httpClient.send(request, BodyHandlers.ofString());
        //} catch(UnauthorizedException error) {

        } catch(Exception error) {
            throw new Exception(error.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass, String action) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            //var body = response.body();
            if (status == 400) {
                if (action.equals("join")) {
                    throw new Exception("Error: you must've tried joining a team other than 'white' or 'black', or that game" +
                            " doesn't exist, try again!");
                }
                throw new Exception("Error: you must've tried giving a blank input, try again!");
            } if (status == 401) {
                if (action.equals("login")) {
                    throw new Exception("Error: the user you tried logging in with is not registered! do that first");
                }
                throw new Exception("Error: you're not logged in or registered! do that first");
            } if (status == 403) {
                switch (action) {
                    case "register" ->
                            throw new Exception("Error: looks like the username and password you entered is already taken, try again!");
                    case "create" ->
                            throw new Exception("Error: looks like the game you tried to create already exists!");
                    case "join" ->
                            throw new Exception("Error: looks like the team you entered is already taken in that game!");
                    default ->
                            throw new Exception("Error: looks like the option you entered is already taken, try another!");
                }
            }
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }
        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
