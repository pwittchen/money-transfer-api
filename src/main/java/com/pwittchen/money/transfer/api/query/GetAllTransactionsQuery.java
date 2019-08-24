package com.pwittchen.money.transfer.api.query;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.concurrent.BlockingQueue;

public interface GetAllTransactionsQuery {
  BlockingQueue<Transaction> run();
}
