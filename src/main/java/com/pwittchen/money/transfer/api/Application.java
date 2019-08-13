package com.pwittchen.money.transfer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pwittchen.money.transfer.api.configuration.component.ApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.component.DaggerApplicationComponent;
import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.model.Response;
import io.javalin.Javalin;
import io.javalin.http.ForbiddenResponse;
import io.javalin.plugin.json.JavalinJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);
  private static final int PORT = 8000;

  public static void main(String args[]) {
    final ApplicationComponent component = DaggerApplicationComponent.create();
    final AccountController accountController = component.accountController();
    final TransactionController transactionController = component.transactionController();

    final Gson gson = new GsonBuilder().create();
    JavalinJson.setFromJsonMapper(gson::fromJson);
    JavalinJson.setToJsonMapper(gson::toJson);

    final Javalin app = Javalin
        .create(config -> config.requestLogger((context, executionTimeMs) ->
            LOG.info("{} ms\t {}\t {} {}",
                executionTimeMs,
                context.req.getMethod(),
                context.req.getRequestURI(),
                context.req.getParameterMap().toString().replaceAll("^.|.$", "")
            )))
        .events(event -> {
          event.serverStarted(() -> LOG.info("server has started"));
          event.serverStartFailed(() -> LOG.error("server start has failed"));
        })
        .start(PORT);

    app.get("/", context -> {
      throw new ForbiddenResponse();
    });

    app.get("/health", context ->
        context
            .status(200)
            .json(Response.builder().message("OK").build())
    );

    app.routes(() -> {
      path("/account", () -> {
        path(":id", () -> {
          get(accountController::getOne);
          delete(accountController::delete);
        });
        get(accountController::getAll);
        post(accountController::create);
      });

      path("/transaction", () -> {
        path(":id", () -> get(transactionController::getOne));
        get(transactionController::getAll);
        post(transactionController::commit);
      });
    });

    app.exception(Exception.class, (exception, context) -> {
      context.status(500);
      LOG.error("error occurred", exception);
    });
  }
}
