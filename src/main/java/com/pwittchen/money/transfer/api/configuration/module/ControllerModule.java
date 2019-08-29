package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.DeleteAccountCommand;
import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.controller.context.DefaultContextWrapper;
import com.pwittchen.money.transfer.api.query.GetAccountQuery;
import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
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
      final GetAccountQuery getAccountQuery,
      final GetAllAccountsQuery getAllAccountsQuery,
      final CreateAccountCommand createAccountCommand,
      final DeleteAccountCommand deleteAccountCommand
  ) {
    return new AccountController(
        contextWrapper,
        getAccountQuery,
        getAllAccountsQuery,
        createAccountCommand,
        deleteAccountCommand
    );
  }

  @Inject
  @Provides
  @Singleton
  TransactionController provideTransactionController(
      final ContextWrapper contextWrapper,
      final GetTransactionQuery getTransactionQuery,
      final GetAllTransactionsQuery getAllTransactionsQuery,
      final CommitTransactionCommand commitTransactionCommand
  ) {
    return new TransactionController(
        contextWrapper,
        getTransactionQuery,
        getAllTransactionsQuery,
        commitTransactionCommand
    );
  }
}
