package server;

import com.google.gson.Gson;
import dataaccess.exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import dataaccess.MemoryDataAccess;
import service.*;
import service.exceptions.*;
import service.requests.*;
import service.responses.*;

public class Server {

    private final Javalin javalin;
    private final Service service = new Service(new MemoryDataAccess());
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


    // endpoint handler methods

    private void register(Context ctx) throws AlreadyTakenException {
        try {
            RegisterRequest request = new Gson().fromJson(ctx.body(), RegisterRequest.class);
            if (request.username() == null || request.password() == null || request.email() == null) {
                ctx.status(400);
                ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
                return;
            }
            RegisterResult result = service.register(request);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (AlreadyTakenException error) {
            ctx.status(403);
            ctx.result(new Gson().toJson(Map.of("message", error.getMessage())));
        }
    }

    private void login(Context ctx) throws UnauthorizedException {
        try {
            LoginRequest request = new Gson().fromJson(ctx.body(), LoginRequest.class);
            if (request.username() == null || request.password() == null) {
                ctx.status(400);
                ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
                return;
            }
            LoginResult result = service.login(request);
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
        if (authorized(ctx)) {
            service.clear();
            ctx.status(200);
            ctx.result("{}");
        }
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
