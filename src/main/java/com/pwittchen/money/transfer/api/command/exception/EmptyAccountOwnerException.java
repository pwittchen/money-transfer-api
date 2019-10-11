package com.pwittchen.money.transfer.api.command.exception;

public class EmptyAccountOwnerException extends RuntimeException {

  @Override public String getMessage() {
    return "Account owner is empty";
  }
}
