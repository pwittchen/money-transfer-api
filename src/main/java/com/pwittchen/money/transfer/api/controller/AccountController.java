package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Response;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import io.javalin.Context;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.joda.money.Money;

public class AccountController {

  private AccountRepository accountRepository;
  private ContextWrapper contextWrapper;

  @Inject
  public AccountController(
      final AccountRepository accountRepository,
      final ContextWrapper contextWrapper) {
    this.accountRepository = accountRepository;
    this.contextWrapper = contextWrapper;
  }

  public void getOne(final Context context) {
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

  public void getAll(final Context context) {
    contextWrapper.json(context, accountRepository.get());
  }

  public void create(final Context context) {
    User user = createUser(context);
    Optional<Account> account = createAccount(context, user);

    if (!account.isPresent()) {
      Response response = Response.builder().message("Invalid currency format").build();
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

  User createUser(Context context) {
    return User.builder()
        .id(UUID.randomUUID().toString())
        .name(contextWrapper.formParam(context, "name"))
        .surname(contextWrapper.formParam(context, "surname"))
        .build();
  }

  Optional<Account> createAccount(Context context, User user) {
    return parseMoney(context)
        .map(money -> Account.builder()
            .number(UUID.randomUUID().toString())
            .user(user)
            .money(money)
            .build()
        );
  }

  Optional<Money> parseMoney(Context context) {
    try {
      return Optional.of(Money.parse(String.format("%s %s",
          contextWrapper.formParam(context, "currency"),
          contextWrapper.formParam(context, "money"))
      ));
    } catch (Exception exception) {
      return Optional.empty();
    }
  }

  public void delete(Context context) {
    try {
      accountRepository.delete(contextWrapper.formParam(context, "id"));

      String message = String.format(
          "account with id %s deleted",
          contextWrapper.formParam(context, "id")
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
