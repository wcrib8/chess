package client;

import model.requests.*;
import model.responses.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import server.Server;
import server.ServerFacade;

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

//    @BeforeEach
//    static void clear() {
//        server.clear();
//    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    public void registerSuccess() {
        //var response = client.register(new Array["wolf", "wolf", "wolf"]);
        assertDoesNotThrow(() -> facade.register(new RegisterRequest("wolf", "wolf", "wolf")));
    }

}
