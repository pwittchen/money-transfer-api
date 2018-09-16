package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import javax.inject.Inject;

public class AccountController {

  private AccountRepository accountRepository;

  @Inject
  public AccountController(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }
}
