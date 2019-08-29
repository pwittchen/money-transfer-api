package com.pwittchen.money.transfer.api.query;

import com.pwittchen.money.transfer.api.model.Account;
import java.util.Optional;

public interface GetAccountQuery {
  Optional<Account> run(String number);
}
