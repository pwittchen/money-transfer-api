package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.exception.NotEnoughMoneyException;
import io.reactivex.Completable;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;
import org.joda.money.Money;

public class InMemoryTransactionRepository implements TransactionRepository {

  private final Queue<Transaction> transactions = new LinkedList<>();
  private final AccountRepository accountRepository;

  public InMemoryTransactionRepository(final AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override public Completable commit(Transaction transaction) {
    return Completable.create(emitter -> {
      Money senderBalance = getSenderBalance(transaction);
      Money moneyWithTransactionFee = getMoneyWithTransactionFee(transaction);

      if (senderBalance.isGreaterThan(moneyWithTransactionFee)) {
        //TODO: use DI
        //TODO: consider other validation variants
        //TODO: consider using conversion rate or allow to transfer only the same currency

        Account senderAccount = updateSenderAccount(transaction, moneyWithTransactionFee);
        Account receiverAccount = updateReceiverAccount(transaction, transaction.money());

        accountRepository.update(transaction.from().number(), senderAccount);
        accountRepository.update(transaction.to().number(), receiverAccount);
        transactions.add(transaction);
      } else {
        emitter.onError(new NotEnoughMoneyException(transaction.from().number()));
      }
    });
  }

  @NotNull
  private Account updateSenderAccount(Transaction transaction, Money moneyWithTransactionFee) {
    //TODO: refactor it to builder pattern
    return new Account(
        transaction.from().number(),
        transaction.from().user(),
        transaction.from().allowedDebit(),
        transaction.from().money().minus(moneyWithTransactionFee));
  }

  @NotNull
  private Account updateReceiverAccount(Transaction transaction, Money money) {
    //TODO: refactor it to builder pattern
    return new Account(
        transaction.from().number(),
        transaction.from().user(),
        transaction.from().allowedDebit(),
        transaction.from().money().plus(money));
  }

  @NotNull private Money getMoneyWithTransactionFee(Transaction transaction) {
    return transaction
        .money()
        .plus(transaction.fee());
  }

  @NotNull private Money getSenderBalance(Transaction transaction) {
    return transaction
        .from()
        .money()
        .plus(transaction.from().allowedDebit());
  }

  @Override public Optional<Transaction> get(String id) {
    return transactions
        .stream()
        .filter(transaction -> transaction.id().equals(id))
        .findFirst();
  }

  @Override public Queue<Transaction> get() {
    return transactions;
  }
}
