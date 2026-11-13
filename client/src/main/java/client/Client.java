package client;

import client.exceptions.InvalidInputException;
import model.requests.*;
import model.responses.*;
import server.ServerFacade;
import java.util.Arrays;

public class Client {

    // handle prelogin and postlogin states


    private State state = State.SIGNED_OUT;
    private String authToken = null;
    private final ServerFacade serverFacade;

    public enum State {
        SIGNED_OUT,
        SIGNED_IN
    }

    public Client(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluatePrompt(String prompt) {
        try {
            String[] tokens = prompt.toLowerCase().split(" ");
            String command = (tokens.length > 0) ? tokens[0] : prompt;
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (command) {
                case "help" -> help(params);
                case "quit" -> "quitting, thanks for playing homeslice ^3^";
                case "register" -> register(params);
                case "login" -> login(params);
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "play" -> playGame(params);
                case "observe" -> observeGame(params);
                default -> throw new InvalidInputException("Error: " + command + " is not an available option, type 'help' to see options.");
            };
        } catch(Exception error) {
            return error.getMessage();
        }
    }

    private String help(String[] params) throws Exception { // no params check, prolly not important
        if (params.length != 0) {
            throw new InvalidInputException("Error: help requires no extra inputs, just type 'help'");
        }
        if (state == State.SIGNED_OUT) {
            return """
                Available actions, type commands in the format shown:
                  register <USERNAME> <PASSWORD> <EMAIL> : to create an account
                  login <USERNAME> <PASSWORD> : to login to existing account
                  quit : exit the chess application
                  help : displays available actions""";
        }
        return """
            Available actions, type commands in the format shown:
              logout : sign out of your account
              create <NAME> : create a new chess game
              list : show a list of available games
              play <ID> [WHITE|BLACK] : join a game with its ID and choose your team color
              observe <ID> : spectate a game
              help : displays available actions""";
    }

    private String register(String[] params) throws Exception {
        assertSignedOut();
        if (params.length != 3) {
            throw new InvalidInputException("Error: register needs username, password, and email separated by one space");
        }
        RegisterResult result = serverFacade.register(new RegisterRequest(params[0], params[1], params[2]));
        authToken = result.authToken();
        state = State.SIGNED_IN;
        return "Registered user: " + params[0];
    }

    private String login(String[] params) throws Exception {
        assertSignedOut();
        if (params.length != 2) {
            throw new InvalidInputException("Error: login needs username and password separated by one space");
        }
        LoginResult result = serverFacade.login(new LoginRequest(params[0], params[1]));
        authToken = result.authToken();
        state = State.SIGNED_IN;
        return "Logged in as " + params[0];
    }

    private String logout(String[] params) throws Exception {
        assertSignedIn();
        if (params.length != 0) {
            throw new InvalidInputException("Error: logout requires no extra inputs, just type 'logout'");
        }
        assertSignedIn();
        serverFacade.logout(new LogoutRequest(authToken)); // authtoken stuff...???
        state = State.SIGNED_OUT;
        return String.format("%s logged out", authToken); // change to username somehow? or just delete that part
    }

    private String createGame(String[] params) throws Exception {
        assertSignedIn();
        if (params.length != 1) {
            throw new InvalidInputException("Error: create takes one word for the name of your chess game after 'create' and a space");
        }
        CreateGameResult result = serverFacade.createGame(new CreateGameRequest(authToken, params[0]));
        return String.format("Joined game %s", params[0]);
    }

    private String listGames(String[] params) throws Exception {
        assertSignedIn();
        if (params.length != 0) {
            throw new InvalidInputException("Error: list requires no extra inputs, just type 'list'");
        }
        ListGamesResult result = serverFacade.listGames(new ListGamesRequest(authToken));
        for (int gameNum=0; gameNum < result.games().size(); gameNum++) {
            // add number
            // add game name
        }
        return String.format(""); // list game names?
        // number list, just 1 to whatever, not game ids, then game name?
    }

    private String playGame(String[] params) throws Exception {
        assertSignedIn();
        if (params.length != 2) { // need check for correct word, black/white? or already done in service?
            throw new InvalidInputException("Error: play needs the chosen game's ID and then the chosen team color separated by a space");
        }
        return null;
    }

    private String observeGame(String[] params) throws Exception {
        assertSignedIn();
        if (params.length != 1) {
            throw new InvalidInputException("Error: observe only needs the ID of the game you want to spectate");
        }
        //serverFacade.joinGame(new JoinGameRequest(authToken, "white", 9)); // change string and int
        return null;
    }

    public State getState() {
        return state;
    }

    private void assertSignedIn() throws InvalidInputException {
        if (state != State.SIGNED_IN) {
            throw new InvalidInputException("Error: you must register or log in first");
        }
    }

    private void assertSignedOut() throws InvalidInputException {
        if (state != State.SIGNED_OUT) {
            throw new InvalidInputException("Error: you must logout first");
        }
    }
}
