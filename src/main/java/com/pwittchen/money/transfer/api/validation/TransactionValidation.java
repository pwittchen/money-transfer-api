package com.pwittchen.money.transfer.api.validation;

import com.pwittchen.money.transfer.api.model.Transaction;

public interface TransactionValidation {
  boolean canCommit(Transaction transaction);
}
