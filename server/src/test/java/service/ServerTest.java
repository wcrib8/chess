package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;
import service.exceptions.UnauthorizedException;
import service.requests.*;
import dataaccess.exceptions.*;
import service.responses.RegisterResult;
//import server.exceptions.*;


class ServerTest {
    private static Service service;
    private static MemoryDataAccess dataAccess;
    public RegisterRequest jojo = new RegisterRequest("Jojo", "jojo_is_cool", "jojo@jojo.com");
    public LoginRequest jojoLogin = new LoginRequest("Jojo", "jojo_is_cool");


    // instantiate objects for tests

    @BeforeEach
    public void setup() {
        dataAccess = new MemoryDataAccess();
        service = new Service(dataAccess);
    }

    // clear tests

    @Test
    public void clearRemovesUser() throws AlreadyTakenException {
        registerUserSuccessful();
        service.clear();
        assertEquals(0, dataAccess.numUsers());
    }

    // register tests

    @Test
    public void registerUserSuccessful() throws AlreadyTakenException {
        service.register(jojo);
        var jojoUserData = new UserData("Jojo", "jojo_is_cool", "jojo@jojo.com");
        assertEquals(jojoUserData, dataAccess.getUser("Jojo"));
    }

    @Test
    public void registerWithNoNameUnsuccessful() throws AlreadyTakenException {
        var jojoNoName = new RegisterRequest("", "jojo_is_cool", "jojo@jojo.com");
        service.register(jojoNoName);
        var jojoNoNameUserData = new UserData("", "jojo_is_cool", "jojo@jojo.com");
        assertNotEquals(jojoNoNameUserData, dataAccess.getUser("Jojo"));
    }

    @Test
    public void registerExistingUserUnsuccessful() throws AlreadyTakenException {
        var jojoNoName = new RegisterRequest("", "jojo_is_cool", "jojo@jojo.com");
        service.register(jojoNoName);
        assertThrows(AlreadyTakenException.class, () -> service.register(jojoNoName));
    }

    @Test
    public void registerUserCreatesAuthData() throws AlreadyTakenException {
        var jojo = new RegisterRequest("Jojo", "jojo_is_cool", "jojo@jojo.com");
        service.register(jojo);
        assertNotNull(dataAccess.getAuth("Jojo"));
    }

    // login tests

    @Test
    public void loginSuccessful() throws UnauthorizedException, AlreadyTakenException {
        registerUserSuccessful();
        var result = service.login(jojoLogin);
        assertEquals("Jojo", result.username()); // user
    }

    @Test
    public void loginNonexistentUserUnsuccessful() {
        assertThrows(UnauthorizedException.class, () -> service.login(jojoLogin));
    }

    @Test
    public void loginWithNoPasswordUnsuccessful() throws AlreadyTakenException {
        registerUserSuccessful();
        var loginRequest = new LoginRequest("Jojo", "");
        assertThrows(UnauthorizedException.class, () -> service.login(loginRequest));
    }

    @Test
    public void loginUnauthorizedUserUnsuccessful() throws AlreadyTakenException {
        registerUserSuccessful();
        dataAccess.deleteAuth(dataAccess.getAuth("Jojo"));
        var loginRequest = new LoginRequest("Jojo", "jojo_is_cool");
        assertThrows(UnauthorizedException.class, () -> service.login(loginRequest));
    }

    // logout tests

    @Test
    public void logoutSuccessful() throws AlreadyTakenException, UnauthorizedException {
        RegisterResult result = service.register(jojo);
        assertEquals(1, dataAccess.numAuthTokens());
        service.logout(new LogoutRequest(result.authToken()));
        assertEquals(0, dataAccess.numAuthTokens());
    }

    @Test
    public void logoutNonexistentUserUnsuccessful() {
        assertThrows(UnauthorizedException.class, () -> service.logout(new LogoutRequest("3442bdss7vd")));
    }

    @Test
    public void logoutUnauthorizedUserUnsuccessful() throws AlreadyTakenException, UnauthorizedException {
        RegisterResult result = service.register(jojo);
        assertEquals(1, dataAccess.numAuthTokens());
        service.logout(new LogoutRequest(result.authToken()));
        assertEquals(0, dataAccess.numAuthTokens());
        assertThrows(UnauthorizedException.class, () -> service.logout(new LogoutRequest(result.authToken())));
    }

    // list games tests

    @Test
    public void listGamesSuccessful() {

    }

    @Test
    public void listMultipleGamesSuccessful() {

    }

    @Test
    public void listGamesUnauthorizedUnsuccessful() {

    }

    @Test
    public void listGamesEmpty() {

    }

    @Test
    public void listGamesAfterLogoutUnsuccessful() {

    }

    // create game tests

    @Test
    public void createGameSuccessful() {

    }

    @Test
    public void createMultipleGamesSuccessful() {

    }

    @Test
    public void createGameUnauthorizedUnsuccessful() {

    }

    @Test
    public void createGameNoNameUnsuccessful() {

    }

    @Test
    public void createAlreadyExistingGameUnsuccessful() {

    }

    // join game tests

    @Test
    public void joinGameSuccessful() {

    }

    @Test
    public void joinMultipleGamesSuccessful() {

    }

    @Test
    public void joinUnauthorizedUnsuccessful() {

    }

    @Test
    public void joinNonexistentGameUnsuccessful() {

    }

    @Test
    public void joinFullGameUnsuccessful() {

    }
}