package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;
import service.exceptions.UnauthorizedException;
import service.requests.*;
import dataaccess.exceptions.*;
//import server.exceptions.*;


class ServerTest {
    private static Service service;
    private static MemoryDataAccess memoryDataAccess;
    public RegisterRequest jojo = new RegisterRequest("Jojo", "jojo_is_cool", "jojo@jojo.com");
    public LoginRequest jojoLogin = new LoginRequest("Jojo", "jojo_is_cool");


    // instantiate objects for tests

    @BeforeEach
    public void setup() {
        memoryDataAccess = new MemoryDataAccess();
        service = new Service(memoryDataAccess);
    }

    // clear tests

    @Test
    public void clearRemovesUser() throws AlreadyTakenException {
        registerUserSuccessful();
        service.clear();
        assertEquals(0, memoryDataAccess.numUsers());
    }

    // register tests

    @Test
    public void registerUserSuccessful() throws AlreadyTakenException {
        service.register(jojo);
        var jojoUserData = new UserData("Jojo", "jojo_is_cool", "jojo@jojo.com");
        assertEquals(jojoUserData, memoryDataAccess.getUser("Jojo"));
    }

    @Test
    public void registerWithNoNameUnsuccessful() throws AlreadyTakenException {
        var jojoNoName = new RegisterRequest("", "jojo_is_cool", "jojo@jojo.com");
        service.register(jojoNoName);
        var jojoNoNameUserData = new UserData("", "jojo_is_cool", "jojo@jojo.com");
        assertNotEquals(jojoNoNameUserData, memoryDataAccess.getUser("Jojo"));
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
        assertNotNull(memoryDataAccess.getAuth("Jojo"));
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
        memoryDataAccess.deleteAuth(memoryDataAccess.getAuth("Jojo"));
        var loginRequest = new LoginRequest("Jojo", "jojo_is_cool");
        assertThrows(UnauthorizedException.class, () -> service.login(loginRequest));
    }

    // logout tests

    @Test
    public void logoutSuccessful() {

    }

    @Test
    public void logoutNonexistentUserUnsuccessful() {

    }

    @Test
    public void logoutUnauthorizedUserUnsuccessful() {

    }

    @Test
    public void logoutAlreadyLoggedOutUserUnsuccessful() {

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