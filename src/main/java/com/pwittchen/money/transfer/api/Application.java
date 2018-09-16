package com.pwittchen.money.transfer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pwittchen.money.transfer.api.configuration.component.ApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.component.DaggerApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.module.RepositoryModule;
import com.pwittchen.money.transfer.api.configuration.module.ValidationModule;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Response;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.ForbiddenResponse;
import io.javalin.Javalin;
import io.javalin.JavalinEvent;
import io.javalin.json.JavalinJson;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);
  private static final int PORT = 8000;

  @SuppressWarnings("ResultOfMethodCallIgnored") // subscriptions don't need to be disposed now
  public static void main(String args[]) {
    final ApplicationComponent component = createApplicationComponent();
    final AccountRepository accountRepository = component.accountRepository();
    final TransactionRepository transactionRepository = component.transactionRepository();

    Javalin app = createServer();

    app.before(context -> {
      LOG.info("{}\t {}", context.req.getMethod(), context.req.getRequestURI());
    });

    app.get("/", context -> {
      throw new ForbiddenResponse();
    });

    app.get("/health", context ->
        context
            .json(Response.builder().message("OK").build())
            .status(200)
    );

    //TODO #1: expose accounts repo and transaction repo via API below
    //TODO #2: move code, which will be created to separate classes (controllers)

    app.routes(() -> {
      path("/account", () -> {
        path(":id", () -> {
          get(context -> {
            Optional<Account> account = accountRepository.get(context.pathParam("id"));

            if (account.isPresent()) {
              context.json(account);
            } else {
              context
                  .status(404)
                  .json(Response.builder().message(String.format(
                      "account with id %s does not exist", context.pathParam("id")
                  )));
            }
          });
        });

        get(context -> {
          context.json(accountRepository.get());
        });

        post(context -> {
          context.result("create new account");
        });

        delete(context -> {
          accountRepository
              .delete(context.formParam("id"))
              .subscribe(() -> {
                    context.json(Response.builder().message(
                        String.format("account with id %s deleted", context.formParam("id"))
                    ));
                  },
                  throwable -> {
                    context
                        .json(Response.builder().message(throwable.getMessage()));
                  }
              );
        });
      });

      path("/transaction", () -> {
        path(":id", () -> {
          get(context -> {
            Optional<Transaction> transaction = transactionRepository.get(context.pathParam("id"));

            if (transaction.isPresent()) {
              context.json(transaction);
            } else {
              context
                  .status(404)
                  .json(Response.builder().message(String.format(
                      "transaction with id %s does not exist", context.pathParam("id")
                  )));
            }
          });
        });

        get(context -> {
          context.json(transactionRepository.get());
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
