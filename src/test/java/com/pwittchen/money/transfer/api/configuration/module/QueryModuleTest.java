package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.query.GetAccountQuery;
import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetAccountQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetTransactionQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class QueryModuleTest {

  private QueryModule queryModule = new QueryModule();

  @Mock private AccountRepository accountRepository;

  @Mock private TransactionRepository transactionRepository;

  @Test public void shouldProvideGetAccountQuery() {
    // when
    GetAccountQuery query = queryModule.provideGetAccountQuery(accountRepository);

    // then
    assertThat(query).isNotNull();
    assertThat(query).isInstanceOf(DefaultGetAccountQuery.class);
  }

  @Test public void shouldProvideGetAllAccountsQuery() {
    // when
    GetAllAccountsQuery query = queryModule.provideGetAllAccountsQuery(accountRepository);

    // then
    assertThat(query).isNotNull();
    assertThat(query).isInstanceOf(DefaultGetAllAccountsQuery.class);
  }

  @Test public void shouldProvideGetTransactionQuery() {
    // when
    GetTransactionQuery query = queryModule.provideGetTransactionQuery(transactionRepository);

    // then
    assertThat(query).isNotNull();
    assertThat(query).isInstanceOf(DefaultGetTransactionQuery.class);
  }

  @Test public void shouldProvideGetAllTransactionsQuery() {
    // when
    GetAllTransactionsQuery query = queryModule.provideGetAllTransactionsQuery(
        transactionRepository
    );

    // then
    assertThat(query).isNotNull();
    assertThat(query).isInstanceOf(DefaultGetAllTransactionsQuery.class);
  }
}