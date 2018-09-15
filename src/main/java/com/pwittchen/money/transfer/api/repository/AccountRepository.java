package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Account;
import io.reactivex.Completable;
import java.util.Map;
import java.util.Optional;

public interface AccountRepository {
  Optional<Account> get(String number);

  Map<String, Account> get();

  Completable create(Account account);

  Completable update(String number, Account account);

  Completable delete(String number);
}
