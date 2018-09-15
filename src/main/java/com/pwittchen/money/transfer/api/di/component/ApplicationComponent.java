package com.pwittchen.money.transfer.api.di.component;

import com.pwittchen.money.transfer.api.di.modules.RepositoryModule;
import com.pwittchen.money.transfer.api.di.modules.ValidationModule;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Component;

@Component(modules = {RepositoryModule.class, ValidationModule.class})
public interface ApplicationComponent {
  AccountRepository injectAccountRepository();

  TransactionRepository injectTransactionRepository();
}
