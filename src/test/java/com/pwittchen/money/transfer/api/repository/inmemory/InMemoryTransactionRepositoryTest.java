package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class InMemoryTransactionRepositoryTest {

  private TransactionRepository transactionRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionValidation transactionValidation;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.transactionRepository = new InMemoryTransactionRepository(
        accountRepository, transactionValidation
    );
  }

  @Test
  public void shouldNotCommitTransactionWhenValidationDetectedError() {
    //TODO: implement
  }

  @Test
  public void shouldCommitTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldGetCreatedTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldGetAllTransactions() {
    //TODO: implement
  }
}