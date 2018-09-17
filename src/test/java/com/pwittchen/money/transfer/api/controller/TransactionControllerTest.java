package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class TransactionControllerTest {

  private TransactionController controller;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private ContextWrapper contextWrapper;

  @Before
  public void setUp() {
    this.controller = new TransactionController(
        contextWrapper, transactionRepository, accountRepository
    );
  }

  @Test
  public void shouldGetOneTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldGetAllTransactions() {
    //TODO: implement
  }

  @Test
  public void shouldCommitTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldNotCommitTransactionIfSenderIsNotPresent() {
    //TODO: implement
  }

  @Test
  public void shouldNotCommitTransactionIfReceiverIsNotPresent() {
    //TODO: implement
  }

  @Test
  public void shouldNotCommitTransactionIfMoneyHasInvalidFormat() {
    //TODO: implement
  }

  @Test
  public void shouldNotCommitTransactionIfValidationErrorOccurred() {
    //TODO: implement
  }
}