package ui;

import java.util.Scanner;

import chess.ChessBoard;
import chess.ChessGame;
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

        while (!result.equals("quitting, thanks for playing homeslice ^3^")) {
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
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + "    h" + COL_SPACE + "g" + COL_SPACE + "f" + COL_SPACE + "e" +
                    COL_SPACE + "d" + COL_SPACE + "c" + COL_SPACE + "b" + COL_SPACE + "a  " + COL_SPACE + RESET_BG_COLOR);
        }
        else {
            System.out.print("\n" + SET_BG_COLOR_MAGENTA + "    a" + COL_SPACE + "b" + COL_SPACE + "c" + COL_SPACE + "d" +
                    COL_SPACE + "e" + COL_SPACE + "f" + COL_SPACE + "g" + COL_SPACE + "h  " + COL_SPACE + RESET_BG_COLOR);
        }
    }

    private void printAllRows(boolean isBlackTeam, ChessBoard board) {
        if (isBlackTeam) {
            for (int row = 1; row < 9; row++) {
                System.out.print("\n");
                printRowNumber(row);
                printChessRow(row, true, board);
                printRowNumber(row);
            }
        } else {
            for (int row = 8; row > 0; row--) {
                System.out.print("\n");
                printRowNumber(row);
                printChessRow(row, false, board);
                printRowNumber(row);
            }
        }
    }

    private void printRowNumber(int row) {
        System.out.print(SET_BG_COLOR_MAGENTA + " " + row + " " + RESET_BG_COLOR);
    }

    private void printChessRow(int row, boolean isBlackTeam, ChessBoard board) {
        if (isBlackTeam) {
            for (int column = 8; column > 0; column--) {
                printBoardBackground(row, column);
                printBoardPiece(row, column, board);
            }
        } else {
            for (int column = 1; column < 9; column++) {
                printBoardBackground(row, column);
                printBoardPiece(row, column, board);
            }
        }
    }

    private void printBoardBackground(int row, int column) {
        if ((row + column) % 2 == 0) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY);
        }
        else {
            System.out.print(SET_BG_COLOR_BLUE);
        }
    }

    private void printBoardPiece(int row, int column, ChessBoard board) {
        // find what piece it is, print out that character
        ChessPosition position = new ChessPosition(row, column);
        if (board.getPiece(position) == null) {
            System.out.print(EMPTY);
            return;
        }
        var color = board.getPiece(position).getTeamColor();
        if (color.equals(ChessGame.TeamColor.BLACK)) {
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
                case PAWN -> System.out.print(SET_TEXT_COLOR_WHITE + WHITE_PAWN + SET_TEXT_COLOR_BLACK);
                case ROOK -> System.out.print(SET_TEXT_COLOR_WHITE + WHITE_ROOK + SET_TEXT_COLOR_BLACK);
                case KNIGHT -> System.out.print(SET_TEXT_COLOR_WHITE + WHITE_KNIGHT + SET_TEXT_COLOR_BLACK);
                case BISHOP -> System.out.print(SET_TEXT_COLOR_WHITE + WHITE_BISHOP + SET_TEXT_COLOR_BLACK);
                case QUEEN -> System.out.print(SET_TEXT_COLOR_WHITE + WHITE_QUEEN + SET_TEXT_COLOR_BLACK);
                case KING -> System.out.print(SET_TEXT_COLOR_WHITE + WHITE_KING + SET_TEXT_COLOR_BLACK);
                default -> System.out.print(EMPTY);
            }
        }
    }
}
