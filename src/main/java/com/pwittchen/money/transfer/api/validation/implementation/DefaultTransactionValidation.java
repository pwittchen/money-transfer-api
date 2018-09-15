package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;

public class DefaultTransactionValidation implements TransactionValidation {

  @Override public boolean canCommit(Transaction transaction) {
    return true; //TODO: implement
  }

  @Override public boolean canRevert(String id) {
    return true; //TODO: implement
  }
}
