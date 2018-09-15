package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.reactivex.Completable;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {
  @Override public Completable commit(Transaction transaction) {
    return null; //TODO: implement
  }

  @Override public Completable revert(String id) {
    return null; //TODO: implement
  }

  @Override public List<Transaction> list() {
    return null; //TODO: implement
  }
}
