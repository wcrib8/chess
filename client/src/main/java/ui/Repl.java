package ui;

import java.util.Scanner;

import ui.EscapeSequences;

public class Repl {
    // board dimensions:

    // padded characters:


    // 3 repl loops, prelogin, postlogin, and game
    // have scanner to read input, but maybe not here in this class??
    public void main(String[] args) {  // repl has scanner read loop. it calls into client to evaluate the scanned line
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();

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

    private void printColumnLetters(String teamColor) { // change from string to chessPiece, teamColor?

    }

    private void printPrompt() {

    }
}
