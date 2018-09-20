package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
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

  @Override public Optional<Transaction> get(String id) {
    return transactions
        .stream()
        .filter(transaction -> transaction.id().equals(id))
        .findFirst();
  }

  @Override public Queue<Transaction> get() {
    return transactions;
  }

  @Override
  @SuppressWarnings("OptionalGetWithoutIsPresent") // accounts are checked in transactionValidation
  public Transaction commit(Transaction transaction) throws Exception {
    Optional<Exception> error = transactionValidation.validate(transaction);

    if (error.isPresent()) {
      throw error.get();
    }

    Account sender = accountRepository.get(transaction.from().number()).get();
    Account receiver = accountRepository.get(transaction.to().number()).get();

    synchronized (sender) {
      sender.withdraw(transaction.money());
      synchronized (receiver) {
        receiver.put(transaction.money());
        transactions.add(transaction);
      }
    }

    return transaction;
  }

  @Override public void clear() {
    transactions.clear();
  }
}
