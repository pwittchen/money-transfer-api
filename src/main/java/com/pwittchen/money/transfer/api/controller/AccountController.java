package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.money.Money;

public class AccountController {

  private ContextWrapper contextWrapper;
  private GetAllAccountsQuery getAllAccountsQuery;
  private CreateAccountCommand createAccountCommand;

  @Inject public AccountController(
      final ContextWrapper contextWrapper,
      final GetAllAccountsQuery getAllAccountsQuery,
      final CreateAccountCommand createAccountCommand
  ) {
    this.contextWrapper = contextWrapper;
    this.getAllAccountsQuery = getAllAccountsQuery;
    this.createAccountCommand = createAccountCommand;
  }

  @OpenApi(
      method = HttpMethod.GET,
      path = "/account",
      description = "gets all accounts",
      responses = @OpenApiResponse(
          status = "200",
          content = @OpenApiContent(from = Account.class, isArray = true)
      )
  )
  public void getAll(final Context context) {
    contextWrapper.json(context, getAllAccountsQuery.run());
  }

  @OpenApi(
      method = HttpMethod.POST,
      path = "/account",
      description = "creates an account",
      pathParams = {
          @OpenApiParam(name = "owner"),
          @OpenApiParam(name = "currency"),
          @OpenApiParam(name = "money")
      },
      responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class)),
          @OpenApiResponse(status = "400", content = @OpenApiContent(from = String.class))
      }
  )
  public void create(final Context context) {
    final Optional<Account> account = createAccount(context);
    if (account.isEmpty()) {
      contextWrapper.json(context, "Invalid money format", HttpStatus.BAD_REQUEST_400);
      return;
    }

    try {
      createAccountCommand.run(account.get());
      contextWrapper.json(context, account, HttpStatus.OK_200);
    } catch (Exception exception) {
      contextWrapper.json(context, exception.getMessage(), HttpStatus.BAD_REQUEST_400);
    }
  }

  private Optional<Account> createAccount(Context context) {
    return parseMoney(context)
        .map(money -> Account.builder()
            .number(UUID.randomUUID().toString())
            .owner(contextWrapper.formParam(context, "owner"))
            .money(money)
            .createdAt(LocalDateTime.now())
            .build()
        );
  }

  private Optional<Money> parseMoney(Context context) {
    try {
      return Optional.of(Money.parse(String.format("%s %s",
          contextWrapper.formParam(context, "currency"),
          contextWrapper.formParam(context, "money"))
      ));
    } catch (Exception exception) {
      return Optional.empty();
    }
  }
}
