package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

public class InMemoryAccountRepository implements AccountRepository {

  private final Map<String, Account> accounts = new HashMap<>();
  private AccountValidation accountValidation;

  @Inject
  public InMemoryAccountRepository(AccountValidation accountValidation) {
    this.accountValidation = accountValidation;
  }

  @Override public Optional<Account> get(String number) {
    final Account account = accounts.get(number);
    if (account == null) {
      return Optional.empty();
    }
    return Optional.of(account);
  }

  @Override public Map<String, Account> get() {
    return accounts;
  }

  @Override public Account create(Account account) throws Exception {
    if (accounts.containsKey(account.number())) {
      throw new AccountAlreadyExistsException(account.number());
    }

    Optional<Exception> error = accountValidation.validate(account);

    if (error.isPresent()) {
      throw error.get();
    }

    accounts.put(account.number(), account);
    return account;
  }

  @Override public Account update(String number, Account account) throws Exception {
    if (!accounts.containsKey(number)) {
      throw new AccountNotExistsException(account.number());
    }

    Optional<Exception> error = accountValidation.validate(account);

    if (error.isPresent()) {
      throw error.get();
    }

    accounts.remove(number);
    accounts.put(account.number(), account);
    return account;
  }

  @Override public void delete(String number) {
    if (number == null || number.isEmpty() || !accounts.containsKey(number)) {
      throw new AccountNotExistsException(number);
    }

    accounts.remove(number);
  }
}
