package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DefaultTransactionValidationTest {

  private TransactionValidation transactionValidation;

  @Mock
  private AccountRepository accountRepository;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    this.transactionValidation = new DefaultTransactionValidation(accountRepository);
  }

  @Test
  public void shouldNotGetCommitErrorForValidTransaction() {
    //TODO: implement
  }

  @Test
  public void shouldGetErrorWhenSenderAccountDoesNotExist() {
    //TODO: implement
  }

  @Test
  public void shouldGetErrorWhenReceiverAccountDoesNotExist() {
    //TODO: implement
  }

  @Test
  public void shouldGetErrorWhenSenderBalanceIsLessThanMoneyToBeSend() {
    //TODO: implement
  }

  @Test
  public void shouldGetErrorWhenTwoAccountsHaveFundsInDifferentCurrencies() {
    //TODO: implement
  }
}