package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import com.pwittchen.money.transfer.api.validation.implementation.DefaultTransactionValidation;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;

@Module
public class ValidationModule {

  @Inject
  @Provides
  TransactionValidation provideTransactionValidation(final AccountRepository accountRepository) {
    return new DefaultTransactionValidation(accountRepository);
  }
}
