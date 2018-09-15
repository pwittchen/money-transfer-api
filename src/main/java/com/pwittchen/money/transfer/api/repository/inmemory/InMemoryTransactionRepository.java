package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.reactivex.Completable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class InMemoryTransactionRepository implements TransactionRepository {

  private final Queue<Transaction> transactions = new LinkedList<>();

  @Override public Completable commit(Transaction transaction) {
    return Completable.create(emitter -> {
      //TODO: add validation related to account balance
      transactions.add(transaction);
    });
  }

  @Override public Completable revert(String id) {
    return null; //TODO: implement -> add next 'revert' transaction
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
}
