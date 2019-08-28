package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.DeleteAccountCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import javax.inject.Inject;

public class DefaultDeleteAccountCommand implements DeleteAccountCommand {

  private AccountRepository accountRepository;

  @Inject
  public DefaultDeleteAccountCommand(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public void run(String number) {
    validateAccount(number);
    accountRepository.delete(number);
  }

  private void validateAccount(String number) {
    if (number == null || number.isEmpty()) {
      throw new EmptyAccountNumberException();
    }

    if (accountRepository
        .getAll()
        .stream()
        .noneMatch(item -> item.number().equals(number))) {
      throw new AccountNotExistsException(number);
    }
  }
}
