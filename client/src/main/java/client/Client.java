package client;

import server.ServerFacade;

public class Client {

    // handle prelogin and postlogin states

    // prelogin ui:
    // handle help, quit, login, and register

    // postlogin ui:
    // handle help, logout, create game, list games, play game, and observe game

    private final ServerFacade server;
    private State state = State.SIGNED_OUT;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {

    }
}
