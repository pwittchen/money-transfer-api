package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserIdException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserNameException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserSurnameException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultAccountValidation implements AccountValidation {

  @Override public Optional<Exception> validate(final Account account) {
    return createAccountValidationRules(account)
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey().equals(Boolean.TRUE))
        .findFirst()
        .map(Map.Entry::getValue);
  }

  private Map<Boolean, Exception> createAccountValidationRules(final Account account) {
    Map<Boolean, Exception> rules = new HashMap<>();

    rules.put(
        account.number() == null || account.number().isEmpty(),
        new AccountNotExistsException("null")
    );

    rules.put(
        account.user().id() == null || account.user().id().isEmpty(),
        new EmptyUserIdException()
    );

    rules.put(
        account.user().name() == null || account.user().name().isEmpty(),
        new EmptyUserNameException()
    );

    rules.put(
        account.user().surname() == null || account.user().surname().isEmpty(),
        new EmptyUserSurnameException()
    );

    return rules;
  }
}
