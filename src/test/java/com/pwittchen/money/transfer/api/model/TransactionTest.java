package com.pwittchen.money.transfer.api.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class TransactionTest {

  @Test public void constructorShouldBePrivate()
      throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {

    // when
    Constructor<Transaction> constructor = Transaction.class.getDeclaredConstructor();

    // then
    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

    constructor.setAccessible(true);
    constructor.newInstance();
  }

  @Test public void objectsShouldBeEqual() {
    // when
    LocalDateTime now = LocalDateTime.now();
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(now)
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR1")
        .createdAt(now)
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // then
    assertThat(transactionOne.equals(transactionTwo)).isTrue();
  }

  @Test public void objectsShouldBeEqualWhenTheyAreTheSameInstance() {
    // when
    Transaction transaction = createTransaction();

    // then
    assertThat(transaction.equals(transaction)).isTrue();
  }

  @Test public void objectsShouldNotBeEqualWhenOneIsNull() {
    // when
    Transaction transaction = createTransaction();

    // then
    assertThat(transaction.equals(null)).isFalse();
  }

  @Test public void objectsShouldNotBeEqualWhenOneHasDifferentType() {
    // when
    Transaction transaction = createTransaction();

    // then
    assertThat(transaction.equals(new Object())).isFalse();
  }

  @Test public void objectsShouldBeInTheSameBucket() {
    // when
    Transaction transactionOne = createTransaction();
    Transaction transactionTwo = transactionOne;

    // then
    assertThat(transactionOne.hashCode() == transactionTwo.hashCode()).isTrue();
  }

  @Test public void objectShouldNotBeTheSameWhenOneHasDifferentId() {
    // given
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR2")
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = transactionOne.equals(transactionTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectShouldNotBeTheSameWhenOneHasDifferentSender() {
    // given
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR1")
        .from("anotherNumber")
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = transactionOne.equals(transactionTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectShouldNotBeTheSameWhenOneHasDifferentReceiver() {
    // given
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to("anotherNumber")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = transactionOne.equals(transactionTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectShouldNotBeTheSameWhenOneMoneyValueDiffers() {
    // given
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    boolean isTheSame = transactionOne.equals(transactionTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectShouldNotBeTheSameWhenOneMoneyCurrencyDiffers() {
    // given
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.USD, 10))
        .build();

    // when
    boolean isTheSame = transactionOne.equals(transactionTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectShouldNotBeTheSameWhenCreationDateDiffers() {
    // given
    Transaction transactionOne = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now().plusSeconds(30))
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    boolean isTheSame = transactionOne.equals(transactionTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void shouldBeCreatedJustNow() {
    // given
    final Transaction transaction = createTransaction();

    // when
    boolean isCreatedJustNow = transaction.createdAt().isBefore(LocalDateTime.now());

    // then
    assertThat(isCreatedJustNow).isTrue();
  }

  private Transaction createTransaction() {
    return Transaction.builder()
        .id("TR1")
        .createdAt(LocalDateTime.now())
        .from(createAccount().number())
        .to(createAccount().number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();
  }

  private Account createAccount() {
    return Account.builder()
        .number("1")
        .owner("testOwner")
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();
  }
}