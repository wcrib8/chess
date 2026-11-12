package client;

import chess.*;
import ui.Repl;


public class ClientMain {

    // client main just starts up server connection, or throws error?
    //private final Repl repl = new Repl();

    public static void main(String[] args) throws Exception {
        try {
            new Repl().run();
        } catch(Exception error) {
            System.out.printf("Unable to start server: %s%n", error.getMessage());
        }
    }
}
