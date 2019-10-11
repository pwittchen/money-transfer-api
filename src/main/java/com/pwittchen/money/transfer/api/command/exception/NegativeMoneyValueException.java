package com.pwittchen.money.transfer.api.command.exception;

public class NegativeMoneyValueException extends RuntimeException {
  @Override public String getMessage() {
    return "Money could not be negative";
  }
}
