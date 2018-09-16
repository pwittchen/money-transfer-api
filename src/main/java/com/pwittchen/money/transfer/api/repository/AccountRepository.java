package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Account;
import java.util.Map;
import java.util.Optional;

public interface AccountRepository {
  Optional<Account> get(String number);

  Map<String, Account> get();

  Account create(Account account);

  Account update(String number, Account account);

  void delete(String number);
}
