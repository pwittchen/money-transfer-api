package com.pwittchen.money.transfer.api.configuration.component;

import com.pwittchen.money.transfer.api.configuration.module.ControllerModule;
import com.pwittchen.money.transfer.api.configuration.module.RepositoryModule;
import com.pwittchen.money.transfer.api.configuration.module.ValidationModule;
import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ControllerModule.class,
    RepositoryModule.class,
    ValidationModule.class,
})
public interface ApplicationComponent {
  AccountRepository accountRepository();

  TransactionRepository transactionRepository();

  AccountController accountController();

  TransactionController transactionController();
}
