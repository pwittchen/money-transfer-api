package com.pwittchen.money.transfer.api.configuration.component;

import com.pwittchen.money.transfer.api.configuration.modules.RepositoryModule;
import com.pwittchen.money.transfer.api.configuration.modules.ValidationModule;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Component;

@Component(modules = {RepositoryModule.class, ValidationModule.class})
public interface ApplicationComponent {
  AccountRepository injectAccountRepository();

  TransactionRepository injectTransactionRepository();
}
