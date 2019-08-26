package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.exception.NotEnoughMoneyException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//todo: analyze and optionally write more tests
@RunWith(MockitoJUnitRunner.class)
public class DefaultCommitTransactionCommandTest {

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private CommitTransactionCommand commitTransactionCommand;

  @Before
  public void setUp() {
    commitTransactionCommand = new DefaultCommitTransactionCommand(
        accountRepository, transactionRepository
    );
  }

  @Test
  public void shouldNotCommitTransactionWhenSenderHasNotEnoughMoney() throws Exception {
    // given
    final String senderNumber = "AC1";
    final String receiverNumber = "AC2";
    Account sender = createSenderAccount(senderNumber, Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount(receiverNumber, Money.of(CurrencyUnit.EUR, 50));
    when(accountRepository.get(senderNumber)).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiverNumber)).thenReturn(Optional.of(receiver));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 600))
        .build();

    // when
    expectedException.expect(NotEnoughMoneyException.class);
    expectedException.expectMessage(new NotEnoughMoneyException(sender.number()).getMessage());

    // then
    commitTransactionCommand.run(transaction);
  }

  @Test
  public void shouldCommitTransaction() {
    // given
    Account sender = spy(createSenderAccount("AC1", Money.of(CurrencyUnit.EUR, 100)));
    Account receiver = spy(createReceiverAccount("AC2", Money.of(CurrencyUnit.EUR, 50)));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.of(receiver));

    // when
    commitTransactionCommand.run(transaction);

    // then
    verify(accountRepository).transfer(sender, receiver, transaction.money());
    verify(transactionRepository).create(transaction);
  }

  @Test
  public void shouldGetErrorWhenSenderBalanceIsLessThanMoneyToBeSend() {
    // given
    Account sender = createSenderAccount("PL1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("PL2", Money.of(CurrencyUnit.EUR, 0));

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.of(receiver));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 120))
        .build();

    // when
    expectedException.expect(NotEnoughMoneyException.class);
    expectedException.expectMessage(new NotEnoughMoneyException(sender.number()).getMessage());

    // then
    commitTransactionCommand.run(transaction);
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