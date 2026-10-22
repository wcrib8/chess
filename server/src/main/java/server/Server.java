package server;

import com.google.gson.Gson;
import io.javalin.*;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import dataaccess.MemoryDataAccess;
import handlers.*;
import service.*;
import service.requests.*;
import service.responses.*;

public class Server {

    private final Javalin javalin;
    UserHandler userHandler = new UserHandler();
    GameHandler gameHandler = new GameHandler();
    private final UserService userService = new UserService(new MemoryDataAccess());
    GameService gameService = new GameService();
    final private HashSet<String> validTokens = new HashSet<>(Set.of("secret1", "secret2"));

    public Server() {
        // Register your endpoints and exception handlers here.
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::register)
                .post("/session", this::login)
                .delete("/session", this::logout)
                .get("/game", this::listGames)
                .post("/game", this::createGame)
                .put("/game", this::joinGame)
                .delete("/db", this::clear)
                .exception(Exception.class, this::exceptionHandler)
                .error(404, this::notFound);
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

    private void register(Context ctx) {
        try {
            RegisterRequest request = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            //        if (request == null) {
            //            throw new RuntimeException("missing required body");
            //        }
            RegisterResult result = userService.register(request);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (Exception error) {
            ctx.status(400);
            ctx.json(Map.of("error", error.getMessage()));
            // use my exception handler method instead?
        }
        // or return new Gson().toJson(result);  ???

        // var result = Map.of("username", "", "authToken","");
        // ctx.result(new Gson().toJson(result));
    }

    private void login(Context ctx) {

    }

    private void logout(Context ctx) {

        if (authorized(ctx)){

        }
    }

    private void listGames(Context ctx) {

        if (authorized(ctx)){

        }
    }

    private void createGame(Context ctx) {

        if (authorized(ctx)){

        }
    }

    private void joinGame(Context ctx) {

        if (authorized(ctx)){

        }
    }

    private void clear(Context ctx) {
        ctx.result("{}");
    }

    private boolean authorized(Context ctx) {
        String authToken = ctx.header("authorization");
        if (!validTokens.contains(authToken)) {
            ctx.contentType("application/json");
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", "Error: unauthorized")));
            return false;
        }
        return true;
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void throwBadRequestException(Context ctx) {
        throw new RuntimeException("The server is on fire!");
        //throw new BadRequestException("bad request");
    }

    private void exceptionHandler(Exception e, Context ctx) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        ctx.status(500);
        ctx.json(body);
    }

    private void notFound(Context ctx) {
        String message = String.format("[%s] %s not found", ctx.method(), ctx.path());
        exceptionHandler(new Exception(message), ctx);
    }
}
