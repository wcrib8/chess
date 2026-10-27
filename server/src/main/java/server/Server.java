package server;

import com.google.gson.Gson;
import dataaccess.exceptions.*;
import io.javalin.*;
import io.javalin.http.Context;
import java.util.Map;
import dataaccess.MemoryDataAccess;
import service.*;
import service.exceptions.*;
import service.requests.*;
import service.responses.*;

public class Server {

    private final Javalin javalin;
    private final Service service = new Service(new MemoryDataAccess());

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

    private void register(Context ctx) {
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
            exceptionResult(ctx, error, 403);
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
            LoginResult result = service.login(request);
            ctx.status(200);
            ctx.result(new Gson().toJson(result));
        } catch (UnauthorizedException error) {
            exceptionResult(ctx, error, 401);
        }
    }

    private void logout(Context ctx) {
        try {
            if (hasAuthHeader(ctx)) {
                //LogoutRequest request = new Gson().fromJson(ctx.header("Authorization"), LogoutRequest.class);
                LogoutRequest request = new LogoutRequest(ctx.header("Authorization"));
                service.logout(request);
                ctx.status(200);
                ctx.result("{}");
            }
        } catch (UnauthorizedException error) {
            exceptionResult(ctx, error, 401);
        }
    }

    private void listGames(Context ctx) {
        try {
            if (hasAuthHeader(ctx)) {
                ListGamesRequest request = new ListGamesRequest(ctx.header("Authorization"));
                ListGamesResult result = service.listGames(request);
                ctx.status(200);
                ctx.result(new Gson().toJson(result));
            }
        } catch (UnauthorizedException error) {
            exceptionResult(ctx, error, 401);
        }
    }

    private void createGame(Context ctx) {
        try {
            if (hasAuthHeader(ctx)){
                CreateGameRequest parseGameName = new Gson().fromJson(ctx.body(), CreateGameRequest.class);
                CreateGameRequest request = new CreateGameRequest(ctx.header("Authorization"), parseGameName.gameName());
                CreateGameResult result = service.createGame(request);
                ctx.status(200);
                ctx.result(new Gson().toJson(result));
            }
        } catch (BadRequestException error) {
            exceptionResult(ctx, error, 400);
        } catch (UnauthorizedException error) {
            exceptionResult(ctx, error, 401);
        }
    }

    private void joinGame(Context ctx) {
        try {

            if (hasAuthHeader(ctx)) {
                JoinGameRequest parseColorAndID = new Gson().fromJson(ctx.body(), JoinGameRequest.class);
                JoinGameRequest request = new JoinGameRequest(ctx.header("Authorization"), parseColorAndID.playerColor(), parseColorAndID.gameID());
                service.joinGame(request);
                ctx.status(200);
                ctx.result("{}");
            }
        } catch (BadRequestException error) {
            exceptionResult(ctx, error, 400);
        } catch (UnauthorizedException error) {
            exceptionResult(ctx, error, 401);
        } catch (AlreadyTakenException error) {
            exceptionResult(ctx, error, 403);
        }
    }

    private void clear(Context ctx) {
        service.clear();
        ctx.status(200);
        ctx.result("{}");
    }

    private static void exceptionResult(Context ctx, Exception error, int status) {
        ctx.status(status);
        ctx.result(new Gson().toJson(Map.of("message", error.getMessage())));
    }

    private boolean hasAuthHeader(Context ctx) {
        String authToken = ctx.header("authorization");
        if (authToken == null) {
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", "Error: bad request")));
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
