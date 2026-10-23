package server;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import io.javalin.*;
import io.javalin.http.Context;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import dataaccess.MemoryDataAccess;
import handlers.*;
import service.*;
import service.exceptions.UnauthorizedException;
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

    private void register(Context ctx) throws Exception {
        try {
            RegisterRequest request = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            if (request.username() == null || request.password() == null || request.email() == null) {
                ctx.status(400);
                ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
                return;
            }
            RegisterResult result = userService.register(request);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (AlreadyTakenException error) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }
    }

    private void login(Context ctx) {
        try {
            LoginRequest request = new Gson().fromJson(ctx.body(), LoginRequest.class);
            if (request.username() == null || request.password() == null) {
                ctx.status(400);
                ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
                return;
            }
            LoginResult result = userService.login(request);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (UnauthorizedException error) {
            ctx.status(401);
            ctx.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }
    }

    private void logout(Context ctx) {
        try {

            if (authorized(ctx)){

            }
        } catch (Exception error) {

        }
    }

    private void listGames(Context ctx) {
        try {

            if (authorized(ctx)){

            }
        } catch (Exception error) {

        }
    }

    private void createGame(Context ctx) {
        try {

            if (authorized(ctx)){

            }
        } catch (Exception error) {

        }
    }

    private void joinGame(Context ctx) {
        try {

            if (authorized(ctx)){

            }
        } catch (Exception error) {

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

    private void exceptionHandler(Exception e, Context ctx) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage())));
        ctx.status(500);
        ctx.result(body);
    }

    private void notFound(Context ctx) {
        String message = String.format("[%s] %s not found", ctx.method(), ctx.path());
        exceptionHandler(new Exception(message), ctx);
    }
}
