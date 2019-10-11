package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.exception.TransferToTheSameAccountException;
import com.pwittchen.money.transfer.api.command.implementation.DefaultCommitTransactionCommand;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.http.Context;
import java.util.Optional;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

  private TransactionController controller;

  @Mock private TransactionRepository transactionRepository;

  @Mock private AccountRepository accountRepository;

  @Mock private ContextWrapper contextWrapper;

  @Mock private Context context;

  @Mock private Account account;

  @Mock private GetAllTransactionsQuery getAllTransactionsQuery;

  private CommitTransactionCommand commitTransactionCommand;

  @Before public void setUp() {
    commitTransactionCommand = spy(new DefaultCommitTransactionCommand(
        accountRepository, transactionRepository
    ));

    this.controller = new TransactionController(
        contextWrapper,
        getAllTransactionsQuery,
        commitTransactionCommand
    );
  }

  @Test public void shouldCommitTransaction() throws Exception {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(accountRepository.get(senderNo)).thenReturn(Optional.of(account));
    when(accountRepository.get(receiverNo)).thenReturn(Optional.of(account));
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");

    // when
    controller.commit(context);

    // then
    verify(commitTransactionCommand).run(any(Transaction.class));
  }

  @Test public void shouldNotCommitTransactionIfSenderIsNotPresent() {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");
    when(accountRepository.get(senderNo)).thenReturn(Optional.empty());

    // when
    controller.commit(context);

    // then
    verify(contextWrapper).json(context,
        "Account with number " + senderNo + " does not exist",
        HttpStatus.BAD_REQUEST_400);
  }

  @Test public void shouldNotCommitTransactionIfReceiverIsNotPresent() {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");
    when(accountRepository.get(senderNo)).thenReturn(Optional.of(account));
    when(accountRepository.get(receiverNo)).thenReturn(Optional.empty());

    // when
    controller.commit(context);

    // then
    verify(contextWrapper).json(context,
        "Account with number " + receiverNo + " does not exist",
        HttpStatus.BAD_REQUEST_400);
  }

  @Test public void shouldNotCommitTransactionIfCurrencyHasInvalidFormat() {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(contextWrapper.formParam(context, "currency")).thenReturn("INVALID");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");

    // when
    controller.commit(context);

    // then
    verify(commitTransactionCommand, times(0)).run(any(Transaction.class));
    verify(contextWrapper).json(context, "invalid money format",
        HttpStatus.BAD_REQUEST_400);
  }

  @Test public void shouldNotCommitTransactionIfMoneyHasInvalidFormat() {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("INVALID");

    // when
    controller.commit(context);

    // then
    verify(commitTransactionCommand, times(0)).run(any(Transaction.class));
    verify(contextWrapper).json(context,
        "invalid money format",
        HttpStatus.BAD_REQUEST_400);
  }

  @Test public void shouldNotCommitTransactionWhenSenderAndReceiverNumberIsTheSame() {
    // given
    String accountNumber = "senderNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(accountNumber);
    when(contextWrapper.formParam(context, "to")).thenReturn(accountNumber);
    when(accountRepository.get(accountNumber)).thenReturn(Optional.of(account));
    when(accountRepository.get(accountNumber)).thenReturn(Optional.of(account));
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");

    TransferToTheSameAccountException exception = new TransferToTheSameAccountException();

    Account account = Account.builder()
        .owner("testOwner")
        .number(accountNumber)
        .money(Money.of(CurrencyUnit.EUR, 20))
        .build();

    when(accountRepository.get(accountNumber)).thenReturn(Optional.of(account));

    // when
    controller.commit(context);

    // then
    verify(contextWrapper).json(context, exception.getMessage(), HttpStatus.BAD_REQUEST_400);
  }
}