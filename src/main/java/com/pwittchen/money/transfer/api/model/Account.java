package com.pwittchen.money.transfer.api.model;

import org.joda.money.Money;

public final class Account {
  private final String number;
  private final User user;
  private Money money;

  private Account() {
    this(builder());
  }

  private Account(final Builder builder) {
    this(builder.number, builder.user, builder.money);
  }

  private Account(final String number, final User user, final Money money) {
    this.number = number;
    this.user = user;
    this.money = money;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String number() {
    return number;
  }

  public static Builder number(final String number) {
    return builder().number(number);
  }

  public User user() {
    return user;
  }

  public static Builder user(final User user) {
    return builder().user(user);
  }

  public Money money() {
    return money;
  }

  public static Builder money(final Money money) {
    return builder().money(money);
  }

  public void put(final Money moneyToPut) {
    this.money = money.plus(moneyToPut);
  }

  public void withdraw(final Money moneyToWithdraw) {
    this.money = money.minus(moneyToWithdraw);
  }

  public static class Builder {
    private String number;
    private User user;
    private Money money;

    private Builder() {
    }

    public Builder number(final String number) {
      this.number = number;
      return this;
    }

    public Builder user(final User user) {
      this.user = user;
      return this;
    }

    public Builder money(final Money money) {
      this.money = money;
      return this;
    }

    public Account build() {
      return new Account(this);
    }
  }
}
