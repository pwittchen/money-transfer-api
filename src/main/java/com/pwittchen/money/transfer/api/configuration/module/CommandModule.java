package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.DeleteAccountCommand;
import com.pwittchen.money.transfer.api.command.implementation.DefaultCommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.implementation.DefaultCreateAccountCommand;
import com.pwittchen.money.transfer.api.command.implementation.DefaultDeleteAccountCommand;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class CommandModule {

  @Inject
  @Provides
  @Singleton
  CommitTransactionCommand provideCommitTransactionCommand(
      AccountRepository accountRepository,
      TransactionRepository transactionRepository) {
    return new DefaultCommitTransactionCommand(accountRepository, transactionRepository);
  }

  @Inject
  @Provides
  @Singleton
  CreateAccountCommand provideCreateAccountCommand(AccountRepository accountRepository) {
    return new DefaultCreateAccountCommand(accountRepository);
  }

  @Inject
  @Provides
  @Singleton
  DeleteAccountCommand provideDeleteAccountCommand(AccountRepository accountRepository) {
    return new DefaultDeleteAccountCommand(accountRepository);
  }
}
