package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

//todo: analyze and consider writing more tests
@RunWith(MockitoJUnitRunner.class)
public class InMemoryTransactionRepositoryTest {

  private TransactionRepository transactionRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    transactionRepository = new InMemoryTransactionRepository();
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // it's not relevant for this test
  public void shouldGetCreatedTransaction() {
    // given
    Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    transactionRepository.create(transaction);

    // then
    Transaction createdTransaction = transactionRepository.get(transaction.id()).get();
    assertThat(createdTransaction.equals(transaction)).isTrue();
    assertThat(createdTransaction.id()).isEqualTo(transaction.id());
    assertThat(createdTransaction.fromNumber()).isEqualTo(transaction.fromNumber());
    assertThat(createdTransaction.toNumber()).isEqualTo(transaction.toNumber());
    assertThat(createdTransaction.money()).isEqualTo(transaction.money());
  }

  @Test
  public void shouldGetAllTransactions() throws Exception {
    // given
    Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));

    Transaction transactionOne = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    Transaction transactionTwo = Transaction
        .builder()
        .id("TR2")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    transactionRepository.create(transactionOne);
    transactionRepository.create(transactionTwo);

    // then
    assertThat(transactionRepository.getAll().size()).isEqualTo(2);
  }

  @Test
  public void shouldClearTransactions() {
    // given
    Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
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
        .user(createSenderUser())
        .number(number)
        .money(money)
        .build();
  }

  private User createSenderUser() {
    return User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("TestSender")
        .surname("TestSenderSurname")
        .build();
  }

  private Account createReceiverAccount(final String number, final Money money) {
    return Account
        .builder()
        .user(createReceiverUser())
        .number(number)
        .money(money)
        .build();
  }

  private User createReceiverUser() {
    return User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("testReceiver")
        .surname("testReceiverSurname")
        .build();
  }
}