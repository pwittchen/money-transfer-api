package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.validation.TransferValidation;
import org.joda.money.Money;

public class DefaultTransferValidation implements TransferValidation {

  @Override public boolean canTransfer(Money money, Account from, Account to) {
    return true; //TODO: implement
  }
}
