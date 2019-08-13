package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Account;
import java.util.Map;
import java.util.Optional;
import org.joda.money.Money;

public interface AccountRepository {
  Optional<Account> get(String number);

  Map<String, Account> get();

  Account create(Account account) throws Exception;

  void withdrawMoney(Account account, Money money);

  void putMoney(Account account, Money money);

  void delete(String number);

  void clear();
}
