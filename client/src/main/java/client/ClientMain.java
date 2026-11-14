package client;

import chess.*;
import ui.Repl;


public class ClientMain {
    public static void main(String[] args) {
        try {
            new Repl().run();
        } catch(Exception error) {
            System.out.printf("Unable to start server: %s%n", error.getMessage());
        }
    }
}
