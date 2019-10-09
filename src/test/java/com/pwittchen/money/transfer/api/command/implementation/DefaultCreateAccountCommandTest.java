package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountOwnerException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import java.util.Collections;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCreateAccountCommandTest {

  private CreateAccountCommand createAccountCommand;

  @Mock private AccountRepository accountRepository;

  @Before public void setUp() {
    createAccountCommand = new DefaultCreateAccountCommand(accountRepository);
  }

  @Test public void shouldCreateAccount() {
    // given
    Account account = Account
        .builder()
        .owner("testOwner")
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository).create(account);
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNotCreateAccountWhenNumberIsNull() {
    // given
    Account account = Account
        .builder()
        .owner("testOwner")
        .number(null)
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNotCreateAccountWhenNumberIsEmpty() {
    // given
    Account account = Account
        .builder()
        .owner("testOwner")
        .number("")
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test
  public void shouldProduceAppropriateErrorMessageForEmptyAccountNumber() {
    // given
    String expectedMessage = "Account number is empty";

    // when
    EmptyAccountNumberException exception = new EmptyAccountNumberException();

    // then
    assertThat(exception.getMessage()).isEqualTo(expectedMessage);
  }

  @Test(expected = AccountAlreadyExistsException.class)
  public void shouldNotCreateAccountWhenAccountAlreadyExists() {
    // given
    Account account = Account
        .builder()
        .owner("testOwner")
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    when(accountRepository.getAll()).thenReturn(Collections.singletonList(account));

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyAccountOwnerException.class)
  public void shouldNotCreateAccountWhenOwnerIsNull() {
    // given
    Account account = Account
        .builder()
        .owner(null)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyAccountOwnerException.class)
  public void shouldNotCreateAccountWhenOwnerIsEmpty() {
    // given
    Account account = Account
        .builder()
        .owner("")
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }
}