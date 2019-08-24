package com.pwittchen.money.transfer.api.command.exception;

public class AccountNotExistsException extends RuntimeException {

  private final String number;

  public AccountNotExistsException(final String number) {
    this.number = number;
  }

  @Override public String getMessage() {
    return String.format("Account with number %s does not exist", number);
  }
}
