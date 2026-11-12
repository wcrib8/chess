package server;

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


    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) throws Exception {
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        return handleResponse(response, LoginResult.class);
    }

    public void logout(LogoutRequest logoutRequest) throws Exception {
        var request = buildRequest("DELETE", "/session", logoutRequest);
        sendRequest(request);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws Exception {
        var request = buildRequest("POST", "/game", createGameRequest);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws Exception {
        var request = buildRequest("PUT", "/game", joinGameRequest);
        sendRequest(request);
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws Exception {
        var request = buildRequest("GET", "/game", listGamesRequest);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class);
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
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
        } catch (Exception error) {
            throw new Exception(error.getMessage()); // change...?
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws Exception {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw new Exception(body); // change
            }

            throw new Exception(); // change
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
