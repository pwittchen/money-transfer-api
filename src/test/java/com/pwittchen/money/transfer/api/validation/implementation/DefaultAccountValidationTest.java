package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserIdException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserNameException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserSurnameException;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DefaultAccountValidationTest {

  private AccountValidation accountValidation = new DefaultAccountValidation();

  @Test
  public void shouldNotCreateAnyErrorsIfAccountIsValid() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name("testName")
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isFalse();
  }

  @Test
  public void shouldCreateErrorIfAccountNumberIsNull() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name("testName")
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(null)
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyAccountNumberException.class));
  }

  @Test
  public void shouldCreateErrorIfAccountNumberIsEmpty() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name("testName")
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number("")
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyAccountNumberException.class));
  }

  @Test
  public void shouldCreateErrorIfUserIdIsNull() {
    // given
    User user = User.builder()
        .id(null)
        .name("testName")
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyUserIdException.class));
  }

  @Test
  public void shouldCreateErrorIfUserIdIsEmpty() {
    // given
    User user = User.builder()
        .id("")
        .name("testName")
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyUserIdException.class));
  }

  @Test
  public void shouldCreateErrorIfUserNameIsNull() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name(null)
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyUserNameException.class));
  }

  @Test
  public void shouldCreateErrorIfUserNameIsEmpty() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name("")
        .surname("testSurname")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyUserNameException.class));
  }

  @Test
  public void shouldCreateErrorIfUserSurnameIsNull() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name("testName")
        .surname(null)
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyUserSurnameException.class));
  }

  @Test
  public void shouldCreateErrorIfUserSurnameIsEmpty() {
    // given
    User user = User.builder()
        .id(UUID.randomUUID().toString())
        .name("testName")
        .surname("")
        .build();

    Account account = Account
        .builder()
        .user(user)
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 10.00))
        .build();

    // when
    Optional<Exception> error = accountValidation.validate(account);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(e -> assertThat(e).isInstanceOf(EmptyUserSurnameException.class));
  }
}