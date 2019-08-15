package com.pwittchen.money.transfer.api.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.joda.money.Money;

public class Transaction {
  private final transient AtomicBoolean isRunning;
  public final String id;
  public final Account from;
  public final Account to;
  public final Money money;
  public final LocalDateTime createdAt;

  private Transaction() {
    this(builder());
  }

  private Transaction(final Builder builder) {
    this(builder.id, builder.from, builder.to, builder.money, builder.createdAt);
  }

  private Transaction(final String id, final Account from, final Account to, final Money money,
      final LocalDateTime createdAt) {
    this.isRunning = new AtomicBoolean(true);
    this.id = id;
    this.from = from;
    this.to = to;
    this.money = money;
    this.createdAt = createdAt;
  }

  public static Builder builder() {
    return new Builder();
  }

  public AtomicBoolean isRunning() {
    return isRunning;
  }

  public String id() {
    return id;
  }

  public Account from() {
    return from;
  }

  public Account to() {
    return to;
  }

  public Money money() {
    return money;
  }

  public LocalDateTime createdAt() {
    return createdAt;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Transaction that = (Transaction) o;

    return Objects.equals(id, that.id)
        && Objects.equals(from, that.from)
        && Objects.equals(to, that.to)
        && Objects.equals(money, that.money)
        && Objects.equals(createdAt, that.createdAt);
  }

  @Override public int hashCode() {
    return Objects.hash(id, from, to, money, createdAt);
  }

  public static class Builder {
    private String id;
    private Account from;
    private Account to;
    private Money money;
    private LocalDateTime createdAt;

    private Builder() {
    }

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public Builder from(final Account from) {
      this.from = from;
      return this;
    }

    public Builder to(final Account to) {
      this.to = to;
      return this;
    }

    public Builder money(final Money money) {
      this.money = money;
      return this;
    }

    public Builder createdAt(final LocalDateTime createdAt) {
      this.createdAt = createdAt;
      return this;
    }

    public Transaction build() {
      return new Transaction(this);
    }
  }
}
