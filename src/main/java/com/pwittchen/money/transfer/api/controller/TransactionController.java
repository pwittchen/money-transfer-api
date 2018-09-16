package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import javax.inject.Inject;

public class TransactionController {

  private TransactionRepository transactionRepository;
  private AccountRepository accountRepository;

  @Inject
  public TransactionController(
      TransactionRepository transactionRepository,
      AccountRepository accountRepository
  ) {
    this.transactionRepository = transactionRepository;
    this.accountRepository = accountRepository;
  }
}
