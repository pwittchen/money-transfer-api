package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.validation.AccountValidation;

public class DefaultAccountValidation implements AccountValidation {
  @Override public boolean canCreate(Account account) {
    return true; //TODO: implement
  }

  @Override public boolean canUpdate(Account account) {
    return true; //TODO: implement
  }

  @Override public boolean canDelete(String number) {
    return true; //TODO: implement
  }
}
