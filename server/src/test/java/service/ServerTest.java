package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;
import service.requests.RegisterRequest;
import dataaccess.exceptions.*;
//import server.exceptions.*;


class ServerTest {
    private static Service service;
    private static MemoryDataAccess memoryDataAccess;

    // instantiate objects for tests

    @BeforeEach
    public void setup() {
        memoryDataAccess = new MemoryDataAccess();
        service = new Service(memoryDataAccess);
    }

    // clear tests

    @Test
    public void clearRemovesUser() {
        registerUserSuccessful();
        service.clear();
        assertEquals(0, memoryDataAccess.numUsers());
    }

    // register tests

    @Test
    public void registerUserSuccessful() {
        var jojo = new RegisterRequest("Jojo", "jojoiscool", "jojo@jojo.com");
        service.register(jojo);
        var jojoUserData = new UserData("Jojo", "jojoiscool", "jojo@jojo.com");
        assertEquals(jojoUserData, memoryDataAccess.getUser("Jojo"));
    }

    @Test
    public void registerWithNoNameUnsuccessful() {
        var jojoNoName = new RegisterRequest("", "jojoiscool", "jojo@jojo.com");
        service.register(jojoNoName);
        var jojoNoNameUserData = new UserData("", "jojoiscool", "jojo@jojo.com");
        assertNotEquals(jojoNoNameUserData, memoryDataAccess.getUser("Jojo"));
    }

    @Test
    public void registerExistingUserUnsuccessful() {
        var jojoNoName = new RegisterRequest("", "jojoiscool", "jojo@jojo.com");
        service.register(jojoNoName);
        assertThrows(AlreadyTakenException.class, () -> {
            service.register(jojoNoName);
        });
    }

    @Test
    public void registerUserCreatesAuthData() {
        var jojo = new RegisterRequest("Jojo", "jojoiscool", "jojo@jojo.com");
        service.register(jojo);
        assertNotNull(memoryDataAccess.getAuth("Jojo"));
    }

    // login tests

    @Test
    public void loginSuccessful() {

    }

    @Test
    public void loginNonexistentUserUnsuccessful() {

    }

    @Test
    public void loginWithNoPasswordUnsuccessful() {

    }

    @Test
    public void loginUnauthorizedUserUnsuccessful() {

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