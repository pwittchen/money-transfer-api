package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.Optional;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public interface TransactionRepository {
  Optional<Transaction> get(String id);

  BlockingQueue<Transaction> get();

  Transaction commit(Transaction transaction) throws Exception;

  void clear();
}
