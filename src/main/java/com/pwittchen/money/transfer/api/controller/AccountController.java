package com.pwittchen.money.transfer.api.controller;

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

  @Inject
  public AccountController(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  public void getOne(final Context context) {
    Optional<Account> account = accountRepository.get(context.pathParam("id"));

    if (account.isPresent()) {
      context.json(account);
    } else {
      context
          .status(404)
          .json(Response.builder().message(String.format(
              "account with id %s does not exist", context.pathParam("id")
          )).build());
    }
  }

  public void getAll(final Context context) {
    context.json(accountRepository.get());
  }

  public void create(final Context context) {
    final User user = createUser(context);

    Optional<Account> account = createAccount(context, user);

    if (!account.isPresent()) {
      context.json(Response.builder().message("Invalid currency format").build());
      return;
    }

    try {
      accountRepository.create(account.get());
      context.json(Response.builder().message("account created").object(account).build());
    } catch (Exception exception) {
      context.json(Response.builder().message(exception.getMessage()).build());
    }
  }

  User createUser(Context context) {
    return User.builder()
        .id(UUID.randomUUID().toString())
        .name(context.formParam("name"))
        .surname(context.formParam("surname"))
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
          context.formParam("currency"),
          context.formParam("money"))
      ));
    } catch (Exception exception) {
      return Optional.empty();
    }
  }

  public void delete(Context context) {
    try {
      accountRepository.delete(context.formParam("id"));
      context.json(Response.builder().message(
          String.format("account with id %s deleted", context.formParam("id"))
      ).build());
    } catch (Exception exception) {
      context.json(Response.builder().message(exception.getMessage()).build());
    }
  }
}
