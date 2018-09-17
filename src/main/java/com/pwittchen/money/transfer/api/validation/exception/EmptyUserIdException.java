package com.pwittchen.money.transfer.api.validation.exception;

public class EmptyUserIdException extends RuntimeException {
  @Override public String getMessage() {
    return "User id is empty";
  }
}
