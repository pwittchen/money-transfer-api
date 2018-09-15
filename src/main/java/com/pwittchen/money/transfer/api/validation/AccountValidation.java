package com.pwittchen.money.transfer.api.validation;

import com.pwittchen.money.transfer.api.model.Account;

public interface AccountValidation {
  boolean canCreate(Account account);

  boolean canUpdate(Account account);

  boolean canDelete(String number);
}
