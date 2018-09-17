package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class ControllerModule {

  @Inject
  @Provides
  @Singleton
  AccountController provideAccountController(final AccountRepository accountRepository) {
    return new AccountController(accountRepository);
  }

  @Inject
  @Provides
  @Singleton
  TransactionController provideTransactionController(
      final TransactionRepository transactionRepository,
      final AccountRepository accountRepository
  ) {
    return new TransactionController(transactionRepository, accountRepository);
  }
}
