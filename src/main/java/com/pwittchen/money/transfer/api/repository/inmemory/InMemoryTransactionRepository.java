package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class InMemoryTransactionRepository implements TransactionRepository {

  private final BlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();

  @Override public BlockingQueue<Transaction> getAll() {
    return transactions;
  }

  @Override public Transaction create(Transaction transaction) {
    transactions.add(transaction);
    return transaction;
  }

  @Override public void clear() {
    transactions.clear();
  }
}
