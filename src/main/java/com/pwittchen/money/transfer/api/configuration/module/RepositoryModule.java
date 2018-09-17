package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryAccountRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryTransactionRepository;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class RepositoryModule {

  @Inject
  @Provides
  @Singleton
  AccountRepository provideAccountRepository(final AccountValidation accountValidation) {
    return new InMemoryAccountRepository(accountValidation);
  }

  @Inject
  @Provides
  @Singleton
  TransactionRepository provideTransactionRepository(
      final AccountRepository accountRepository,
      final TransactionValidation transactionValidation
  ) {
    return new InMemoryTransactionRepository(accountRepository, transactionValidation);
  }
}
