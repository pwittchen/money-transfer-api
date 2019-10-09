package com.pwittchen.money.transfer.api.command.exception;

public class EmptyAccountNumberException extends RuntimeException {
  @Override public String getMessage() {
    return "Account number is empty";
  }
}
