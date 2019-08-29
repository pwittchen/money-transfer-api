package com.pwittchen.money.transfer.api.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class AccountTest {

  @Test public void constructorShouldBePrivate()
      throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {

    // when
    Constructor<Account> constructor = Account.class.getDeclaredConstructor();

    // then
    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

    constructor.setAccessible(true);
    constructor.newInstance();
  }

  @Test public void objectsShouldBeEqualWhenTheyAreTheSameInstance() {
    // when
    Account account = createAccount();

    // then
    assertThat(account.equals(account)).isTrue();
  }

  @Test public void objectsShouldNotBeEqualWhenOneIsNull() {
    // when
    Account account = createAccount();

    // then
    assertThat(account.equals(null)).isFalse();
  }

  @Test public void shouldNotBeTheSameAsOtherObject() {
    // given
    Account account = createAccount();
    Object anotherObject = new Object();

    // when
    boolean isTheSame = account.equals(anotherObject);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenComparingToNull() {
    // given
    Account account = createAccount();

    // when
    boolean isTheSame = account.equals(null);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenUserIsDifferent() {
    // given
    Account account = createAccount();
    User user = User.builder()
        .id("2")
        .name("Test")
        .surname("User")
        .build();

    Account anotherAccount = Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now())
        .user(user)
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenNumberIsDifferent() {
    // given
    Account account = createAccount();
    User user = User.builder()
        .id("1")
        .name("John")
        .surname("Doe")
        .build();

    Account anotherAccount = Account.builder()
        .number("2")
        .createdAt(LocalDateTime.now())
        .user(user)
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenMoneyDiffers() {
    // given
    Account account = createAccount();
    User user = User.builder()
        .id("1")
        .name("John")
        .surname("Doe")
        .build();

    Account anotherAccount = Account.builder()
        .number("2")
        .createdAt(LocalDateTime.now())
        .user(user)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  private Account createAccount() {
    User user = User.builder()
        .id("1")
        .name("John")
        .surname("Doe")
        .build();

    return Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now())
        .user(user)
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();
  }
}