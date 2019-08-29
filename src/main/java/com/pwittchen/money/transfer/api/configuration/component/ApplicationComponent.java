package com.pwittchen.money.transfer.api.configuration.component;

import com.pwittchen.money.transfer.api.configuration.module.CommandModule;
import com.pwittchen.money.transfer.api.configuration.module.ControllerModule;
import com.pwittchen.money.transfer.api.configuration.module.QueryModule;
import com.pwittchen.money.transfer.api.configuration.module.RepositoryModule;
import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ControllerModule.class,
    RepositoryModule.class,
    CommandModule.class,
    QueryModule.class
})
public interface ApplicationComponent {
  AccountController accountController();

  TransactionController transactionController();
}
