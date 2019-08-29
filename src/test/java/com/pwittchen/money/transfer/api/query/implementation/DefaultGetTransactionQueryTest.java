package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetTransactionQueryTest {

  private GetTransactionQuery query;

  @Mock private TransactionRepository transactionRepository;

  @Before public void setUp() {
    query = new DefaultGetTransactionQuery(transactionRepository);
  }

  @Test public void shouldRunQuery() {
    // given
    String id = "1234";

    // when
    query.run(id);

    // then
    verify(transactionRepository).get(id);
  }
}