package com.pwittchen.money.transfer.api.repository.exception;

public class AccountNotExistsException extends RuntimeException {
  @Override public String getMessage() {
    return "account with given number does not exist";
  }
}
