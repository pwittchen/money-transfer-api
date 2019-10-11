package com.pwittchen.money.transfer.api.command.exception;

public class DifferentCurrencyException extends RuntimeException {

  private final String accountNumberFrom;
  private final String accountNumberTo;

  public DifferentCurrencyException(String accountNumberFrom, String accountNumberTo) {
    this.accountNumberFrom = accountNumberFrom;
    this.accountNumberTo = accountNumberTo;
  }

  @Override public String getMessage() {
    return String.format(
        "Accounts %s and %s have funds in different currencies",
        accountNumberFrom,
        accountNumberTo
    );
  }
}
