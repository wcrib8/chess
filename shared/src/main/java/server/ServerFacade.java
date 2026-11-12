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

    // http connection and communication with server
    // Opening a connection to the server.
    // Serializing the request body to JSON.
    // Setting the correct HTTP method (POST, GET, etc.).
    // Parsing the response.
    // Mapping HTTP error codes (like 401 or 403) into meaningful client-side exceptions.

    private final String serverUrl;
    // HTTP Client for making requests
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws Exception {
        var request = buildRequest("DELETE", "/db", null);
        sendRequest(request);
    }

    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResult.class);
    }

//    public LoginResult login(LoginRequest request) {
//        return null;
//    }

//    public void logout(LogoutRequest request) {}

//    public CreateGameResult createGame(CreateGameRequest request) {
//        return null;
//    }

//    public void joinGame(JoinGameRequest request) {}

//    public ListGamesResult listGames(ListGamesRequest request) {
//        return null;
//    }

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
