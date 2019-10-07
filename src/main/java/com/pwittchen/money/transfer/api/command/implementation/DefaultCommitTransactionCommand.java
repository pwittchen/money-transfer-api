package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.command.exception.DifferentCurrencyException;
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

  @Override public void run(Transaction transaction) {
    Account sender;
    Account receiver;
    long stopTime = System.nanoTime() + TIMEOUT;

    while (transaction.isRunning().get()) {
      synchronized (this) {
        validateTransaction(transaction);
        sender = getSender(transaction.fromNumber());
        receiver = getReceiver(transaction.toNumber());
        validateSenderBalance(transaction, sender);
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

  private void validateTransaction(Transaction transaction) {
    final Account sender = getSender(transaction.fromNumber());
    final Account receiver = getReceiver(transaction.toNumber());

    if (!sender.money().isSameCurrency(receiver.money())) {
      throw new DifferentCurrencyException(
          transaction.fromNumber(),
          transaction.toNumber()
      );
    }

    if (transaction.fromNumber().equals(transaction.toNumber())) {
      throw new TransferToTheSameAccountException();
    }
  }

  @NotNull private Account getSender(String number) {
    final Optional<Account> from = accountRepository.get(number);
    if (from.isPresent()) {
      return from.get();
    } else {
      throw new AccountNotExistsException(number);
    }
  }

  @NotNull private Account getReceiver(String number) {
    final Optional<Account> to = accountRepository.get(number);
    if (to.isPresent()) {
      return to.get();
    } else {
      throw new AccountNotExistsException(number);
    }
  }

  private void validateSenderBalance(Transaction transaction, Account sender) {
    if (sender.money().isLessThan(transaction.money())) {
      throw new NotEnoughMoneyException(sender.number());
    }
  }
}
