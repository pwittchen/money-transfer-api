package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryAccountRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryTransactionRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class RepositoryModule {

  @Inject
  @Provides
  @Singleton
  AccountRepository provideAccountRepository() {
    return new InMemoryAccountRepository();
  }

  @Inject
  @Provides
  @Singleton
  TransactionRepository provideTransactionRepository(final AccountRepository accountRepository) {
    return new InMemoryTransactionRepository(accountRepository);
  }
}
