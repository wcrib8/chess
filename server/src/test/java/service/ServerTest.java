package service;

import chess.ChessGame;
import dataaccess.MemoryDataAccess;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import model.*;
import service.exceptions.BadRequestException;
import service.exceptions.UnauthorizedException;
import service.requests.*;
import dataaccess.exceptions.*;
import service.responses.RegisterResult;

class ServerTest {
    // instantiate objects for tests
    private static Service service;
    private static MemoryDataAccess dataAccess;
    public RegisterRequest jojo = new RegisterRequest("Jojo", "jojo_is_cool", "jojo@jojo.com");
    public LoginRequest jojoLogin = new LoginRequest("Jojo", "jojo_is_cool");

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
        var result = service.register(jojo);
        assertNotNull(dataAccess.getAuth(result.authToken()));
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
    public void listGamesSuccessful() throws AlreadyTakenException, UnauthorizedException {
        RegisterResult result = service.register(jojo);
        ListGamesRequest request = new ListGamesRequest(result.authToken());
        assertEquals(0, service.listGames(request).games().size());
        dataAccess.createGame(new GameData(4, null, null, "bob", new ChessGame()));
        assertEquals(1, service.listGames(request).games().size());
    }

    @Test
    public void listGamesUnauthorizedUnsuccessful() {
        RegisterResult result = new RegisterResult("Jojo", "234k234");
        ListGamesRequest request = new ListGamesRequest(result.authToken());
        assertThrows(UnauthorizedException.class, () -> service.listGames(request));
    }

    @Test
    public void listGamesAfterLogoutUnsuccessful() throws UnauthorizedException, AlreadyTakenException {
        RegisterResult result = service.register(jojo);
        ListGamesRequest request = new ListGamesRequest(result.authToken());
        dataAccess.createGame(new GameData(4, null, null, "bob", new ChessGame()));
        assertEquals(1, service.listGames(request).games().size());
        service.logout(new LogoutRequest(result.authToken()));
        assertThrows(UnauthorizedException.class, () -> service.listGames(request));
    }

    // create game tests

    @Test
    public void createGameSuccessful() throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        RegisterResult register = service.register(jojo);
        CreateGameRequest request = new CreateGameRequest(register.authToken(), "war");
        var result = service.createGame(request);
        assertEquals(1, result.gameID());
        CreateGameRequest request2 = new CreateGameRequest(register.authToken(), "battle");
        var result2 = service.createGame(request2);
        assertEquals(2, result2.gameID());
    }

    @Test
    public void createGameUnauthorizedUnsuccessful() {
        CreateGameRequest request = new CreateGameRequest("fakeAuthToken", "war");
        assertThrows(UnauthorizedException.class, () -> service.createGame(request));
    }

    @Test
    public void createGameNoNameUnsuccessful() throws AlreadyTakenException {
        RegisterResult register = service.register(jojo);
        CreateGameRequest request = new CreateGameRequest(register.authToken(), "");
        assertThrows(BadRequestException.class, () -> service.createGame(request));
    }

    // join game tests

    @Test
    public void joinGameSuccessful() throws AlreadyTakenException, UnauthorizedException, BadRequestException {
        RegisterResult register = service.register(jojo);
        CreateGameRequest request = new CreateGameRequest(register.authToken(), "war");
        service.createGame(request);
        assertNull(dataAccess.getGame(1).whiteUsername());
        service.joinGame(new JoinGameRequest(register.authToken(), "WHITE", 1));
        assertNotEquals(null, dataAccess.getGame(1).whiteUsername());
    }

    @Test
    public void joinUnauthorizedUnsuccessful() {
        JoinGameRequest request = new JoinGameRequest("fakeAuthToken", "WHITE", 1);
        assertThrows(UnauthorizedException.class, () -> service.joinGame(request));
    }

    @Test
    public void joinNonexistentGameUnsuccessful() throws AlreadyTakenException {
        RegisterResult register = service.register(jojo);
        assertThrows(BadRequestException.class, () -> service.joinGame(new JoinGameRequest(register.authToken(), "WHITE", 1)));
    }

    @Test
    public void joinFullGameUnsuccessful() throws UnauthorizedException, BadRequestException, AlreadyTakenException {
        RegisterResult register = service.register(jojo);
        CreateGameRequest request = new CreateGameRequest(register.authToken(), "war");
        service.createGame(request);
        service.joinGame(new JoinGameRequest(register.authToken(), "WHITE", 1));
        service.joinGame(new JoinGameRequest(register.authToken(), "BLACK", 1));
        assertThrows(AlreadyTakenException.class, () -> service.joinGame(new JoinGameRequest(register.authToken(), "WHITE", 1)));
        assertThrows(AlreadyTakenException.class, () -> service.joinGame(new JoinGameRequest(register.authToken(), "BLACK", 1)));

    }
}