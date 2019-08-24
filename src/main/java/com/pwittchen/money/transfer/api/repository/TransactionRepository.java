package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;

public interface TransactionRepository {
  Optional<Transaction> get(String id);

  BlockingQueue<Transaction> getAll();

  Transaction create(Transaction transaction);

  void clear();
}
