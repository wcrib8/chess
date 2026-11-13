package client;

import client.exceptions.InvalidInputException;
import model.requests.*;
import model.responses.*;
import server.ServerFacade;
import java.util.Arrays;
import static java.lang.Integer.parseInt;

public class Client {
    private State state = State.SIGNED_OUT;
    private String authToken = null;
    private final ServerFacade serverFacade;
    private String teamColor = null;

    public Client(String serverUrl) {
        serverFacade = new ServerFacade(serverUrl);
    }

    public String evaluateState(String prompt) {
        String[] tokens = prompt.toLowerCase().split(" ");
        String command = (tokens.length > 0) ? tokens[0] : prompt;
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (state) {
            case SIGNED_IN -> evaluatePromptSignedIn(command, params);
            case GAME -> evaluatePromptGame(command, params);
            default -> evaluatePromptSignedOut(command, params);
        };
    }

    private String evaluatePromptSignedOut(String command, String[] params) {
        try {
            return switch (command) {
                case "help" -> help(params);
                case "quit" -> "quitting, thanks for playing homeslice ^3^";
                case "register" -> register(params);
                case "login" -> login(params);
                default -> throw new InvalidInputException("Error: " + command + " is not an available option, type 'help' to see options.");
            };
        } catch(Exception error) {
            return error.getMessage();
        }
    }

    private String evaluatePromptSignedIn(String command, String[] params) {
        try {
            return switch (command) {
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

    private String evaluatePromptGame(String command, String[] params) {
        try {
            return ""; // change to game functionality later for phase 6
        } catch(Exception error) {
            return error.getMessage();
        }
    }

    public String help(String[] params) throws Exception { // no params check, prolly not important
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

    public String register(String[] params) throws Exception {
        if (params.length != 3) {
            throw new InvalidInputException("Error: register needs username, password, and email separated by one space");
        }
        RegisterResult result = serverFacade.register(new RegisterRequest(params[0], params[1], params[2]));
        authToken = result.authToken();
        state = State.SIGNED_IN;
        return "Registered user: " + params[0];
    }

    public String login(String[] params) throws Exception {
        if (params.length != 2) {
            throw new InvalidInputException("Error: login needs username and password separated by one space");
        }
        LoginResult result = serverFacade.login(new LoginRequest(params[0], params[1]));
        authToken = result.authToken();
        state = State.SIGNED_IN;
        return "Logged in as " + params[0];
    }

    public String logout(String[] params) throws Exception {
        if (params.length != 0) {
            throw new InvalidInputException("Error: logout requires no extra inputs, just type 'logout'");
        }
        serverFacade.logout(new LogoutRequest(authToken));
        state = State.SIGNED_OUT;
        return "logged out";
    }

    public String createGame(String[] params) throws Exception {
        if (params.length != 1) {
            throw new InvalidInputException("Error: create takes one word for the name of your chess game after 'create' and a space");
        }
        CreateGameResult result = serverFacade.createGame(new CreateGameRequest(authToken, params[0]));
        return String.format("Created game %s", params[0]);
    }

    public String listGames(String[] params) throws Exception {
        if (params.length != 0) {
            throw new InvalidInputException("Error: list requires no extra inputs, just type 'list'");
        }
        ListGamesResult result = serverFacade.listGames(new ListGamesRequest(authToken));
        StringBuilder gameList = new StringBuilder();
        for (int gameNum=0; gameNum < result.games().size(); gameNum++) {
            // add number and game name
            gameList.append(gameNum+1).append(". ").append(result.games().get(gameNum).gameName()).append("\n");
        }
        return String.valueOf(gameList);
    }

    public String playGame(String[] params) throws Exception {
        if (params.length != 2) {
            throw new InvalidInputException("Error: play needs the chosen game's ID and then the chosen team color separated by a space");
        }
        int gameID;
        try {
            gameID = parseInt(params[0]);
        } catch(Exception error) {
            throw new InvalidInputException("Error: gameID is specified first, and should only be a number separated by spaces");
        }
        serverFacade.joinGame(new JoinGameRequest(authToken, params[1], gameID));

        // draw initial chess board

        state = State.GAME;
        teamColor = params[1];
        return String.format("joined game %s as team %s", params[0], params[1]);
    }

    public String observeGame(String[] params) throws Exception {
        if (params.length != 1) {
            throw new InvalidInputException("Error: observe only needs the ID of the game you want to spectate");
        }
        serverFacade.joinGame(new JoinGameRequest(authToken, null, parseInt(params[0]))); // change string and int

        // draw initial chess board

        state = State.GAME;
        teamColor = "spectator";
        return String.format("spectating game %s", params[0]);
    }

    public State getState() {
        return state;
    }

    public void changeState() {
        state = State.SIGNED_IN; // temporary function for phase 5, delete later
    }

    public String getTeamColor() {
        return teamColor;
    }

//    private void assertSignedIn() throws InvalidInputException {
//        if (state != State.SIGNED_IN) {
//            throw new InvalidInputException("Error: you must register or log in first");
//        }
//    }
//
//    private void assertSignedOut() throws InvalidInputException {
//        if (state != State.SIGNED_OUT) {
//            throw new InvalidInputException("Error: you must logout first");
//        }
//    }  not used anymore...?
}
