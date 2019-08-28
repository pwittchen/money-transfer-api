package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CreateAccountCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.command.exception.EmptyUserIdException;
import com.pwittchen.money.transfer.api.command.exception.EmptyUserNameException;
import com.pwittchen.money.transfer.api.command.exception.EmptyUserSurnameException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
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
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository).create(account);
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNoCreateAccountWhenNumberIsNull() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(null)
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNoCreateAccountWhenNumberIsEmpty() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number("")
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = AccountAlreadyExistsException.class)
  public void shouldNoCreateAccountWhenAccountAlreadyExists() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    when(accountRepository.getAll()).thenReturn(Collections.singletonList(account));

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyUserIdException.class)
  public void shouldNoCreateAccountWhenUserIdIsNull() {
    // given
    User user = User
        .builder()
        .id(null)
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyUserIdException.class)
  public void shouldNoCreateAccountWhenUserIdIsEmpty() {
    // given
    User user = User
        .builder()
        .id("")
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyUserNameException.class)
  public void shouldNoCreateAccountWhenUserNameIsNull() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name(null)
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyUserNameException.class)
  public void shouldNoCreateAccountWhenUserNameIsEmpty() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyUserSurnameException.class)
  public void shouldNoCreateAccountWhenUserSurnameIsNull() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname(null)
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }

  @Test(expected = EmptyUserSurnameException.class)
  public void shouldNoCreateAccountWhenUserSurnameIsEmpty() {
    // given
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname("")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    createAccountCommand.run(account);

    // then
    verify(accountRepository, times(0)).create(account);
  }
}