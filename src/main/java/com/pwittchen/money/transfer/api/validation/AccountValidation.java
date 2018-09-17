package com.pwittchen.money.transfer.api.validation;

import com.pwittchen.money.transfer.api.model.Account;
import java.util.Optional;

public interface AccountValidation {
  Optional<Exception> validate(Account account);
}
