package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.command.exception.DifferentCurrencyException;
import com.pwittchen.money.transfer.api.command.exception.NegativeMoneyValueException;
import com.pwittchen.money.transfer.api.command.exception.NotEnoughMoneyException;
import com.pwittchen.money.transfer.api.command.exception.TransferToTheSameAccountException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

public class DefaultCommitTransactionCommand implements CommitTransactionCommand {

  private static final Random random = new Random();
  private static final long FIXED_DELAY = 1;
  private static final long RANDOM_DELAY = 2;
  private static final long TIMEOUT = TimeUnit.SECONDS.toNanos(2);

  private AccountRepository accountRepository;
  private TransactionRepository transactionRepository;

  @Inject public DefaultCommitTransactionCommand(
      AccountRepository accountRepository,
      TransactionRepository transactionRepository) {
    this.accountRepository = accountRepository;
    this.transactionRepository = transactionRepository;
  }

  @Override public void run(final Transaction transaction) {
    while (transaction.isRunning().get()) {
      Account sender, receiver;
      synchronized (this) {
        validateTransaction(transaction);
        sender = getSender(transaction.from());
        receiver = getReceiver(transaction.to());
      }
      if (sender.lock().tryLock()) {
        try {
          if (receiver.lock().tryLock()) {
            try {
              accountRepository.transfer(sender, receiver, transaction.money());
              transactionRepository.create(transaction);
              transaction.isRunning().set(false);
            } finally {
              receiver.lock().unlock();
            }
          }
        } finally {
          sender.lock().unlock();
        }
      }
      long stopTime = System.nanoTime() + TIMEOUT;

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
  }

  private void validateTransaction(final Transaction transaction) {
    final Account sender = getSender(transaction.from());
    final Account receiver = getReceiver(transaction.to());

    if (transaction.money().isNegative()) {
      throw new NegativeMoneyValueException();
    }
    if (sender.money().isLessThan(transaction.money())) {
      throw new NotEnoughMoneyException(sender.number());
    }
    if (!sender.money().isSameCurrency(receiver.money())) {
      throw new DifferentCurrencyException(transaction.from(), transaction.to());
    }
    if (transaction.from().equals(transaction.to())) {
      throw new TransferToTheSameAccountException();
    }
  }

  @NotNull private Account getSender(final String number) {
    final Optional<Account> from = accountRepository.get(number);
    if (from.isPresent()) {
      return from.get();
    } else {
      throw new AccountNotExistsException(number);
    }
  }

  @NotNull private Account getReceiver(final String number) {
    final Optional<Account> to = accountRepository.get(number);
    if (to.isPresent()) {
      return to.get();
    } else {
      throw new AccountNotExistsException(number);
    }
  }
}
