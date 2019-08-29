package com.pwittchen.money.transfer.api.command.exception;

public class EmptyUserNameException extends RuntimeException {
  @Override public String getMessage() {
    return "User name is empty";
  }
}
