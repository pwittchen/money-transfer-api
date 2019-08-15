package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Response;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
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
import org.joda.money.Money;

public class AccountController {

  private ContextWrapper contextWrapper;
  private AccountRepository accountRepository;

  @Inject
  public AccountController(
      final ContextWrapper contextWrapper,
      final AccountRepository accountRepository) {
    this.contextWrapper = contextWrapper;
    this.accountRepository = accountRepository;
  }

  @OpenApi(
      method = HttpMethod.GET,
      path = "/account/:id",
      description = "gets single account with a given id",
      pathParams = @OpenApiParam(name = "id", type = Integer.class),
      responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Account.class))
  )
  public void getOne(Context context) {
    Optional<Account> account = accountRepository.get(contextWrapper.pathParam(context, "id"));

    if (account.isPresent()) {
      contextWrapper.json(context, account.get());
    } else {
      String message = String.format(
          "account with id %s does not exist",
          contextWrapper.pathParam(context, "id")
      );

      Response response = Response.builder()
          .message(message)
          .build();

      contextWrapper.json(context, response, 404);
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
    contextWrapper.json(context, accountRepository.get());
  }

  @OpenApi(
      method = HttpMethod.POST,
      path = "/account",
      description = "creates an account",
      pathParams = {
          @OpenApiParam(name = "name"),
          @OpenApiParam(name = "surname"),
          @OpenApiParam(name = "currency"),
          @OpenApiParam(name = "money")
      },
      responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Response.class))
  )
  public void create(final Context context) {
    User user = createUser(context);
    Optional<Account> account = createAccount(context, user);

    if (!account.isPresent()) {
      Response response = Response.builder().message("Invalid money format").build();
      contextWrapper.json(context, response);
      return;
    }

    try {
      accountRepository.create(account.get());
      Response response = Response.builder()
          .message("account created")
          .object(account.get())
          .build();

      contextWrapper.json(context, response);
    } catch (Exception exception) {
      Response response = Response.builder().message(exception.getMessage()).build();
      contextWrapper.json(context, response);
    }
  }

  private User createUser(Context context) {
    return User.builder()
        .id(UUID.randomUUID().toString())
        .name(contextWrapper.formParam(context, "name"))
        .surname(contextWrapper.formParam(context, "surname"))
        .build();
  }

  private Optional<Account> createAccount(Context context, User user) {
    return parseMoney(context)
        .map(money -> Account.builder()
            .number(UUID.randomUUID().toString())
            .user(user)
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
      responses = @OpenApiResponse(status = "200", content = @OpenApiContent(from = Response.class))
  )
  public void delete(Context context) {
    try {
      accountRepository.delete(contextWrapper.pathParam(context, "id"));

      String message = String.format(
          "account with number %s deleted",
          contextWrapper.pathParam(context, "id")
      );

      Response response = Response.builder()
          .message(message)
          .build();

      contextWrapper.json(context, response);
    } catch (Exception exception) {
      Response response = Response.builder().message(exception.getMessage()).build();
      contextWrapper.json(context, response);
    }
  }
}
