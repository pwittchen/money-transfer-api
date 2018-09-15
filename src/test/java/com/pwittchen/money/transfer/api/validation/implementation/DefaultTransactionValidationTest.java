package com.pwittchen.money.transfer.api.validation.implementation;

import org.junit.Test;

public class DefaultTransactionValidationTest {

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