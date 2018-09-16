package com.pwittchen.money.transfer.api.model;

import java.util.Objects;
import org.joda.money.Money;

public class Account {
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

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Account account = (Account) o;

    return Objects.equals(number, account.number)
        && Objects.equals(user, account.user)
        && Objects.equals(money, account.money);
  }

  @Override public int hashCode() {
    return Objects.hash(number, user, money);
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
