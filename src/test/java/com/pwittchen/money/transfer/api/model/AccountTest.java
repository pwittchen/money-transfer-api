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

  @Test public void objectsShouldBeEqualWhenValuesAreTheSame() {
    // when
    LocalDateTime now = LocalDateTime.now();
    Account accountOne = Account.builder()
        .number("1")
        .createdAt(now)
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Account accountTwo = Account.builder()
        .number("1")
        .createdAt(now)
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // then
    assertThat(accountOne.equals(accountTwo)).isTrue();
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

  @Test public void shouldNotBeTheSameWhenNumberIsDifferent() {
    // given
    Account account = createAccount();

    Account anotherAccount = Account.builder()
        .number("2")
        .createdAt(LocalDateTime.now())
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenCreationDatesAreDifferent() {
    // given
    Account account = createAccount();

    Account anotherAccount = Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now().plusSeconds(30))
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenOwnerIsDifferent() {
    // given
    Account account = createAccount();

    Account anotherAccount = Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now())
        .owner("anotherTestOwner")
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
    Account anotherAccount = Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now())
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldNotBeTheSameWhenMoneyCurrencyDiffers() {
    // given
    Account account = createAccount();
    Account anotherAccount = Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now())
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.USD, 10))
        .build();

    // when
    boolean isTheSame = account.equals(anotherAccount);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldBeCreatedJustNow() {
    // given
    Account account = createAccount();

    // when
    boolean isCreatedJustNow = account.createdAt().isBefore(LocalDateTime.now());

    // then
    assertThat(isCreatedJustNow).isTrue();
  }

  @Test public void objectsShouldBeInTheSameBucket() {
    // when
    Account accountOne = createAccount();
    Account accountTwo = accountOne;

    // then
    assertThat(accountOne.hashCode() == accountTwo.hashCode()).isTrue();
  }

  private Account createAccount() {
    return Account.builder()
        .number("1")
        .createdAt(LocalDateTime.now())
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();
  }
}