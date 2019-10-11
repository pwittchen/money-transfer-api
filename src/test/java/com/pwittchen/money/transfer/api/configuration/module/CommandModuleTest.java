package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.implementation.DefaultCommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.implementation.DefaultCreateAccountCommand;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CommandModuleTest {

  private CommandModule commandModule = new CommandModule();

  @Mock private AccountRepository accountRepository;

  @Mock private TransactionRepository transactionRepository;

  @Test public void shouldProvideCommitTransactionCommand() {
    // when
    CommitTransactionCommand command = commandModule.provideCommitTransactionCommand(
        accountRepository, transactionRepository
    );

    // then
    assertThat(command).isNotNull();
    assertThat(command).isInstanceOf(DefaultCommitTransactionCommand.class);
  }

  @Test public void shouldProvideCreateAccountCommand() {
    CreateAccountCommand command = commandModule.provideCreateAccountCommand(
        accountRepository
    );

    assertThat(command).isNotNull();
    assertThat(command).isInstanceOf(DefaultCreateAccountCommand.class);
  }
}