package client;

import client.exceptions.InvalidInputException;
import model.requests.*;
import model.responses.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import server.Server;
import server.ServerFacade;
import server.exceptions.UnauthorizedException;

import java.lang.reflect.Array;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws Exception {
        facade.clear();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void registerSuccess() {
        assertDoesNotThrow(() -> facade.register(new RegisterRequest("wolf", "wolf", "wolf")));
        //server.service.dataAccess.numUsers()
    }

    @Test
    public String loginSuccess() throws Exception {
        registerSuccess();
        LoginResult result = facade.login(new LoginRequest("wolf", "wolf"));
        assertNotNull(result);
        return result.authToken();
    }

    @Test
    public void logoutSuccess() throws Exception {
        String auth = loginSuccess();
        assertDoesNotThrow(() -> facade.logout(new LogoutRequest(auth)));
    }

    @Test
    public void createGameSuccess() throws Exception {
        String auth = loginSuccess();
        assertDoesNotThrow(() -> facade.createGame(new CreateGameRequest(auth, "fun")));
    }

    @Test
    public void listGamesSuccess() throws Exception {
        String auth = loginSuccess();
        assertDoesNotThrow(() -> facade.listGames(new ListGamesRequest(auth)));
        ListGamesResult result1 = facade.listGames(new ListGamesRequest(auth));
        facade.createGame(new CreateGameRequest(auth, "fun"));
        ListGamesResult result2 = facade.listGames(new ListGamesRequest(auth));
        assertTrue(result1.games().size() < result2.games().size());
    }

    @Test
    public void listGamesUnsuccessful() {
        assertThrows(Exception.class, () -> facade.listGames(new ListGamesRequest("badAuthToken")));
    }

    @Test
    public void joinGameSuccess() throws Exception {
        String auth = loginSuccess();
        //assertThrows(Exception.class, () -> facade.joinGame(new JoinGameRequest(auth, "white", 1)));
        facade.createGame(new CreateGameRequest(auth, "fun"));
        assertDoesNotThrow(() -> facade.joinGame(new JoinGameRequest(auth, "white", 1)));
    }

}
