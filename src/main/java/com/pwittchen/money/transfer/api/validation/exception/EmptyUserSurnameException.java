package com.pwittchen.money.transfer.api.validation.exception;

public class EmptyUserSurnameException extends RuntimeException {
  @Override public String getMessage() {
    return "User surname is empty";
  }
}
