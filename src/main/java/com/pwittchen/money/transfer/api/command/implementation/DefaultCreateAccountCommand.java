package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountOwnerException;
import com.pwittchen.money.transfer.api.command.exception.NegativeMoneyValueException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import javax.inject.Inject;

public class DefaultCreateAccountCommand implements CreateAccountCommand {

  private AccountRepository accountRepository;

  @Inject public DefaultCreateAccountCommand(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public void run(final Account account) {
    validateAccount(account);
    accountRepository.create(account);
  }

  private void validateAccount(final Account account) {
    if (account.number() == null || account.number().isEmpty()) {
      throw new EmptyAccountNumberException();
    }

    if (account.money().isNegative()) {
      throw new NegativeMoneyValueException();
    }

    if (accountRepository
        .getAll()
        .stream()
        .anyMatch(item -> item.number().equals(account.number()))) {
      throw new AccountAlreadyExistsException(account.number());
    }

    if (account.owner() == null || account.owner().isEmpty()) {
      throw new EmptyAccountOwnerException();
    }
  }
}
