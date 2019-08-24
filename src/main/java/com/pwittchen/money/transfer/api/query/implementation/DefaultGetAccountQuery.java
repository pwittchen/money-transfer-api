package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.query.GetAccountQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import java.util.Optional;

//todo: write tests
public class DefaultGetAccountQuery implements GetAccountQuery {

  private AccountRepository accountRepository;

  public DefaultGetAccountQuery(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public Optional<Account> run(String number) {
    return accountRepository.get(number);
  }
}
