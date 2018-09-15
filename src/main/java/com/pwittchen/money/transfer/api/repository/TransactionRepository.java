package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Transaction;
import io.reactivex.Completable;
import java.util.List;

public interface TransactionRepository {
  Completable commit(Transaction transaction);

  Completable revert(String id);

  List<Transaction> list();
}
