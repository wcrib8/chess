package service;

import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import model.*;
import service.requests.RegisterRequest;
import dataaccess.exceptions.*;
//import server.exceptions.*;
import service.exceptions.*;

class ServerTest {
    private static Service service;
    private static MemoryDataAccess MemoryDataAccess;

    // instantiate objects for tests

    @BeforeEach
    public void setup() {
        MemoryDataAccess = new MemoryDataAccess();
        service = new Service(MemoryDataAccess);
    }

    // clear tests

    @Test
    public void clearReturnsEmptyDatabase() {

    }

    @Test
    public void clearRemovesUser() {
        var John = new RegisterRequest("John", "johniscool", "john@john.com");
        service.register(John);
        //assert
    }

    // register tests

    @Test
    public void registerUserSuccessful() {
        var jojo = new RegisterRequest("Jojo", "jojoiscool", "jojo@jojo.com");
        service.register(jojo);
        var jojoUserData = new UserData("Jojo", "jojoiscool", "jojo@jojo.com");
        assertEquals(jojoUserData, MemoryDataAccess.getUser("Jojo"));
    }

    @Test
    public void registerWithNoNameUnsuccessful() {
        var jojoNoName = new RegisterRequest("", "jojoiscool", "jojo@jojo.com");
        service.register(jojoNoName);
        var jojoNoNameUserData = new UserData("", "jojoiscool", "jojo@jojo.com");
        assertNotEquals(jojoNoNameUserData, MemoryDataAccess.getUser("Jojo"));
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
        assertNotNull(MemoryDataAccess.getAuth("Jojo"));
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