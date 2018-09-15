package com.pwittchen.money.transfer.api.repository.exception;

public class AccountAlreadyExistsException extends RuntimeException {
  @Override public String getMessage() {
    return "Account with given number already exists";
  }
}
