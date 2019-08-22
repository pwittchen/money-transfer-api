package com.pwittchen.money.transfer.api.exception;

public class EmptyAccountNumberException extends RuntimeException {
  @Override public String getMessage() {
    return "Empty account number";
  }
}
