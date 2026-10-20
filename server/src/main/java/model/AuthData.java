package model;

import java.util.UUID;

public class AuthData {

    private final Object authToken;
    private final Object username;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
