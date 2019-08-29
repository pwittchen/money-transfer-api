package com.pwittchen.money.transfer.api.query;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.Optional;

public interface GetTransactionQuery {
  Optional<Transaction> run(String id);
}
