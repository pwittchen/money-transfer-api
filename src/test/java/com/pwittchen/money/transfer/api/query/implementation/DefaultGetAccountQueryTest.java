package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.query.GetAccountQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetAccountQueryTest {

  private GetAccountQuery query;

  @Mock private AccountRepository accountRepository;

  @Before public void setUp() {
    query = new DefaultGetAccountQuery(accountRepository);
  }

  @Test public void shouldRunQuery() {
    // given
    String number = "1234";

    // when
    query.run(number);

    // then
    verify(accountRepository).get(number);
  }
}