package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.concurrent.BlockingQueue;
import javax.inject.Inject;

public class DefaultGetAllTransactionsQuery implements GetAllTransactionsQuery {

  private TransactionRepository transactionRepository;

  @Inject
  public DefaultGetAllTransactionsQuery(TransactionRepository transactionRepository) {
    this.transactionRepository = transactionRepository;
  }

  @Override public BlockingQueue<Transaction> run() {
    return transactionRepository.getAll();
  }
}
