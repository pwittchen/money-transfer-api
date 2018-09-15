package com.pwittchen.money.transfer.api.model;

import java.math.BigDecimal;
import org.joda.money.Money;

public final class Transaction {
  private final String id;
  private final Account from;
  private final Account to;
  private final Money money;
  private final BigDecimal conversionRate;
  private final BigDecimal transactionFee;

  public Transaction(final String id, final Account from, final Account to, final Money money,
      final BigDecimal conversionRate, final BigDecimal transactionFee) {
    this.id = id;
    this.from = from;
    this.to = to;
    this.money = money;
    this.conversionRate = conversionRate;
    this.transactionFee = transactionFee;
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

  public BigDecimal conversionRate() {
    return conversionRate;
  }

  public BigDecimal transactionFee() {
    return transactionFee;
  }
}
