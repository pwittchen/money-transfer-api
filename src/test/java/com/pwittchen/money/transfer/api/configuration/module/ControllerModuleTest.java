package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.controller.context.DefaultContextWrapper;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ControllerModuleTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private ContextWrapper contextWrapper;

  private ControllerModule controllerModule = new ControllerModule();

  @Test
  public void shouldProvideAccountController() {
    // when
    AccountController controller = controllerModule.provideAccountController(
        contextWrapper, accountRepository
    );

    // then
    assertThat(controller).isNotNull();
  }

  @Test
  public void shouldProvideTransactionController() {
    // when
    TransactionController transactionController = controllerModule.provideTransactionController(
        contextWrapper, transactionRepository, accountRepository
    );

    // then
    assertThat(transactionController).isNotNull();
  }

  public void shouldProvideContextWrapper() {
    // when
    ContextWrapper contextWrapper = controllerModule.provideContextWrapper();

    // then
    assertThat(contextWrapper).isNotNull();
    assertThat(contextWrapper).isInstanceOf(DefaultContextWrapper.class);
  }
}