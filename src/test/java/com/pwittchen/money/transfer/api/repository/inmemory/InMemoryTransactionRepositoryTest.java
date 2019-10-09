package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryTransactionRepositoryTest {

  private TransactionRepository transactionRepository;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    transactionRepository = new InMemoryTransactionRepository();
  }

  @Test public void shouldGetCreatedTransaction() {
    // given
    Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender.number())
        .to(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    transactionRepository.create(transaction);

    // then
    //noinspection OptionalGetWithoutIsPresent
    Transaction createdTransaction = transactionRepository.get(transaction.id()).get();
    assertThat(createdTransaction.equals(transaction)).isTrue();
    assertThat(createdTransaction.id()).isEqualTo(transaction.id());
    assertThat(createdTransaction.from()).isEqualTo(transaction.from());
    assertThat(createdTransaction.to()).isEqualTo(transaction.to());
    assertThat(createdTransaction.money()).isEqualTo(transaction.money());
  }

  @Test public void shouldGetAllTransactions() {
    // given
    Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));

    Transaction transactionOne = Transaction
        .builder()
        .id("TR1")
        .from(sender.number())
        .to(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction
        .builder()
        .id("TR2")
        .from(sender.number())
        .to(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    transactionRepository.create(transactionOne);
    transactionRepository.create(transactionTwo);

    // then
    assertThat(transactionRepository.getAll().size()).isEqualTo(2);
  }

  @Test public void shouldClearTransactions() {
    // given
    Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender.number())
        .to(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    transactionRepository.create(transaction);
    transactionRepository.create(transaction);
    transactionRepository.clear();

    // then
    assertThat(transactionRepository.getAll().isEmpty()).isTrue();
  }

  private Account createSenderAccount(final String number, final Money money) {
    return Account
        .builder()
        .owner("senderOwner")
        .number(number)
        .money(money)
        .build();
  }

  private Account createReceiverAccount(final String number, final Money money) {
    return Account
        .builder()
        .owner("receiverOwner")
        .number(number)
        .money(money)
        .build();
  }
}