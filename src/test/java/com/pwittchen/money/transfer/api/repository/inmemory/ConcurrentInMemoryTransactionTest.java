package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import com.pwittchen.money.transfer.api.validation.implementation.DefaultAccountValidation;
import com.pwittchen.money.transfer.api.validation.implementation.DefaultTransactionValidation;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ConcurrentInMemoryTransactionTest {

  private static final int NUMBER_OF_THREADS = 3;

  private TransactionRepository transactionRepository;

  private AccountRepository accountRepository;

  private ExecutorService executorService;

  @Before
  public void setUp() {
    accountRepository = new InMemoryAccountRepository(new DefaultAccountValidation());
    TransactionValidation transactionValidation = new DefaultTransactionValidation(
        accountRepository
    );

    transactionRepository = new InMemoryTransactionRepository(
        accountRepository, transactionValidation
    );

    executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  }

  @After
  public void tearDown() {
    executorService.shutdown();
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public void shouldHandleConcurrentTransactions() throws Exception {
    // given
    final Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100));
    final Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50));
    accountRepository.create(sender);
    accountRepository.create(receiver);

    // send 5 EUR: AC1 -> AC2
    final Transaction transaction1 = Transaction
        .builder()
        .id("transaction_1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 5))
        .build();

    // send 10 EUR: AC1 -> AC2
    final Transaction transaction2 = Transaction
        .builder()
        .id("transaction_2")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // send 1 EUR: AC2 -> AC1
    final Transaction transaction3 = Transaction
        .builder()
        .id("transaction_3")
        .from(receiver)
        .to(sender)
        .money(Money.of(CurrencyUnit.EUR, 1))
        .build();

    // when
    executorService.submit(() -> commitTransaction(transaction1));
    executorService.submit(() -> commitTransaction(transaction2));
    executorService.submit(() -> commitTransaction(transaction3));

    executorService.awaitTermination(5, TimeUnit.SECONDS);

    // then
    Money senderMoney = accountRepository.get(sender.number()).get().money();
    Money receiverMoney = accountRepository.get(receiver.number()).get().money();

    assertThat(transactionRepository.get().size()).isEqualTo(3);
    assertThat(senderMoney).isEqualTo(Money.of(CurrencyUnit.EUR, 86));
    assertThat(receiverMoney).isEqualTo(Money.of(CurrencyUnit.EUR, 64));
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public void shouldPassOnlyOneTransaction() throws Exception {
    // given
    final Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 10));
    final Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 0));
    accountRepository.create(sender);
    accountRepository.create(receiver);

    // send 10 EUR: AC1 -> AC2
    final Transaction transaction1 = Transaction
        .builder()
        .id("transaction_1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // send 10 EUR: AC1 -> AC2
    final Transaction transaction2 = Transaction
        .builder()
        .id("transaction_2")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    executorService.submit(() -> commitTransaction(transaction1));
    executorService.submit(() -> commitTransaction(transaction2));

    executorService.awaitTermination(5, TimeUnit.SECONDS);

    // then
    Money senderMoney = accountRepository.get(sender.number()).get().money();
    Money receiverMoney = accountRepository.get(receiver.number()).get().money();

    assertThat(transactionRepository.get().size()).isEqualTo(1);
    assertThat(senderMoney).isEqualTo(Money.of(CurrencyUnit.EUR, 0));
    assertThat(receiverMoney).isEqualTo(Money.of(CurrencyUnit.EUR, 10));
  }

  private void commitTransaction(Transaction transaction) {
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
      transactionRepository.commit(transaction);
      System.out.println(String.format("executing: %s, thread: %s",
          transaction.id(),
          Thread.currentThread().getName())
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
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
