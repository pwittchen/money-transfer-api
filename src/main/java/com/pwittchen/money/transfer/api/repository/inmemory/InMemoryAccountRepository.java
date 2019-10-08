package com.pwittchen.money.transfer.api.repository.inmemory;

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

  @Override public Account create(Account account) {
    accounts.put(account.number(), account);
    return account;
  }

  @Override
  public synchronized void transfer(final Account from, final Account to, final Money money) {
    withdraw(from, money);
    deposit(to, money);
  }

  private void withdraw(final Account account, final Money money) {
    final Account updatedAccount = Account
        .builder()
        .number(account.number())
        .user(account.user())
        .money(account.money().minus(money))
        .build();

    accounts.put(account.number(), updatedAccount);
  }

  private void deposit(final Account account, final Money money) {
    final Account updatedAccount = Account
        .builder()
        .number(account.number())
        .user(account.user())
        .money(account.money().plus(money))
        .build();

    accounts.put(account.number(), updatedAccount);
  }

  @Override public void delete(String number) {
    accounts.remove(number);
  }

  @Override public void clear() {
    accounts.clear();
  }
}
