package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.DeleteAccountCommand;
import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.controller.context.DefaultContextWrapper;
import com.pwittchen.money.transfer.api.query.GetAccountQuery;
import com.pwittchen.money.transfer.api.query.GetAllAccountsQuery;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ControllerModuleTest {

  @Mock private GetAccountQuery getAccountQuery;

  @Mock private GetAllAccountsQuery getAllAccountsQuery;

  @Mock private GetTransactionQuery getTransactionQuery;

  @Mock private GetAllTransactionsQuery getAllTransactionsQuery;

  @Mock private CommitTransactionCommand commitTransactionCommand;

  @Mock private CreateAccountCommand createAccountCommand;

  @Mock private DeleteAccountCommand deleteAccountCommand;

  @Mock private ContextWrapper contextWrapper;

  private ControllerModule controllerModule = new ControllerModule();

  @Test public void shouldProvideAccountController() {
    // when
    AccountController controller = controllerModule.provideAccountController(
        contextWrapper,
        getAccountQuery,
        getAllAccountsQuery,
        createAccountCommand,
        deleteAccountCommand
    );

    // then
    assertThat(controller).isNotNull();
  }

  @Test public void shouldProvideTransactionController() {
    // when
    TransactionController transactionController = controllerModule.provideTransactionController(
        contextWrapper,
        getTransactionQuery,
        getAllTransactionsQuery,
        commitTransactionCommand
    );

    // then
    assertThat(transactionController).isNotNull();
  }

  @Test public void shouldProvideContextWrapper() {
    // when
    ContextWrapper contextWrapper = controllerModule.provideContextWrapper();

    // then
    assertThat(contextWrapper).isNotNull();
    assertThat(contextWrapper).isInstanceOf(DefaultContextWrapper.class);
  }
}