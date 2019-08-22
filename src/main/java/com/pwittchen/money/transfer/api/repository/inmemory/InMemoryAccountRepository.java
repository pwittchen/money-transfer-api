package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.exception.EmptyUserIdException;
import com.pwittchen.money.transfer.api.exception.EmptyUserNameException;
import com.pwittchen.money.transfer.api.exception.EmptyUserSurnameException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.joda.money.Money;

public class InMemoryAccountRepository implements AccountRepository {

  private final Map<String, Account> accounts = new HashMap<>();

  @Override public Optional<Account> get(String number) {
    return Optional.ofNullable(accounts.get(number));
  }

  @Override public List<Account> getAll() {
    return new ArrayList<>(accounts.values());
  }

  @Override public Account create(Account account) throws Exception {
    if (account.number() == null || account.number().isEmpty()) {
      throw new EmptyAccountNumberException();
    }

    if (accounts.containsKey(account.number())) {
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

    accounts.put(account.number(), account);
    return account;
  }

  @Override public void withdrawMoney(final Account account, final Money money) {
    if (!accounts.containsKey(account.number())) {
      throw new AccountNotExistsException(account.number());
    }

    final Account updatedAccount = Account
        .builder()
        .number(account.number())
        .user(account.user())
        .money(account.money().minus(money))
        .build();

    accounts.put(account.number(), updatedAccount);
  }

  @Override public void putMoney(final Account account, Money money) {
    if (!accounts.containsKey(account.number())) {
      throw new AccountNotExistsException(account.number());
    }

    final Account updatedAccount = Account
        .builder()
        .number(account.number())
        .user(account.user())
        .money(account.money().plus(money))
        .build();

    accounts.put(account.number(), updatedAccount);
  }

  @Override public void delete(String number) {
    if (number == null || number.isEmpty()) {
      throw new EmptyAccountNumberException();
    }

    if (!accounts.containsKey(number)) {
      throw new AccountNotExistsException(number);
    }

    accounts.remove(number);
  }

  @Override public void clear() {
    accounts.clear();
  }
}
