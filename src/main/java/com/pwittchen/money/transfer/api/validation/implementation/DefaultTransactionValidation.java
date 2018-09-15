package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.repository.exception.DifferentCurrencyException;
import com.pwittchen.money.transfer.api.repository.exception.NotEnoughMoneyException;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.joda.money.Money;

public class DefaultTransactionValidation implements TransactionValidation {

  private final AccountRepository accountRepository;

  public DefaultTransactionValidation(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public Optional<Exception> getCommitError(Transaction transaction) {
    return createCommitValidationRules(transaction)
        .entrySet()
        .stream()
        .filter(entry -> entry.getKey().equals(Boolean.TRUE))
        .findFirst()
        .map(Map.Entry::getValue);
  }

  private Map<Boolean, Exception> createCommitValidationRules(final Transaction transaction) {
    Map<Boolean, Exception> rules = new HashMap<>();

    rules.put(
        !accountRepository.get(transaction.from().number()).isPresent(),
        new AccountNotExistsException(transaction.from().number())
    );

    rules.put(
        !accountRepository.get(transaction.to().number()).isPresent(),
        new AccountNotExistsException(transaction.to().number())
    );

    rules.put(
        getSenderBalance(transaction).isLessThan(getMoneyWithTransactionFee(transaction)),
        new NotEnoughMoneyException(transaction.from().number())
    );

    rules.put(
        !transaction.from().money().isSameCurrency(transaction.to().money()),
        new DifferentCurrencyException(transaction.from().number(), transaction.to().number())
    );

    return rules;
  }

  @NotNull private Money getSenderBalance(Transaction transaction) {
    return transaction
        .from()
        .money()
        .plus(transaction.from().allowedDebit());
  }

  @NotNull private Money getMoneyWithTransactionFee(Transaction transaction) {
    return transaction
        .money()
        .plus(transaction.fee());
  }
}
