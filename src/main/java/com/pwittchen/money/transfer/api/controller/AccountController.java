package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.DeleteAccountCommand;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.query.GetAccountQuery;
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
  private GetAccountQuery getAccountQuery;
  private GetAllAccountsQuery getAllAccountsQuery;
  private CreateAccountCommand createAccountCommand;
  private DeleteAccountCommand deleteAccountCommand;

  @Inject public AccountController(
      final ContextWrapper contextWrapper,
      final GetAccountQuery getAccountQuery,
      final GetAllAccountsQuery getAllAccountsQuery,
      final CreateAccountCommand createAccountCommand,
      final DeleteAccountCommand deleteAccountCommand
  ) {
    this.contextWrapper = contextWrapper;
    this.getAccountQuery = getAccountQuery;
    this.getAllAccountsQuery = getAllAccountsQuery;
    this.createAccountCommand = createAccountCommand;
    this.deleteAccountCommand = deleteAccountCommand;
  }

  @OpenApi(
      method = HttpMethod.GET,
      path = "/account/:id",
      description = "gets single account with a given id",
      pathParams = @OpenApiParam(name = "id", type = Integer.class),
      responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class)),
          @OpenApiResponse(status = "404", content = @OpenApiContent(from = String.class))
      }
  )
  public void getOne(Context context) {
    Optional<Account> account = getAccountQuery.run(contextWrapper.pathParam(context, "id"));

    if (account.isPresent()) {
      contextWrapper.json(context, account.get(), HttpStatus.OK_200);
    } else {
      String message = String.format(
          "account with id %s does not exist",
          contextWrapper.pathParam(context, "id")
      );

      contextWrapper.json(context, message, HttpStatus.NOT_FOUND_404);
    }
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
    Optional<Account> account = createAccount(context);

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

  @OpenApi(
      method = HttpMethod.DELETE,
      path = "/account/:id",
      description = "deletes an account with given id",
      pathParams = @OpenApiParam(name = "id", type = Integer.class),
      responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = String.class)),
          @OpenApiResponse(status = "400", content = @OpenApiContent(from = String.class))
      }
  )
  public void delete(Context context) {
    try {
      deleteAccountCommand.run(contextWrapper.pathParam(context, "id"));

      String message = String.format(
          "account with number %s deleted",
          contextWrapper.pathParam(context, "id")
      );

      contextWrapper.json(context, message, HttpStatus.OK_200);
    } catch (Exception exception) {
      contextWrapper.json(context, exception.getMessage(), HttpStatus.BAD_REQUEST_400);
    }
  }
}
