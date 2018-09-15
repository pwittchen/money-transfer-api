package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.repository.exception.DifferentCurrencyException;
import com.pwittchen.money.transfer.api.repository.exception.NotEnoughMoneyException;
import io.reactivex.Completable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.joda.money.Money;

public class InMemoryTransactionRepository implements TransactionRepository {

  private final Queue<Transaction> transactions = new LinkedList<>();
  private final AccountRepository accountRepository;

  public InMemoryTransactionRepository(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public Completable commit(Transaction transaction) {
    return Completable.create(emitter -> {

      //TODO: move this validation somewhere else
      boolean senderAccountExists = accountRepository.get(transaction.from().number()).isPresent();

      if (!senderAccountExists) {
        emitter.onError(new AccountNotExistsException(transaction.from().number()));
        return;
      }

      boolean receiverAccountExists = accountRepository.get(transaction.to().number()).isPresent();

      if (!receiverAccountExists) {
        emitter.onError(new AccountNotExistsException(transaction.to().number()));
        return;
      }

      if (getSenderBalance(transaction).isLessThan(getMoneyWithTransactionFee(transaction))) {
        emitter.onError(new NotEnoughMoneyException(transaction.from().number()));
        return;
      }

      if (!transaction.from().money().isSameCurrency(transaction.to().money())) {
        emitter.onError(
            new DifferentCurrencyException(
                transaction.from().number(),
                transaction.to().number()
            )
        );
        return;
      }

      //TODO: use DI

      Account sender = accountRepository.get(transaction.from().number()).get();
      sender.withdraw(getMoneyWithTransactionFee(transaction));

      Account receiver = accountRepository.get(transaction.to().number()).get();
      receiver.put(transaction.money());

      transactions.add(transaction);
      emitter.onComplete();
    });
  }

  @NotNull private Money getSenderBalance(Transaction transaction) {
    return transaction
        .from()
        .money()
        .plus(transaction.from().allowedDebit());
  }

  @NotNull private Money getMoneyWithTransactionFee(Transaction transaction) {
    return transaction
        .money()
        .plus(transaction.fee());
  }

  @Override public Optional<Transaction> get(String id) {
    return transactions
        .stream()
        .filter(transaction -> transaction.id().equals(id))
        .findFirst();
  }

  @Override public Queue<Transaction> get() {
    return transactions;
  }
}
