package client;

import chess.*;
import ui.Repl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Locale;

public class ClientMain {

    // client main just starts up server connection, or throws error?
    private final Repl repl = new Repl();

    public void main(String[] args) throws Exception {
        //var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        //System.out.println("♕ 240 Chess Client: " + piece);
        System.out.println("♕ 240 Chess Client: Type help to get started.");
        try {
            repl.main();
        } catch(Exception error) {
            System.out.printf("Unable to start server: %s%n", error.getMessage());
        }

        new ClientMain().get("localhost", 8080, "/name");
    }


    private void get(String host, int port, String path) throws Exception {
        String urlString = String.format(Locale.getDefault(), "http://%s:%d%s", host, port, path);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(urlString))
                .timeout(java.time.Duration.ofMillis(5000))
                .GET()
                // .header() ??
                .build();

        //HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());



//        var response = client.send(request, BodyHandlers.ofString());
//        var headers = response.headers();
//        OptionalLong length = response.firstValueAsLong("Content-Length");
//        Optional<String> type = response.firstValue("Content-Type");
        // from masteryLS

//        var body = Map.of("name", "joe", "type", "cat");
//        var jsonBody = new Gson().toJson(body);
//
//        var request = HttpRequest.newBuilder(uri)
//                .POST(BodyPublishers.ofString(jsonBody))
//                .header("Content-Type", "application/json")
//                .build();
        // masteryLs

//        if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
//            System.out.println(httpResponse.body());
//        } else {
//            System.out.println("Error: received status code " + httpResponse.statusCode());
//        }

        // above code copied, verify its useful, adjust


    }
}
