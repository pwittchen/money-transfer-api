package com.pwittchen.money.transfer.api.model;

import org.joda.money.Money;

public final class Transaction {
  private final String id;
  private final Account from;
  private final Account to;
  private final Money money;
  private final Money fee;

  private Transaction() {
    this(builder());
  }

  protected Transaction(final Builder builder) {
    this(builder.id, builder.from, builder.to, builder.money, builder.fee);
  }

  protected Transaction(final String id, final Account from, final Account to, final Money money,
      final Money fee) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.money = money;
    this.fee = fee;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String id() {
    return id;
  }

  public static Builder id(final String id) {
    return builder().id(id);
  }

  public Account from() {
    return from;
  }

  public static Builder from(final Account from) {
    return builder().from(from);
  }

  public Account to() {
    return to;
  }

  public static Builder to(final Account to) {
    return builder().to(to);
  }

  public Money money() {
    return money;
  }

  public static Builder money(final Money money) {
    return builder().money(money);
  }

  public Money fee() {
    return fee;
  }

  public static Builder fee(final Money fee) {
    return builder().fee(fee);
  }

  public static class Builder {
    private String id;
    private Account from;
    private Account to;
    private Money money;
    private Money fee;

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

    public Builder fee(final Money fee) {
      this.fee = fee;
      return this;
    }

    public Transaction build() {
      return new Transaction(this);
    }
  }
}
