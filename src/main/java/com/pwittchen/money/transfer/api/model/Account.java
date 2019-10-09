package com.pwittchen.money.transfer.api.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.joda.money.Money;

@SuppressWarnings("WeakerAccess") // public access of class attributes is required by OpenAPI
public class Account {
  private transient final Lock lock;
  public final String number;
  public final String owner;
  public Money money;
  public final LocalDateTime createdAt;

  private Account() {
    this(builder());
  }

  private Account(final Builder builder) {
    this(builder.number, builder.owner, builder.money, builder.createdAt);
  }

  public Account(final String number, final String owner, final Money money,
      final LocalDateTime createdAt) {
    this.lock = new ReentrantLock();
    this.number = number;
    this.owner = owner;
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

  public String owner() {
    return owner;
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
        && Objects.equals(owner, account.owner)
        && Objects.equals(money, account.money)
        && Objects.equals(createdAt, account.createdAt);
  }

  @Override public int hashCode() {
    return Objects.hash(number, owner, money, createdAt);
  }

  public static class Builder {
    private String number;
    private String owner;
    private Money money;
    private LocalDateTime createdAt;

    private Builder() {
    }

    public Builder number(final String number) {
      this.number = number;
      return this;
    }

    public Builder owner(final String owner) {
      this.owner = owner;
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
