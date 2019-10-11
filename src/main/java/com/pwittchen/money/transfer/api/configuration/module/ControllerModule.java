package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.controller.context.DefaultContextWrapper;
import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class ControllerModule {

  @Provides
  @Singleton
  ContextWrapper provideContextWrapper() {
    return new DefaultContextWrapper();
  }

  @Inject
  @Provides
  @Singleton
  AccountController provideAccountController(
      final ContextWrapper contextWrapper,
      final GetAllAccountsQuery getAllAccountsQuery,
      final CreateAccountCommand createAccountCommand
  ) {
    return new AccountController(
        contextWrapper,
        getAllAccountsQuery,
        createAccountCommand
    );
  }

  @Inject
  @Provides
  @Singleton
  TransactionController provideTransactionController(
      final ContextWrapper contextWrapper,
      final GetAllTransactionsQuery getAllTransactionsQuery,
      final CommitTransactionCommand commitTransactionCommand
  ) {
    return new TransactionController(
        contextWrapper,
        getAllTransactionsQuery,
        commitTransactionCommand
    );
  }
}
