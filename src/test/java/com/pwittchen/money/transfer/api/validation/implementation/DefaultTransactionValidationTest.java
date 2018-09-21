package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.validation.exception.DifferentCurrencyException;
import com.pwittchen.money.transfer.api.validation.exception.TransferToTheSameAccountException;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultTransactionValidationTest {

  private TransactionValidation transactionValidation;

  @Mock
  private AccountRepository accountRepository;

  @Before
  public void setUp() {
    transactionValidation = new DefaultTransactionValidation(accountRepository);
  }

  @Test
  public void shouldNotGetCommitErrorForValidTransaction() {
    // given
    Account sender = createSenderAccount("PL1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("PL2", Money.of(CurrencyUnit.EUR, 0));

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.of(receiver));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    Optional<Exception> error = transactionValidation.validate(transaction);

    // then
    assertThat(error.isPresent()).isFalse();
  }

  @Test
  public void shouldGetErrorWhenSenderAccountDoesNotExist() {
    // given
    Account sender = createSenderAccount("PL1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("PL2", Money.of(CurrencyUnit.EUR, 0));

    when(accountRepository.get(sender.number())).thenReturn(Optional.empty());
    when(accountRepository.get(receiver.number())).thenReturn(Optional.of(receiver));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    Optional<Exception> error = transactionValidation.validate(transaction);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(exception -> {
      assertThat(exception).isInstanceOf(AccountNotExistsException.class);
      assertThat(exception.getMessage()).isEqualTo(
          new AccountNotExistsException(sender.number()).getMessage()
      );
    });
  }

  @Test
  public void shouldGetErrorWhenReceiverAccountDoesNotExist() {
    // given
    Account sender = createSenderAccount("PL1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("PL2", Money.of(CurrencyUnit.EUR, 0));

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.empty());

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    Optional<Exception> error = transactionValidation.validate(transaction);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(exception -> {
      assertThat(exception).isInstanceOf(AccountNotExistsException.class);
      assertThat(exception.getMessage()).isEqualTo(
          new AccountNotExistsException(receiver.number()).getMessage()
      );
    });
  }

  @Test
  public void shouldGetErrorWhenTwoAccountsHaveFundsInDifferentCurrencies() {
    // given
    Account sender = createSenderAccount("PL1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("PL2", Money.of(CurrencyUnit.GBP, 0));

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.of(receiver));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    Optional<Exception> error = transactionValidation.validate(transaction);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(exception -> {
      assertThat(exception).isInstanceOf(DifferentCurrencyException.class);
      assertThat(exception.getMessage()).isEqualTo(
          new DifferentCurrencyException(sender.number(), receiver.number()).getMessage()
      );
    });
  }

  @Test
  public void shouldGetErrorWhenSenderAndReceiverHasTheSameAccountNumber() {
    // given
    Account sender = createSenderAccount("PL1", Money.of(CurrencyUnit.EUR, 100));
    Account receiver = createReceiverAccount("PL1", Money.of(CurrencyUnit.EUR, 0));

    when(accountRepository.get(sender.number())).thenReturn(Optional.of(sender));
    when(accountRepository.get(receiver.number())).thenReturn(Optional.of(receiver));

    Transaction transaction = Transaction
        .builder()
        .id("TR1")
        .from(sender)
        .to(receiver)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    // when
    Optional<Exception> error = transactionValidation.validate(transaction);

    // then
    assertThat(error.isPresent()).isTrue();
    error.ifPresent(exception -> {
      assertThat(exception).isInstanceOf(TransferToTheSameAccountException.class);
      assertThat(exception.getMessage()).isEqualTo(
          new TransferToTheSameAccountException().getMessage()
      );
    });
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