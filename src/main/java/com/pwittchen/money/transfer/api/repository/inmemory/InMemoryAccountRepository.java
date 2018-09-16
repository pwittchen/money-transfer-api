package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import io.reactivex.Completable;
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

  @Override public Completable create(Account account) {
    return Completable.create(emitter -> {
      if (accounts.containsKey(account.number())) {
        emitter.onError(new AccountAlreadyExistsException(account.number()));
      } else {
        accounts.put(account.number(), account);
        emitter.onComplete();
      }
    });
  }

  @Override public Completable update(String number, Account account) {
    return Completable.create(emitter -> {
      if (accounts.containsKey(number)) {
        accounts.remove(number);
        accounts.put(account.number(), account);
        emitter.onComplete();
      } else {
        emitter.onError(new AccountNotExistsException(account.number()));
      }
    });
  }

  @Override public Completable delete(String number) {
    return Completable.create(emitter -> {
      if (accounts.containsKey(number)) {
        accounts.remove(number);
        emitter.onComplete();
      } else {
        emitter.onError(new AccountNotExistsException(number));
      }
    });
  }
}
