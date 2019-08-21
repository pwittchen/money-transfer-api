package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Account;
import java.util.List;
import java.util.Optional;
import org.joda.money.Money;

public interface AccountRepository {
  Optional<Account> get(String number);

  List<Account> getAll();

  Account create(Account account) throws Exception;

  void withdrawMoney(Account account, Money money);

  void putMoney(Account account, Money money);

  void delete(String number);

  void clear();
}
