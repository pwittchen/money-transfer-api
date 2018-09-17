package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Response;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.Context;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.joda.money.Money;

public class TransactionController {

  private TransactionRepository transactionRepository;
  private AccountRepository accountRepository;

  @Inject
  public TransactionController(
      TransactionRepository transactionRepository,
      AccountRepository accountRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }

  public void getOne(final Context context) {
    Optional<Transaction> transaction = transactionRepository.get(context.pathParam("id"));

    if (transaction.isPresent()) {
      context.json(transaction);
    } else {
      context
          .status(404)
          .json(Response.builder().message(String.format(
              "transaction with id %s does not exist", context.pathParam("id")
          )).build());
    }
  }

  public void getAll(final Context context) {
    context.json(transactionRepository.get());
  }

  public void commit(final Context context) {
    Optional<Account> senderAccount = accountRepository.get(context.formParam("from"));
    Optional<Account> receiverAccount = accountRepository.get(context.formParam("to"));

    if (!senderAccount.isPresent() || !receiverAccount.isPresent()) {
      createInvalidAccountResponse(context);
      return;
    }

    Optional<Money> money = parseMoney(context);

    if (!money.isPresent()) {
      createInvalidCurrencyFormatResponse(context);
      return;
    }

    commit(context, createTransaction(senderAccount.get(), receiverAccount.get(), money.get()));
  }

  void createInvalidAccountResponse(Context context) {
    context.json(Response.builder()
        .message("Trying to transfer money from or to account, which does not exist")
        .build());
  }

  Optional<Money> parseMoney(Context context) {
    try {
      return Optional.of(Money.parse(String.format("%s %s",
          context.formParam("currency"),
          context.formParam("money"))
      ));
    } catch (Exception e) {
      return Optional.empty();
    }
  }

  void createInvalidCurrencyFormatResponse(Context context) {
    context.json(Response.builder()
        .message(String.format(
            "%s is invalid currency format", context.formParam("currency"))
        )
        .build());
  }

  Transaction createTransaction(Account sender, Account receiver, Money money) {
    return Transaction.builder()
        .id(UUID.randomUUID().toString())
        .from(sender)
        .to(receiver)
        .money(money)
        .build();
  }

  void commit(final Context context, final Transaction transaction) {
    try {
      transactionRepository.commit(transaction);
      context.json(Response.builder().message("transaction committed").object(transaction).build());
    } catch (Exception exception) {
      context.json(Response.builder().message(exception.getMessage()).build());
    }
  }
}
