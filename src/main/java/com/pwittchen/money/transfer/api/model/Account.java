package com.pwittchen.money.transfer.api.model;

import org.joda.money.Money;

public final class Account {
  private final String number;
  private final User user;
  private Money money;

  public Account(final String number, final User user, final Money money) {
    this.number = number;
    this.user = user;
    this.money = money;
  }

  public String number() {
    return number;
  }

  public User user() {
    return user;
  }

  public Money money() {
    return money;
  }

  public void put(final Money moneyToPut) {
    this.money = money.plus(moneyToPut);
  }

  public void withdraw(final Money moneyToWithdraw) {
    this.money = money.minus(moneyToWithdraw);
  }
}
