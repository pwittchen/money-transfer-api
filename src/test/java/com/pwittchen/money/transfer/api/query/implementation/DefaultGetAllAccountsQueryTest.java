package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetAllAccountsQueryTest {

  private GetAllAccountsQuery query;

  @Mock private AccountRepository accountRepository;

  @Before public void setUp() {
    query = new DefaultGetAllAccountsQuery(accountRepository);
  }

  @Test public void shouldRunQuery() {
    // when
    query.run();

    // then
    verify(accountRepository).getAll();
  }
}