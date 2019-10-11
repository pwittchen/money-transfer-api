package com.pwittchen.money.transfer.api.command.exception;

public class NotEnoughMoneyException extends RuntimeException {

  private final String accountNumber;

  public NotEnoughMoneyException(final String accountNumber) {
    this.accountNumber = accountNumber;
  }

  @Override public String getMessage() {
    return String.format("Not enough money on the account with number %s", accountNumber);
  }
}
