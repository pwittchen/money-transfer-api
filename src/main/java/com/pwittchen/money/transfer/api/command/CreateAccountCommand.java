package com.pwittchen.money.transfer.api.command;

import com.pwittchen.money.transfer.api.model.Account;

public interface CreateAccountCommand {
  void run(Account account);
}
