package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.exception.DifferentCurrencyException;
import com.pwittchen.money.transfer.api.exception.NotEnoughMoneyException;
import com.pwittchen.money.transfer.api.exception.TransferToTheSameAccountException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class InMemoryTransactionRepository implements TransactionRepository {

  private static final Random random = new Random();
  private static final long FIXED_DELAY = 1;
  private static final long RANDOM_DELAY = 2;
  private static final long TIMEOUT = TimeUnit.SECONDS.toNanos(2);

  private final BlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();
  private AccountRepository accountRepository;

  @Inject
  public InMemoryTransactionRepository(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public Optional<Transaction> get(final String id) {
    return transactions
        .stream()
        .filter(transaction -> transaction.id().equals(id))
        .findFirst();
  }

  @Override public BlockingQueue<Transaction> getAll() {
    return transactions;
  }

  @Override
  public Transaction commit(final Transaction transaction) throws Exception {

    Account sender;
    Account receiver;

    long stopTime = System.nanoTime() + TIMEOUT;

    while (transaction.isRunning().get()) {
      synchronized (this) {

        if (!transaction.from().money().isSameCurrency(transaction.to().money())) {
          throw new DifferentCurrencyException(
              transaction.from().number(),
              transaction.to().number()
          );
        }

        if (transaction.from().number().equals(transaction.to().number())) {
          throw new TransferToTheSameAccountException();
        }

        final Optional<Account> from = accountRepository.get(transaction.from().number());
        if (from.isPresent()) {
          sender = from.get();
        } else {
          throw new AccountNotExistsException(transaction.from().number());
        }

        final Optional<Account> to = accountRepository.get(transaction.to().number());
        if (to.isPresent()) {
          receiver = to.get();
        } else {
          throw new AccountNotExistsException(transaction.to().number());
        }

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
              transaction.isRunning().set(false);
            } finally {
              receiver.lock().unlock();
            }
          }
        } finally {
          sender.lock().unlock();
        }
      }

      if (System.nanoTime() > stopTime) {
        transaction.isRunning().set(false);
      }
      try {
        TimeUnit.NANOSECONDS.sleep(FIXED_DELAY + random.nextLong() % RANDOM_DELAY);
      } catch (InterruptedException exception) {
        Thread.currentThread().interrupt();
        throw new RuntimeException(exception);
      }
    }

    return transaction;
  }

  @Override public void clear() {
    transactions.clear();
  }
}
