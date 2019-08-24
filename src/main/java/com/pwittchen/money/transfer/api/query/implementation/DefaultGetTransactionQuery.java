package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.Optional;
import javax.inject.Inject;

//todo: write tests
public class DefaultGetTransactionQuery implements GetTransactionQuery {

  private TransactionRepository transactionRepository;

  @Inject
  public DefaultGetTransactionQuery(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override public Optional<Transaction> run(String id) {
    return transactionRepository.get(id);
  }
}
