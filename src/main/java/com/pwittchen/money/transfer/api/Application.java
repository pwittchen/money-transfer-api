package com.pwittchen.money.transfer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pwittchen.money.transfer.api.configuration.component.ApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.component.DaggerApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.module.RepositoryModule;
import com.pwittchen.money.transfer.api.configuration.module.ValidationModule;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.ForbiddenResponse;
import io.javalin.Javalin;
import io.javalin.JavalinEvent;
import io.javalin.json.JavalinJson;
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
    final ApplicationComponent component = createApplicationComponent();
    final AccountRepository accountRepository = component.accountRepository();
    final TransactionRepository transactionRepository = component.transactionRepository();

    Javalin app = createServer();

    app.before(context -> {
      LOG.info("{}: {}", context.req.getMethod(), context.req.getRequestURI());
    });

    app.get("/", context -> {
      throw new ForbiddenResponse();
    });

    app.get("/healthcheck", context -> context.result("OK").status(200));

    //TODO #1: expose accounts repo and transaction repo via API below
    //TODO #2: move code, which will be created to separate classes (controllers)

    app.routes(() -> {
      path("account", () -> {
        path(":id", () -> {
          get(context -> {
            context.result("get account with id: ".concat(context.pathParam("id")));
          });
        });

        get(context -> {
          context.result("get all accounts");
        });

        post(context -> {
          context.result("create new account");
        });

        delete(context -> {
          context.result("delete account");
        });
      });

      path("transaction", () -> {
        path(":id", () -> {
          get(context -> {
            context.result("get transaction with id: ".concat(context.pathParam("id")));
          });
        });

        get(context -> {
          context.result("get all transactions");
        });

        post(context -> {
          context.result("transaction committed");
        });
      });
    });

    app.exception(Exception.class, (exception, context) -> {
      context.status(500);
      LOG.error("error occurred", exception);
    });
  }

  private static Javalin createServer() {
    initializeJsonMapping();

    return Javalin.create()
        .event(JavalinEvent.SERVER_STARTED, () -> LOG.info("server started"))
        .event(JavalinEvent.SERVER_START_FAILED, () -> LOG.error("server start failed"))
        .start(PORT);
  }

  private static void initializeJsonMapping() {
    Gson gson = new GsonBuilder().create();
    JavalinJson.setFromJsonMapper(gson::fromJson);
    JavalinJson.setToJsonMapper(gson::toJson);
  }

  private static ApplicationComponent createApplicationComponent() {
    return DaggerApplicationComponent.builder()
        .repositoryModule(new RepositoryModule())
        .validationModule(new ValidationModule())
        .build();
  }
}
