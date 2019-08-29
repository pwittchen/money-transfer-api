package com.pwittchen.money.transfer.api.command.exception;

public class TransferToTheSameAccountException extends RuntimeException {
  @Override public String getMessage() {
    return "Sender and receiver account numbers have to be different";
  }
}
