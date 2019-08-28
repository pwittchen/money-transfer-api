package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.DeleteAccountCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.command.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultDeleteAccountCommandTest {
  private DeleteAccountCommand deleteAccountCommand;

  @Mock private AccountRepository accountRepository;

  @Before public void setUp() {
    deleteAccountCommand = new DefaultDeleteAccountCommand(accountRepository);
  }

  @Test public void shouldDeleteAccount() {
    // given
    String accountNumber = UUID.randomUUID().toString();
    User user = User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("name")
        .surname("surname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(accountNumber)
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    when(accountRepository.getAll()).thenReturn(Collections.singletonList(account));

    // when
    deleteAccountCommand.run(accountNumber);

    // then
    verify(accountRepository).delete(accountNumber);
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNotDeleteAccountWhenAccountNumberIsNull() {
    // when
    deleteAccountCommand.run(null);

    // then
    verify(accountRepository, times(0)).delete(anyString());
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNotDeleteAccountWhenAccountNumberIsEmpty() {
    // when
    deleteAccountCommand.run("");

    // then
    verify(accountRepository, times(0)).delete(anyString());
  }

  @Test(expected = AccountNotExistsException.class)
  public void shouldNotDeleteAccountWhenAccountDoesNotExist() {
    // given
    String accountNumber = UUID.randomUUID().toString();
    when(accountRepository.getAll()).thenReturn(new ArrayList<>());

    // when
    deleteAccountCommand.run(accountNumber);

    // then
    verify(accountRepository, times(0)).delete(anyString());
  }
}