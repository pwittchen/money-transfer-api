package com.pwittchen.money.transfer.api.command.exception;

public class EmptyUserIdException extends RuntimeException {
  @Override public String getMessage() {
    return "User id is empty";
  }
}
