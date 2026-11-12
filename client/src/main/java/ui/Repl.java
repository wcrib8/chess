package ui;

import java.util.Scanner;

import client.Client;
import static ui.EscapeSequences.*;

public class Repl {
    // board dimensions:

    // padded characters:


    private final Client client = new Client("http://localhost:8080");

    public Repl() {}

    // 3 repl loops, prelogin, postlogin, and game
    // have scanner to read input, but maybe not here in this class??
    public void run() {  // repl has scanner read loop. it calls into client to evaluate the scanned line
        System.out.println("♕ 240 Chess Client: Type help to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String prompt = scanner.nextLine();
            try {
                result = client.evaluatePrompt(prompt);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
            } catch (Exception error) { // different exception?
                System.out.print(error.getMessage()); // something else?
            }
        }
        System.out.println("Thanks for playing homeslice ^3^");
        // need an erase screen func?
        // print column letters
        //printColumnLetters("WHITE");
        // print row numbers and board
        // print column letters again
        //printColumnLetters("WHITE");
    }

    public String help() {
        //if (state == State.SIGNED_OUT) {
            return """
                    Available actions, type commands in the format shown: " +
                    + SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL> : to create an account
              login <USERNAME> <PASSWORD> : to login to existing account
              quit : exit the chess application2a
              help : displays available actions
            """;
        //}
        //return """
                
        //        """;
    }

    private void printColumnLetters(String teamColor) { // change from string to chessPiece, teamColor?

    }

    private void printPrompt() {
        System.out.print("\n" + RESET + "[" + client.getState() + "] >>> " + SET_TEXT_COLOR_MAGENTA);
    }
}
