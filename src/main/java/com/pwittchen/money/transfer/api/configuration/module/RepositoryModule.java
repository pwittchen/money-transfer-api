package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryAccountRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryTransactionRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;

@Module
public class RepositoryModule {

  @Provides
  AccountRepository provideAccountRepository() {
    return new InMemoryAccountRepository();
  }

  @Inject
  @Provides
  TransactionRepository provideTransactionRepository(
      final AccountRepository accountRepository,
      final TransactionValidation transactionValidation
  ) {
    return new InMemoryTransactionRepository(accountRepository, transactionValidation);
  }
}
