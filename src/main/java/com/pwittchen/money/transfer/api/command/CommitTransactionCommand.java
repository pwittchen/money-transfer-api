package com.pwittchen.money.transfer.api.command;

import com.pwittchen.money.transfer.api.model.Transaction;

public interface CommitTransactionCommand {
  void run(Transaction transaction);
}
