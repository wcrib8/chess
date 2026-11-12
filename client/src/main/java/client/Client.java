package client;

import server.ServerFacade;
import ui.Repl;

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
                case "help" -> help();
                case "quit" -> "quit";
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

//    private String quit() {
//        return null;
//    }

    private String register(String[] params) {
        return null;
    }

    private String login(String[] params) {
        return null;
    }

    private String logout() {
        //assertSignedIn();
        //logout();
        state = State.SIGNED_OUT;
        return String.format("%s logged out", authToken); // change to username somehow? or just delete that part
    }

//    public String signOut() throws ResponseException {
//        assertSignedIn();
//        ws.leavePetShop(visitorName);
//        state = State.SIGNEDOUT;
//        return String.format("%s left the shop", visitorName);
//    }

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
