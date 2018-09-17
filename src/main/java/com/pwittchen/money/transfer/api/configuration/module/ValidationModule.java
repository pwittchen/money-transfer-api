package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import com.pwittchen.money.transfer.api.validation.implementation.DefaultAccountValidation;
import com.pwittchen.money.transfer.api.validation.implementation.DefaultTransactionValidation;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class ValidationModule {

  @Inject
  @Provides
  @Singleton
  TransactionValidation provideTransactionValidation(final AccountRepository accountRepository) {
    return new DefaultTransactionValidation(accountRepository);
  }

  @Provides
  @Singleton
  AccountValidation provideAccountValidation() {
    return new DefaultAccountValidation();
  }
}
