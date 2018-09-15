package com.pwittchen.money.transfer.api.repository.exception;

public class AccountNotExistsException extends RuntimeException {

  private final String number;

  public AccountNotExistsException(final String number) {
    this.number = number;
  }

  @Override public String getMessage() {
    return String.format("account with number %s does not exist", number);
  }
}
