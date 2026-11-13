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
    public void run() {
        System.out.println("♕ 240 Chess Client: Type 'help' to get started.");
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String prompt = scanner.nextLine();
            try {
                result = client.evaluatePrompt(prompt);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
                // if play or observe, send to another loop?
            } catch (Exception error) {
                System.out.print(error.getMessage());
            }
        }

        // need an erase screen func?
        // print column letters
        //printColumnLetters("WHITE");
        // print row numbers and board
        // print column letters again
        //printColumnLetters("WHITE");
    }

    private void printColumnLetters(String teamColor) { // change from string to chessPiece, teamColor?

    }

    private void printPrompt() {
        System.out.print("\n" + RESET + "[" + client.getState() + "] >>> " + SET_TEXT_COLOR_MAGENTA);
    }
}
