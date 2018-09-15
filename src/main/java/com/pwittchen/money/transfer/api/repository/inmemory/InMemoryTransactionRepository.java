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
        //TODO: consider other validation variants -> check if accounts exist
        //TODO: consider using conversion rate or allow to transfer only the same currency

        Account sender = accountRepository.get(transaction.from().number()).get();
        sender.withdraw(moneyWithTransactionFee);

        Account receiver = accountRepository.get(transaction.to().number()).get();
        receiver.put(transaction.money());

        transactions.add(transaction);
      } else {
        emitter.onError(new NotEnoughMoneyException(transaction.from().number()));
      }
    });
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
