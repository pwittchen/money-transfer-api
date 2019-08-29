package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetAllTransactionsQueryTest {

  private GetAllTransactionsQuery query;

  @Mock private TransactionRepository transactionRepository;

  @Before public void setUp() {
    query = new DefaultGetAllTransactionsQuery(transactionRepository);
  }

  @Test public void shouldRunQuery() {
    // when
    query.run();

    // then
    verify(transactionRepository).getAll();
  }
}