package service;

import dataaccess.DataAccess;
import service.requests.*;
import service.responses.*;

public class UserService {

    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    // Each service method receives a Request object containing all the information it needs to do its work
    public RegisterResult register(RegisterRequest registerRequest) {
        // connect to data access
        if (dataAccess.getUser(registerRequest.username()) != null) {

        }

        // check for errors - already taken name,

        // set user in db

        // return result


        return null;
    }
    public LoginResult login(LoginRequest loginRequest) {
        return null;
    }
    public void logout(LogoutRequest logoutRequest) {}

    // when service fails, have error handling, throw exception
}
