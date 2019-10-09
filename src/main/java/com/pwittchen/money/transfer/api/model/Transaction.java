package com.pwittchen.money.transfer.api.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import org.joda.money.Money;

@SuppressWarnings("WeakerAccess") // public access of class attributes is required by OpenAPI
public class Transaction {
  private final transient AtomicBoolean isRunning;
  public final String id;
  public final String fromNumber;
  public final String toNumber;
  public final Money money;
  public final LocalDateTime createdAt;

  private Transaction() {
    this(builder());
  }

  private Transaction(final Builder builder) {
    this(builder.id, builder.from, builder.to, builder.money, builder.createdAt);
  }

  private Transaction(final String id, final String fromNumber, final String toNumber,
      final Money money, final LocalDateTime createdAt) {
    this.isRunning = new AtomicBoolean(true);
    this.id = id;
    this.fromNumber = fromNumber;
    this.toNumber = toNumber;
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

  public String fromNumber() {
    return fromNumber;
  }

  public String toNumber() {
    return toNumber;
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
        && Objects.equals(fromNumber, that.fromNumber)
        && Objects.equals(toNumber, that.toNumber)
        && Objects.equals(money, that.money)
        && Objects.equals(createdAt, that.createdAt);
  }

  @Override public int hashCode() {
    return Objects.hash(id, fromNumber, toNumber, money, createdAt);
  }

  public static class Builder {
    private String id;
    private String from;
    private String to;
    private Money money;
    private LocalDateTime createdAt;

    private Builder() {
    }

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public Builder fromNumber(final String from) {
      this.from = from;
      return this;
    }

    public Builder toNumber(final String to) {
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
