package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.exception.AccountNotExistsException;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

  @Mock
  private Account account;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private ContextWrapper contextWrapper;

  @Mock
  private Context context;

  private AccountController controller;

  @Before
  public void setUp() {
    controller = new AccountController(contextWrapper, accountRepository);
  }

  @Test
  public void shouldGetOneAccount() {
    // given
    when(contextWrapper.pathParam(context, "id")).thenReturn("1");
    when(accountRepository.get("1")).thenReturn(Optional.of(account));

    // when
    controller.getOne(context);

    // then
    verify(contextWrapper).json(context, account, HttpStatus.OK_200);
  }

  @Test
  public void shouldNotGetOneAccount() {
    // given
    String id = "1";
    when(contextWrapper.pathParam(context, "id")).thenReturn(id);
    when(accountRepository.get(id)).thenReturn(Optional.empty());

    // when
    controller.getOne(context);

    // then
    verify(contextWrapper).json(context,
        "account with id 1 does not exist",
        HttpStatus.NOT_FOUND_404);
  }

  @Test
  public void shouldGetAllAccounts() {
    // given
    List<Account> accounts = new ArrayList<>();
    when(accountRepository.getAll()).thenReturn(accounts);

    // when
    controller.getAll(context);

    // then
    verify(contextWrapper).json(context, accounts);
  }

  @Test
  public void shouldCreateAccount() throws Exception {
    // given
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("100.00");

    // when
    controller.create(context);

    // then
    verify(accountRepository).create(any(Account.class));
  }

  @Test
  public void shouldNotCreateAccountIfCurrencyFormatIsInvalid() throws Exception {
    // given
    when(contextWrapper.formParam(context, "currency")).thenReturn("INVALID");
    when(contextWrapper.formParam(context, "money")).thenReturn("100.00");

    // when
    controller.create(context);

    // then
    verify(accountRepository, times(0)).create(any(Account.class));
    verify(contextWrapper).json(context, "Invalid money format", HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldNotCreateAccountIfMoneyFormatIsInvalid() throws Exception {
    // given
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("INVALID");

    // when
    controller.create(context);

    // then
    verify(accountRepository, times(0)).create(any(Account.class));
    verify(contextWrapper).json(context, "Invalid money format", HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldNotCreateAccountIfErrorOccurred() throws Exception {
    // given
    AccountAlreadyExistsException exception = new AccountAlreadyExistsException("1");
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("100.00");
    when(accountRepository.create(any())).thenThrow(exception);

    // when
    controller.create(context);

    // then
    verify(contextWrapper).json(context, exception.getMessage(), HttpStatus.BAD_REQUEST_400);
  }

  @Test
  public void shouldDeleteAccount() {
    // given
    String id = "1";
    when(contextWrapper.pathParam(context, "id")).thenReturn(id);
    String message = String.format(
        "account with number %s deleted",
        contextWrapper.pathParam(context, "id")
    );

    // when
    controller.delete(context);

    // then
    verify(accountRepository).delete(id);
    verify(contextWrapper).json(context, message, HttpStatus.OK_200);
  }

  @Test
  public void shouldNotDeleteAccountIfErrorOccurred() {
    // given
    String id = "1";
    AccountNotExistsException exception = new AccountNotExistsException(id);
    when(contextWrapper.pathParam(context, "id")).thenReturn(id);
    doThrow(exception).when(accountRepository).delete(id);

    // when
    controller.delete(context);

    // then
    verify(contextWrapper).json(context, exception.getMessage(), HttpStatus.BAD_REQUEST_400);
  }
}