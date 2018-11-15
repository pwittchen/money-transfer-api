package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import com.pwittchen.money.transfer.api.validation.exception.NotEnoughMoneyException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import javax.inject.Inject;

public class InMemoryTransactionRepository implements TransactionRepository {

  private final Queue<Transaction> transactions = new LinkedList<>();
  private AccountRepository accountRepository;
  private TransactionValidation transactionValidation;

  @Inject
  public InMemoryTransactionRepository(final AccountRepository accountRepository,
      final TransactionValidation transactionValidation) {
    this.accountRepository = accountRepository;
    this.transactionValidation = transactionValidation;
  }

  @Override public Optional<Transaction> get(final String id) {
    return transactions
        .stream()
        .filter(transaction -> transaction.id().equals(id))
        .findFirst();
  }

  @Override public Queue<Transaction> get() {
    return transactions;
  }

  @Override
  public Transaction commit(final Transaction transaction) throws Exception {

    Account sender;
    Account receiver;

    synchronized (this) {
      final Optional<Exception> error = transactionValidation.validate(transaction);
      if (error.isPresent()) {
        throw error.get();
      }

      //noinspection OptionalGetWithoutIsPresent
      sender = accountRepository.get(transaction.from().number()).get();
      //noinspection OptionalGetWithoutIsPresent
      receiver = accountRepository.get(transaction.to().number()).get();

      if (sender.money().isLessThan(transaction.money())) {
        throw new NotEnoughMoneyException(sender.number());
      }
    }

    if (sender.lock().tryLock()) {
      try {
        if (receiver.lock().tryLock()) {
          try {
            accountRepository.withdrawMoney(sender, transaction.money());
            accountRepository.putMoney(receiver, transaction.money());
            transactions.add(transaction);
          } finally {
            receiver.lock().unlock();
          }
        }
      } finally {
        sender.lock().unlock();
      }
    }

    return transaction;
  }

  @Override public void clear() {
    transactions.clear();
  }
}
