package ui;

import java.util.Scanner;

import client.Client;
import client.State;

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
                result = client.evaluateState(prompt);
                System.out.print(SET_TEXT_COLOR_GREEN + result);
                checkIfInGame(scanner);
                // if play or observe, send to another loop?
            } catch (Exception error) {
                System.out.print(error.getMessage());
            }
        }
    }

    public void gameLoop(Scanner scanner) {
        printBoard();
        System.out.print("just printing board for phase 5 purposes. more to come\n");
        // have a while in game loop
    }

    private void printColumnLetters(String teamColor) { // change from string to chessPiece, teamColor?

    }

    private void printPrompt() {
        System.out.print("\n" + RESET + "[" + client.getState() + "] >>> " + SET_TEXT_COLOR_MAGENTA);
    }

    private void checkIfInGame(Scanner scanner) {
        if (client.getState() == State.GAME) {
            gameLoop(scanner);
        }
    }

    private void printBoard() {
        // check what team it is, print respective board
        // spectator will observe from white perspective? for now
//        if (client.getTeamColor().equalsIgnoreCase("black")) {
//            printBlackBoard();
//        }
//        else {
//            printWhiteBoard();
//        }
        boolean isBlackTeam = client.getTeamColor().equalsIgnoreCase("black");

        printColumnLetters(isBlackTeam);

        // print rows all between


        printColumnLetters(isBlackTeam);

        // need an erase screen func?
        // print column letters
        //printColumnLetters("WHITE");
        // print row numbers and board
        // print column letters again
        //printColumnLetters("WHITE");
    }

//    private void printBlackBoard() {
//        // column
//    }
//
//    private void printWhiteBoard() {
//
//    }

    private void printColumnLetters(boolean isBlackTeam) {
        if (isBlackTeam) {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + "    h  g  f  e  d  c  b  a    ");
        }
        else {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + SET_TEXT_COLOR_BLACK + "    a  b  c  d  e  f  g  h    ");
        }
    }
}
