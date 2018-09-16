package com.pwittchen.money.transfer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pwittchen.money.transfer.api.configuration.component.ApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.component.DaggerApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.modules.RepositoryModule;
import com.pwittchen.money.transfer.api.configuration.modules.ValidationModule;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.Javalin;
import io.javalin.JavalinEvent;
import io.javalin.json.JavalinJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);
  private static final int PORT = 8000;

  public static void main(String args[]) {
    final ApplicationComponent component = createApplicationComponent();
    final AccountRepository accountRepository = component.accountRepository();
    final TransactionRepository transactionRepository = component.transactionRepository();

    Javalin app = createServer();

    app.get("/", context -> context.result("server is running"));
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
