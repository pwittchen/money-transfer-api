package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.controller.AccountController;
import com.pwittchen.money.transfer.api.controller.TransactionController;
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

  private ControllerModule controllerModule = new ControllerModule();

  @Test
  public void shouldProvideAccountController() {
    // when
    AccountController controller = controllerModule.provideAccountController(accountRepository);

    // then
    assertThat(controller).isNotNull();
  }

  @Test
  public void shouldProvideTransactionController() {
    // when
    TransactionController transactionController = controllerModule.provideTransactionController(
        transactionRepository, accountRepository
    );

    // then
    assertThat(transactionController).isNotNull();
  }
}