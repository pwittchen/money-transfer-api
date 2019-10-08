package com.pwittchen.money.transfer.api.command.implementation;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.command.exception.DifferentCurrencyException;
import com.pwittchen.money.transfer.api.command.exception.NotEnoughMoneyException;
import com.pwittchen.money.transfer.api.command.exception.TransferToTheSameAccountException;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import java.util.Optional;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCommitTransactionCommandTest {

  @Mock private AccountRepository accountRepository;

  @Mock private TransactionRepository transactionRepository;

  @Rule public ExpectedException expectedException = ExpectedException.none();

  private CommitTransactionCommand commitTransactionCommand;

  @Before public void setUp() {
    commitTransactionCommand = new DefaultCommitTransactionCommand(
        accountRepository, transactionRepository
    );
  }

  @Test public void shouldCommitTransaction() {
    // given
    Account sender = spy(createSenderAccount(Money.of(CurrencyUnit.EUR, 100)));
    Account receiver = spy(createReceiverAccount(Money.of(CurrencyUnit.EUR, 50)));

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

  @Test public void shouldNotCommitTransactionWhenSenderHasNotEnoughMoney() {
    // given
    Account sender = createSenderAccount(Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount(Money.of(CurrencyUnit.EUR, 50));
    when(accountRepository.get("AC1")).thenReturn(Optional.of(sender));
    when(accountRepository.get("AC2")).thenReturn(Optional.of(receiver));

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

  @Test(expected = DifferentCurrencyException.class)
  public void shouldNotCommitTransactionWhenMoneyOnTwoAccountsHasDifferentCurrency() {
    // given
    Account sender = spy(createSenderAccount(Money.of(CurrencyUnit.EUR, 100)));
    Account receiver = spy(createReceiverAccount(Money.of(CurrencyUnit.GBP, 50)));

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
    verify(accountRepository, times(0)).transfer(sender, receiver, transaction.money());
    verify(transactionRepository, times(0)).create(transaction);
  }

  @Test(expected = TransferToTheSameAccountException.class)
  public void shouldNotCommitTransactionWhenTransferIsToTheSameAccount() {
    // given
    Account account = spy(createSenderAccount(Money.of(CurrencyUnit.EUR, 100)));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(account.number())
        .toNumber(account.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    when(accountRepository.get(account.number())).thenReturn(Optional.of(account));

    // when
    commitTransactionCommand.run(transaction);

    // then
    verify(accountRepository, times(0)).transfer(account, account, transaction.money());
    verify(transactionRepository, times(0)).create(transaction);
  }

  @Test(expected = AccountNotExistsException.class)
  public void shouldNotCommitTransactionWhenSenderAccountDoesNotExist() {
    // given
    Account sender = spy(createSenderAccount(Money.of(CurrencyUnit.EUR, 100)));
    Account receiver = spy(createReceiverAccount(Money.of(CurrencyUnit.EUR, 50)));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    when(accountRepository.get(sender.number())).thenReturn(Optional.empty());

    // when
    commitTransactionCommand.run(transaction);

    // then
    verify(accountRepository, times(0)).transfer(sender, receiver, transaction.money());
    verify(transactionRepository, times(0)).create(transaction);
  }

  @Test(expected = AccountNotExistsException.class)
  public void shouldNotCommitTransactionWhenReceiverAccountDoesNotExist() {
    // given
    Account sender = spy(createSenderAccount(Money.of(CurrencyUnit.EUR, 100)));
    Account receiver = spy(createReceiverAccount(Money.of(CurrencyUnit.EUR, 50)));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .fromNumber(sender.number())
        .toNumber(receiver.number())
        .money(Money.of(CurrencyUnit.EUR, 10))
        .build();

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.empty());

    // when
    commitTransactionCommand.run(transaction);

    // then
    verify(accountRepository, times(0)).transfer(sender, receiver, transaction.money());
    verify(transactionRepository, times(0)).create(transaction);
  }

  private Account createSenderAccount(final Money money) {
    return Account
        .builder()
        .owner("testSender")
        .number("AC1")
        .money(money)
        .build();
  }

  private Account createReceiverAccount(final Money money) {
    return Account
        .builder()
        .owner("testReceiver")
        .number("AC2")
        .money(money)
        .build();
  }
}