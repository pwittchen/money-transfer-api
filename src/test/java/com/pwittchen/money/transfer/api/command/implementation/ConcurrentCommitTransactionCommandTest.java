package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryAccountRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryTransactionRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import net.jodah.concurrentunit.Waiter;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ConcurrentCommitTransactionCommandTest {

  private static final int NUMBER_OF_THREADS = 4;

  private CommitTransactionCommand commitTransactionCommand;
  private TransactionRepository transactionRepository;
  private AccountRepository accountRepository;
  private ExecutorService executorService;
  private Waiter waiter;

  @Before public void setUp() {
    accountRepository = new InMemoryAccountRepository();
    transactionRepository = new InMemoryTransactionRepository();
    commitTransactionCommand = new DefaultCommitTransactionCommand(
        accountRepository, transactionRepository
    );

    waiter = new Waiter();
    executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
  }

  @After public void tearDown() {
    executorService.shutdown();
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public void shouldHandleConcurrentTransactions() throws Exception {
    // given
    double initialBalanceOne = 100;
    double initialBalanceTwo = 50;

    double money1 = 5;
    double money2 = 10;
    double money3 = 11;
    double money4 = 25;

    final Account accountOne = createSenderAccount(
        "AC1", Money.of(CurrencyUnit.EUR, initialBalanceOne)
    );
    final Account accountTwo = createReceiverAccount(
        "AC2", Money.of(CurrencyUnit.EUR, initialBalanceTwo)
    );

    double expectedBalanceOne = initialBalanceOne - money1 - money2 + money3 + money4;
    double expectedBalanceTwo = initialBalanceTwo + money1 + money2 - money3 - money4;
    accountRepository.create(accountOne);
    accountRepository.create(accountTwo);

    // send 5 EUR: AC1 -> AC2
    final Transaction transaction1 = Transaction
        .builder()
        .id("transaction_1")
        .createdAt(LocalDateTime.now())
        .fromNumber(accountOne.number())
        .toNumber(accountTwo.number())
        .money(Money.of(CurrencyUnit.EUR, money1))
        .build();

    // send 10 EUR: AC1 -> AC2
    final Transaction transaction2 = Transaction
        .builder()
        .id("transaction_2")
        .createdAt(LocalDateTime.now())
        .fromNumber(accountOne.number())
        .toNumber(accountTwo.number())
        .money(Money.of(CurrencyUnit.EUR, money2))
        .build();

    // send 11 EUR: AC2 -> AC1
    final Transaction transaction3 = Transaction
        .builder()
        .id("transaction_3")
        .createdAt(LocalDateTime.now())
        .fromNumber(accountTwo.number())
        .toNumber(accountOne.number())
        .money(Money.of(CurrencyUnit.EUR, money3))
        .build();

    // send 25 EUR: AC2 -> AC1
    final Transaction transaction4 = Transaction
        .builder()
        .id("transaction_4")
        .createdAt(LocalDateTime.now())
        .fromNumber(accountTwo.number())
        .toNumber(accountOne.number())
        .money(Money.of(CurrencyUnit.EUR, money4))
        .build();

    // when
    executorService.submit(() -> commitTransaction(transaction1));
    executorService.submit(() -> commitTransaction(transaction2));
    executorService.submit(() -> commitTransaction(transaction3));
    executorService.submit(() -> commitTransaction(transaction4));

    waiter.await(5, TimeUnit.SECONDS, 4);

    // then
    Money senderMoney = accountRepository.get(accountOne.number()).get().money();
    Money receiverMoney = accountRepository.get(accountTwo.number()).get().money();

    assertThat(transactionRepository.getAll().size()).isEqualTo(4);
    assertThat(senderMoney).isEqualTo(Money.of(CurrencyUnit.EUR, expectedBalanceOne));
    assertThat(receiverMoney).isEqualTo(Money.of(CurrencyUnit.EUR, expectedBalanceTwo));
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  public void shouldPassOnlyOneTransactionBecauseOfLimitedMoney() throws Exception {
    // given
    final Account sender = createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 10));
    final Account receiver = createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 0));
    accountRepository.create(sender);
    accountRepository.create(receiver);

    // send 10 EUR: AC1 -> AC2
    final Transaction transaction1 = Transaction
        .builder()
        .id("transaction_1")
        .createdAt(LocalDateTime.now())
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // send 10 EUR: AC1 -> AC2
    final Transaction transaction2 = Transaction
        .builder()
        .id("transaction_2")
        .createdAt(LocalDateTime.now())
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    // when
    executorService.submit(() -> commitTransaction(transaction1));
    executorService.submit(() -> commitTransaction(transaction2)); // should produce an exception

    waiter.await(5, TimeUnit.SECONDS, 1);

    // then
    Money senderMoney = accountRepository.get(sender.number()).get().money();
    Money receiverMoney = accountRepository.get(receiver.number()).get().money();

    assertThat(transactionRepository.getAll().size()).isEqualTo(1);
    assertThat(senderMoney).isEqualTo(Money.of(CurrencyUnit.EUR, 0));
    assertThat(receiverMoney).isEqualTo(Money.of(CurrencyUnit.EUR, 10));
  }

  private void commitTransaction(Transaction transaction) {
    try {
      Thread.sleep(ThreadLocalRandom.current().nextInt(3000));
      commitTransactionCommand.run(transaction);
      waiter.assertNotNull(transaction);
      System.out.println(String.format("executing: %s, %s -> %s (%s), time: %d ms, thread: %s",
          transaction.id(),
          transaction.fromNumber(),
          transaction.toNumber(),
          transaction.money(),
          TimeUnit.MILLISECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS),
          Thread.currentThread().getName())
      );
      waiter.resume();
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
