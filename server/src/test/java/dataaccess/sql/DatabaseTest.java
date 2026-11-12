package dataaccess.sql;

import chess.ChessGame;
import model.data.AuthData;
import model.data.GameData;
import model.data.UserData;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import dataaccess.exceptions.*;
import java.sql.SQLException;


class DatabaseTest {
    // setup objects
    private static MySqlDataAccess dataAccess;
    UserData jojo = new UserData("Jojo", "iLikeJojo", "jojo@jojo");
    AuthData jojoAuth = new AuthData("Jojo", "coolAuthToken");
    GameData egypt = new GameData(1, null, null, "battle", new ChessGame());
    GameData america = new GameData(2, null, null, "war", new ChessGame());


    @BeforeAll
    public static void configure() {
        try {
            dataAccess = new MySqlDataAccess();
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        dataAccess.clear();
    }

    @Test
    public void clear() throws DataAccessException {
        assertEquals(0, dataAccess.numUsers());
        createUser();
        assertEquals(1, dataAccess.numUsers());
        dataAccess.clear();
        assertEquals(0, dataAccess.numUsers());
    }

    @Test
    public void createUser() throws DataAccessException {
        dataAccess.createUser(jojo);
        assertEquals(1, dataAccess.numUsers());
    }

    @Test
    public void getUser() throws DataAccessException {
        dataAccess.createUser(jojo);
        assertEquals("Jojo", dataAccess.getUser("Jojo").username());
        assertNotEquals("iLikeJojo", dataAccess.getUser("Jojo").password());
    }

    @Test
    public void createAuth() throws DataAccessException {
        assertThrows(DataAccessException.class, () -> dataAccess.createAuth(jojoAuth));
        dataAccess.createUser(jojo);
        dataAccess.createAuth(jojoAuth);
        assertEquals(1, dataAccess.numAuth());
    }

    @Test
    public void getAuth() throws DataAccessException {
        dataAccess.createUser(jojo);
        dataAccess.createAuth(jojoAuth);
        assertDoesNotThrow(() -> dataAccess.getAuth(jojoAuth.authToken()));
    }

    @Test
    public void deleteAuth() throws DataAccessException {
        createAuth();
        dataAccess.deleteAuth(jojoAuth);
        assertEquals(0, dataAccess.numAuth());
    }

    @Test
    public void createGame() {
        assertDoesNotThrow(() -> dataAccess.createGame(egypt));
    }

    @Test
    public void getGame() throws DataAccessException {
        dataAccess.createGame(egypt);
        assertDoesNotThrow(() -> dataAccess.getGame(1));
        assertNull(dataAccess.getGame(0));
    }

    @Test
    public void listGames() throws DataAccessException {
        dataAccess.createGame(egypt);
        assertThrows(DataAccessException.class, () -> dataAccess.createGame(egypt));
        dataAccess.createGame(america);
        assertEquals(2, dataAccess.listGames().size());
    }

    @Test
    public void updateGame() throws DataAccessException {
        dataAccess.createGame(egypt);
        assertThrows(DataAccessException.class, () -> dataAccess.updateGame(egypt, "WHITE", "Jojo"));
        createAuth();
        assertDoesNotThrow(() -> dataAccess.updateGame(egypt, "WHITE", "Jojo"));
    }

}
