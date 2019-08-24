package com.pwittchen.money.transfer.api.query;

import com.pwittchen.money.transfer.api.model.Account;
import java.util.List;

public interface GetAllAccountsQuery {
  List<Account> run();
}
