package service;

import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.DataAccess;
import model.*;
import service.exceptions.UnauthorizedException;
import service.requests.*;
import service.responses.*;

import java.util.UUID;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
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
        if (dataAccess.getUser(loginRequest.username()) == null) {
            throw new UnauthorizedException("Error: unauthorized");
        }

        return null;
    }

    public void logout(LogoutRequest logoutRequest) {

    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    // when service fails, have error handling, throw exception
}
