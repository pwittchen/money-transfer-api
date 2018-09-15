package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Transaction;
import io.reactivex.Completable;
import java.util.Optional;
import java.util.Queue;

public interface TransactionRepository {
  Completable commit(Transaction transaction);
  
  Optional<Transaction> get(String id);

  Queue<Transaction> get();
}
