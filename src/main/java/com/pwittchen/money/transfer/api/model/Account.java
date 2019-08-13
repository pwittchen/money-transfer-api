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
  private final LocalDateTime updatedAt;

  private Account() {
    this(builder());
  }

  private Account(final Builder builder) {
    this(builder.number, builder.user, builder.money, builder.createdAt, builder.updatedAt);
  }

  public Account(final String number, final User user, final Money money,
      final LocalDateTime createdAt, final LocalDateTime updatedAt) {
    this.lock = new ReentrantLock();
    this.number = number;
    this.user = user;
    this.money = money;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
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

  public LocalDateTime updatedAt() {
    return updatedAt;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Account account = (Account) o;
    return Objects.equals(number, account.number) &&
        Objects.equals(user, account.user) &&
        Objects.equals(money, account.money) &&
        Objects.equals(createdAt, account.createdAt) &&
        Objects.equals(updatedAt, account.updatedAt);
  }

  @Override public int hashCode() {
    return Objects.hash(number, user, money, createdAt, updatedAt);
  }

  public static class Builder {
    private String number;
    private User user;
    private Money money;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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

    public Builder updatedAt(final LocalDateTime dateTime) {
      this.createdAt = dateTime;
      return this;
    }

    public Account build() {
      return new Account(this);
    }
  }
}
