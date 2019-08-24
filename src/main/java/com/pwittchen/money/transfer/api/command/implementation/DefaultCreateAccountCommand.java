package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.command.exception.EmptyUserIdException;
import com.pwittchen.money.transfer.api.command.exception.EmptyUserNameException;
import com.pwittchen.money.transfer.api.command.exception.EmptyUserSurnameException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import javax.inject.Inject;

//todo: write tests
public class DefaultCreateAccountCommand implements CreateAccountCommand {

  private AccountRepository accountRepository;

  @Inject
  public DefaultCreateAccountCommand(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public Account run(Account account) {
    validateAccount(account);
    return accountRepository.create(account);
  }

  private void validateAccount(Account account) {
    if (account.number() == null || account.number().isEmpty()) {
      throw new EmptyAccountNumberException();
    }

    if (accountRepository
        .getAll()
        .stream()
        .anyMatch(item -> item.number().equals(account.number()))) {
      throw new AccountAlreadyExistsException(account.number());
    }

    if (account.user().id() == null || account.user().id().isEmpty()) {
      throw new EmptyUserIdException();
    }

    if (account.user().name() == null || account.user().name().isEmpty()) {
      throw new EmptyUserNameException();
    }

    if (account.user().surname() == null || account.user().surname().isEmpty()) {
      throw new EmptyUserSurnameException();
    }
  }
}
