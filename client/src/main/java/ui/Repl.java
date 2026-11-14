package ui;

import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessPosition;
import client.Client;
import client.State;

import static ui.EscapeSequences.*;

public class Repl {
    // board dimensions:

    // padded characters:


    private final Client client = new Client("http://localhost:8080");

    public Repl() {}

    // 3 repl loops, pre-login, post-login, and game
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

    public void gameLoop(Scanner scanner, ChessBoard board) {
        printBoard(board);
        System.out.print("\njust printing board for phase 5 purposes. more to come\n");
        client.changeState();
        // have while in game loop
    }

    private void printPrompt() {
        System.out.print("\n" + RESET + "[" + client.getState() + "] >>> " + SET_TEXT_COLOR_MAGENTA);
    }

    private void checkIfInGame(Scanner scanner) {
        if (client.getState() == State.GAME) {
            gameLoop(scanner, new ChessBoard());
        }
    }

    private void printBoard(ChessBoard board) {
        // spectator will observe from white perspective? for now
        boolean isBlackTeam = client.getTeamColor().equalsIgnoreCase("black");
        board.resetBoard();
        System.out.print(SET_TEXT_COLOR_BLACK);
        printColumnLetters(isBlackTeam);
        printAllRows(isBlackTeam, board);
        printColumnLetters(isBlackTeam);
        System.out.print(RESET_TEXT_COLOR);

        // need an erase screen func?
    }

    private void printColumnLetters(boolean isBlackTeam) {
        if (isBlackTeam) {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + "     h   g   f   e   d   c   b   a     " + RESET_BG_COLOR);
        }
        else {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + "     a   b   c   d   e   f   g   h     " + RESET_BG_COLOR);
        }
    }

    private void printAllRows(boolean isBlackTeam, ChessBoard board) {
        if (isBlackTeam) {
            for (int row = 1; row < 9; row++) {
                System.out.print("\n");
                printRowNumber(row);
                printChessRow(row, isBlackTeam, board);
                printRowNumber(row);
            }
        }
        for (int row = 8; row > 0; row--) {
            System.out.print("\n");
            printRowNumber(row);
            printChessRow(row, isBlackTeam, board);
            printRowNumber(row);
        }
    }

    private void printRowNumber(int row) {
        System.out.print(SET_BG_COLOR_MAGENTA + " " + row + " " + RESET_BG_COLOR);
    }

    private void printChessRow(int row, boolean isBlackTeam, ChessBoard board) {
        for (int column = 1; column < 9; column++) {
            printBoardBackground(row, column, isBlackTeam);
            printBoardPiece(row, column, isBlackTeam, board);
        }
    }

    private void printBoardBackground(int row, int column, boolean isBlackTeam) {
        if (isBlackTeam) {
            if ((row + column) % 2 == 0) {
                System.out.print(SET_BG_COLOR_BLUE);
            }
            else {
                System.out.print(SET_BG_COLOR_LIGHT_GREY);
            }
        }
        else if ((row + column) % 2 == 0) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        else {
            System.out.print(SET_BG_COLOR_BLUE);
        }
    }

    private void printBoardPiece(int row, int column, boolean isBlackTeam, ChessBoard board) {
        // find what piece it is, print out that character
        ChessPosition position = new ChessPosition(row, column);
        if (board.getPiece(position) == null) {
            System.out.print(EMPTY);
            return;
        }
        if (isBlackTeam) {
            switch (board.getPiece(position).getPieceType()) {
                case PAWN -> System.out.print(BLACK_PAWN);
                case ROOK -> System.out.print(BLACK_ROOK);
                case KNIGHT -> System.out.print(BLACK_KNIGHT);
                case BISHOP -> System.out.print(BLACK_BISHOP);
                case QUEEN -> System.out.print(BLACK_QUEEN);
                case KING -> System.out.print(BLACK_KING);
                default -> System.out.print(EMPTY);
            }
        } else {
            switch (board.getPiece(position).getPieceType()) {
                case PAWN -> System.out.print(WHITE_PAWN);
                case ROOK -> System.out.print(WHITE_ROOK);
                case KNIGHT -> System.out.print(WHITE_KNIGHT);
                case BISHOP -> System.out.print(WHITE_BISHOP);
                case QUEEN -> System.out.print(WHITE_QUEEN);
                case KING -> System.out.print(WHITE_KING);
                default -> System.out.print(EMPTY);
            }
        }
    }
}
