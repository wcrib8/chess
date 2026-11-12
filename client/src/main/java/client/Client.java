package client;

import server.ServerFacade;
import ui.Repl;

import java.util.Arrays;

public class Client {

    // handle prelogin and postlogin states

    // prelogin ui:
    // handle help, quit, login, and register

    // postlogin ui:
    // handle help, logout, create game, list games, play game, and observe game

    private State state = State.SIGNED_OUT;
    private final ServerFacade serverFacade;

    public Client(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluatePrompt(String prompt) {
        try {
            String[] tokens = prompt.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : prompt;
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "help" -> help();
                case "quit" -> quit();
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout();
                case "create" -> createGame();
                case "list" -> listGames();
                case "play" -> playGame();
                case "observe" -> observeGame();
                default -> throw new Exception("Error: " + command + " is not an available option, type 'help' to see options.");
            };
        } catch(Exception error) { // change to specific exception?
            return error.getMessage();
        }
    }

    private String help() {
        //Repl.help();
        return null;
    }

    private String quit() {
        return null;
    }

    private String register(String[] params) {
        return null;
    }

    private String login(String[] params) {
        return null;
    }

    private String logout() {
        return null;
    }

    private String createGame() {
        return null;
    }

    private String listGames() {
        return null;
    }

    private String playGame() {
        return null;
    }

    private String observeGame() {
        return null;
    }

    public State getState() {
        return state;
    }
}
