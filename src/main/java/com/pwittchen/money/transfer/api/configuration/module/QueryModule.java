package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class QueryModule {

  @Inject
  @Provides
  @Singleton
  GetAllAccountsQuery provideGetAllAccountsQuery(AccountRepository accountRepository) {
    return new DefaultGetAllAccountsQuery(accountRepository);
  }

  @Inject
  @Provides
  @Singleton
  GetAllTransactionsQuery provideGetTransactionQuery(TransactionRepository transactionRepository) {
    return new DefaultGetAllTransactionsQuery(transactionRepository);
  }
}
