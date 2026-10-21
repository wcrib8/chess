package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        javalin.post("/user", Server::register);
        javalin.post("/session", Server::login);
        javalin.delete("/session", Server::logout);
        javalin.get("/game", Server::listGames);
        javalin.post("/game", Server::createGame);
        javalin.put("/game", Server::joinGame);
        javalin.delete("/db", ctx -> ctx.result("{}"));
        // Register your endpoints and exception handlers here.

    }

    //        var serializer = new Gson();
//        var game = new ChessGame();
//        // serialize to JSON
//        var json = serializer.toJson(game);
//        // deserialize back to ChessGame
//        game = serializer.fromJson(json, ChessGame.class);

    // example from masteryls, implement here in server??
    // include converting thrown exception types into the appropriate HTTP status codes if necessary

    // server handler classes?

    private static void register(Context ctx) {
        var result = Map.of("username", "", "authToken","");
        ctx.result(new Gson().toJson(result));
    }

    private static void login(Context context) {

    }

    private static void logout(Context context) {

    }

    private static void listGames(Context context) {

    }

    private static void createGame(Context context) {

    }

    private static void joinGame(Context context) {

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
