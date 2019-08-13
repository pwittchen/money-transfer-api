package com.pwittchen.money.transfer.api.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.joda.money.Money;

public class Account {
  private transient final Lock lock;
  private final String number;
  private final User user;
  private Money money;
  private final LocalDateTime createdAt;

  private Account() {
    this(builder());
  }

  private Account(final Builder builder) {
    this(builder.number, builder.user, builder.money, builder.createdAt);
  }

  public Account(final String number, final User user, final Money money,
      final LocalDateTime createdAt) {
    this.lock = new ReentrantLock();
    this.number = number;
    this.user = user;
    this.money = money;
    this.createdAt = createdAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public Lock lock() {
    return lock;
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

  public LocalDateTime createdAt() {
    return createdAt;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(number, account.number)
        && Objects.equals(user, account.user)
        && Objects.equals(money, account.money)
        && Objects.equals(createdAt, account.createdAt);
  }

  @Override public int hashCode() {
    return Objects.hash(number, user, money, createdAt);
  }

  public static class Builder {
    private String number;
    private User user;
    private Money money;
    private LocalDateTime createdAt;

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

    public Builder createdAt(final LocalDateTime dateTime) {
      this.createdAt = dateTime;
      return this;
    }

    public Account build() {
      return new Account(this);
    }
  }
}
