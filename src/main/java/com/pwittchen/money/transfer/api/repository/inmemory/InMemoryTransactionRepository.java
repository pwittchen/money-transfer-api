package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import io.reactivex.Completable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.joda.money.Money;

public class InMemoryTransactionRepository implements TransactionRepository {

  private final Queue<Transaction> transactions = new LinkedList<>();
  private final AccountRepository accountRepository;
  private final TransactionValidation transactionValidation;

  public InMemoryTransactionRepository(final AccountRepository accountRepository,
      final TransactionValidation transactionValidation) {
    this.accountRepository = accountRepository;
    this.transactionValidation = transactionValidation;
  }

  @Override
  @SuppressWarnings("OptionalGetWithoutIsPresent") // transactionValidation verifies it earlier
  public Completable commit(Transaction transaction) {
    return Completable.create(emitter -> {
      Optional<Exception> error = transactionValidation.getCommitError(transaction);

      if (error.isPresent()) {
        emitter.onError(error.get());
        return;
      }

      Account sender = accountRepository.get(transaction.from().number()).get();
      sender.withdraw(getMoneyWithTransactionFee(transaction));

      Account receiver = accountRepository.get(transaction.to().number()).get();
      receiver.put(transaction.money());

      transactions.add(transaction);
      emitter.onComplete();
    });
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
