package com.pwittchen.money.transfer.api.repository;

import com.pwittchen.money.transfer.api.model.Account;
import io.reactivex.Completable;
import java.util.List;
import java.util.Optional;

public interface AccountRepository {
  Optional<Account> get(String number);

  List<Account> get();

  Completable create(Account account);

  Completable update(String number, Account account);

  Completable delete(String number);
}
