package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {

  private final Map<String, Account> accounts = new HashMap<>();

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

  //TODO: consider moving whole validation to separate class like in transactions

  @Override public Account create(Account account) {
    if (accounts.containsKey(account.number())) {
      throw new AccountAlreadyExistsException(account.number());
    } else {
      accounts.put(account.number(), account);
      return account;
    }
  }

  @Override public Account update(String number, Account account) {
    if (accounts.containsKey(number)) {
      accounts.remove(number);
      accounts.put(account.number(), account);
      return account;
    } else {
      throw new AccountNotExistsException(account.number());
    }
  }

  @Override public void delete(String number) {
    if (number != null && !number.isEmpty() && accounts.containsKey(number)) {
      accounts.remove(number);
    } else {
      throw new AccountNotExistsException(number);
    }
  }
}
