package com.pwittchen.money.transfer.api.query.implementation;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGetAllTransactionsQueryTest {

  @Mock private TransactionRepository transactionRepository;

  private DefaultGetAllTransactionsQuery query;

  @Before public void setUp() {
    query = new DefaultGetAllTransactionsQuery(transactionRepository);
  }

  @Test public void shouldGetAllTransactions() {
    // given
    BlockingQueue<Transaction> queue = new LinkedBlockingQueue<>();
    queue.add(Transaction.builder().build());
    when(transactionRepository.getAll()).thenReturn(queue);

    // when
    BlockingQueue<Transaction> transactions = query.run();

    // then
    assertThat(transactions).isNotEmpty();
  }
}