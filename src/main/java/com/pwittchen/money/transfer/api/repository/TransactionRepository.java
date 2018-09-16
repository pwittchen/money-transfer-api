package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.Optional;
import java.util.Queue;

public interface TransactionRepository {
  Optional<Transaction> get(String id);

  Queue<Transaction> get();

  Transaction commit(Transaction transaction) throws Exception;
}
