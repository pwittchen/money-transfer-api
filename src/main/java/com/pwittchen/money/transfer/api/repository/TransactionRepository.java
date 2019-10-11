package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.concurrent.BlockingQueue;

public interface TransactionRepository {

  BlockingQueue<Transaction> getAll();

  Transaction create(Transaction transaction);

  void clear();
}
