package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;

@Module
public class ControllerModule {

  @Inject
  @Provides
  AccountController provideAccountController(final AccountRepository accountRepository) {
    return new AccountController(accountRepository);
  }

  @Inject
  @Provides
  TransactionController provideTransactionController(
      final TransactionRepository transactionRepository,
      final AccountRepository accountRepository
  ) {
    return new TransactionController(transactionRepository, accountRepository);
  }
}
