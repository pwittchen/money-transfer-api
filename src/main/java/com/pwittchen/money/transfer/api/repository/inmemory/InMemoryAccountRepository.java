package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import io.reactivex.Completable;
import java.util.List;
import java.util.Optional;

public class InMemoryAccountRepository implements AccountRepository {
  @Override public Optional<Account> get(String number) {
    return Optional.empty(); //TODO: implement
  }

  @Override public List<Account> get() {
    return null; //TODO: implement
  }

  @Override public Completable create(Account account) {
    return null; //TODO: implement
  }

  @Override public Completable update(String number, Account account) {
    return null; //TODO: implement
  }

  @Override public Completable delete(String number) {
    return null; //TODO: implement
  }
}
