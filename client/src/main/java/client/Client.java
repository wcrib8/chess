package client;

import server.ServerFacade;

import java.util.Arrays;

public class Client {

    // handle prelogin and postlogin states

    // prelogin ui:
    // handle help, quit, login, and register

    // postlogin ui:
    // handle help, logout, create game, list games, play game, and observe game

    private final ServerFacade server;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {

    }

    public void evaluatePrompt(String prompt) {
        try {
            String[] tokens = prompt.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : prompt;
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (command) {
                case "help" -> help();
                case "quit" -> quit();
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame();
                case "list" -> listGames();
                case "play" -> playGame();
                case "observe" -> observeGame();
            }
        } catch(Exception error) {
            // call to repl?
        }
    }

    private void help() {

    }

    private void quit() {

    }

    private void register(String[] params) {

    }

    private void login(String[] params) {

    }
}
