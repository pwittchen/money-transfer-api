package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Before;
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
}