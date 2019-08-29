package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import java.util.List;
import javax.inject.Inject;

public class DefaultGetAllAccountsQuery implements GetAllAccountsQuery {

  private AccountRepository accountRepository;

  @Inject
  public DefaultGetAllAccountsQuery(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public List<Account> run() {
    return accountRepository.getAll();
  }
}
