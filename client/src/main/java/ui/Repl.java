package ui;

import java.util.Scanner;

import client.Client;
import client.State;
import static ui.EscapeSequences.*;

public class Repl {
    // board dimensions:

    // padded characters:


    private final Client client = new Client("http://localhost:8080");
    private State state = State.SIGNED_OUT;

    public Repl() {}

    // 3 repl loops, prelogin, postlogin, and game
    // have scanner to read input, but maybe not here in this class??
    public void main() {  // repl has scanner read loop. it calls into client to evaluate the scanned line
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String prompt = scanner.nextLine();

            try {
                // client?
                client.evaluatePrompt(prompt);
            } catch (Exception error) {
                System.out.print(error); // something else?
            }
        }

        // need an erase screen func?

        // print column letters
        printColumnLetters("WHITE");

        // print row numbers and board

        // print column letters again
        printColumnLetters("WHITE");

//        Scanner scanner = new Scanner(System.in);
//        var result = "";
//        while (!result.equals("quit")) {
//            printPrompt();
//            String line = scanner.nextLine();
//
//            try {
//                result = eval(line);
//                System.out.print(BLUE + result);
//            } catch (Throwable e) {
//                var msg = e.toString();
//                System.out.print(msg);
//            }
//        }
//        System.out.println();
    }

    public String help() {
        if (state == State.SIGNED_OUT) {
            return """
                    Available actions, type commands in the format shown: " +
                    + SET_TEXT_COLOR_BLUE + "register <USERNAME> <PASSWORD> <EMAIL> : to create an account
              login <USERNAME> <PASSWORD> : to login to existing account
              quit : exit the chess application
              help : displays available actions
            """;
        }
        return """
                
                """;
    }

    private void printColumnLetters(String teamColor) { // change from string to chessPiece, teamColor?

    }

    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_MAGENTA);
    }
}
