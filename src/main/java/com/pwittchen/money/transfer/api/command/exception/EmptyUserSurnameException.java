package com.pwittchen.money.transfer.api.command.exception;

public class EmptyUserSurnameException extends RuntimeException {
  @Override public String getMessage() {
    return "User surname is empty";
  }
}
