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
        System.out.print("\njust printing board for phase 5 purposes. more to come\n");
        // have a while in game loop
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + "[" + client.getState() + "] >>> " + SET_TEXT_COLOR_MAGENTA);
    }

    private void checkIfInGame(Scanner scanner) {
        if (client.getState() == State.GAME) {
            gameLoop(scanner);
        }
        client.changeState();
    }

    private void printBoard() {
        // spectator will observe from white perspective? for now
        boolean isBlackTeam = client.getTeamColor().equalsIgnoreCase("black");
        System.out.print(SET_TEXT_COLOR_BLACK);
        printColumnLetters(isBlackTeam);
        printRows(isBlackTeam);
        printColumnLetters(isBlackTeam);
        System.out.print(RESET_TEXT_COLOR);

        // need an erase screen func?
        // print column letters
        //printColumnLetters("WHITE");
        // print row numbers and board
        // print column letters again
        //printColumnLetters("WHITE");
    }

    private void printColumnLetters(boolean isBlackTeam) {
        if (isBlackTeam) {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR);
        }
        else {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR);
        }
    }

    private void printRows(boolean isBlackTeam) {
        if (isBlackTeam) {
            for (int row = 1; row < 9; row++) {
                System.out.print("\n");
                printRowNumber(row);
                printChessRow(row);
                printRowNumber(row);
            }
        }
        for (int row = 8; row > 0; row--) {
            System.out.print("\n");
            printRowNumber(row);
            printChessRow(row);
            printRowNumber(row);
        }
    }

    private void printRowNumber(int row) {
        System.out.print(SET_BG_COLOR_MAGENTA + " " + row + " " + RESET_BG_COLOR);
    }

    private void printChessRow(int row) {
        if (row % 2 == 0) {
            System.out.print(SET_BG_COLOR_BLUE + "   " + SET_BG_COLOR_LIGHT_GREY + "   " +
                    SET_BG_COLOR_BLUE + "   " + SET_BG_COLOR_LIGHT_GREY + "   " +
                    SET_BG_COLOR_BLUE + "   " + SET_BG_COLOR_LIGHT_GREY + "   " +
                    SET_BG_COLOR_BLUE + "   " + SET_BG_COLOR_LIGHT_GREY + "   " +
                    RESET_BG_COLOR);
        }
        else {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + "   " + SET_BG_COLOR_BLUE + "   " +
                    SET_BG_COLOR_LIGHT_GREY + "   " + SET_BG_COLOR_BLUE + "   " +
                    SET_BG_COLOR_LIGHT_GREY + "   " + SET_BG_COLOR_BLUE + "   " +
                    SET_BG_COLOR_LIGHT_GREY + "   " + SET_BG_COLOR_BLUE + "   " +
                    RESET_BG_COLOR);
        }
    }
}
