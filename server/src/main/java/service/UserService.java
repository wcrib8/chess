package service;

import service.requests.*;
import service.responses.*;

public class UserService {
    // Each service method receives a Request object containing all the information it needs to do its work
    public RegisterResult register(RegisterRequest registerRequest) {}
    public LoginResult login(LoginRequest loginRequest) {}
    public void logout(LogoutRequest logoutRequest) {}

    // when service fails, have error handling, throw exception
}
