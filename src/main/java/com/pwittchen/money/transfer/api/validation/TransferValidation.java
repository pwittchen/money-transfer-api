package com.pwittchen.money.transfer.api.validation;

import com.pwittchen.money.transfer.api.model.Account;
import org.joda.money.Money;

public interface TransferValidation {
  boolean canTransfer(Money money, Account from, Account to);
}
