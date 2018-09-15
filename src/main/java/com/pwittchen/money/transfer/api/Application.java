package com.pwittchen.money.transfer.api;

import com.pwittchen.money.transfer.api.configuration.component.ApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.component.DaggerApplicationComponent;
import com.pwittchen.money.transfer.api.configuration.modules.RepositoryModule;
import com.pwittchen.money.transfer.api.configuration.modules.ValidationModule;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger LOG = LoggerFactory.getLogger(Application.class);
  private static final int PORT = 8000;

  public static void main(String args[]) {
    final ApplicationComponent component = createApplicationComponent();
    final AccountRepository accountRepository = component.accountRepository();
    final TransactionRepository transactionRepository = component.transactionRepository();

    Javalin app = Javalin.create().start(PORT);
    app.get("/", context -> context.result("server is running"));
    LOG.info("server is running");
  }

  private static ApplicationComponent createApplicationComponent() {
    return DaggerApplicationComponent.builder()
        .repositoryModule(new RepositoryModule())
        .validationModule(new ValidationModule())
        .build();
  }
}
