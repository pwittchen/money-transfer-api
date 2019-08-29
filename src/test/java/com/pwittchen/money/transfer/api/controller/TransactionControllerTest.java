package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.command.exception.TransferToTheSameAccountException;
import com.pwittchen.money.transfer.api.command.implementation.DefaultCommitTransactionCommand;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.implementation.DefaultGetTransactionQuery;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import io.javalin.http.Context;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
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

  @Mock private Transaction transaction;

  @Mock private Account account;

  private GetTransactionQuery getTransactionQuery;
  private GetAllTransactionsQuery getAllTransactionsQuery;
  private CommitTransactionCommand commitTransactionCommand;

  @Before public void setUp() {
    getTransactionQuery = spy(new DefaultGetTransactionQuery(transactionRepository));
    getAllTransactionsQuery = spy(new DefaultGetAllTransactionsQuery(transactionRepository));
    commitTransactionCommand = spy(new DefaultCommitTransactionCommand(
        accountRepository, transactionRepository
    ));

    this.controller = new TransactionController(
        contextWrapper,
        getTransactionQuery,
        getAllTransactionsQuery,
        commitTransactionCommand
    );
  }

  @Test public void shouldGetOneTransaction() {
    // given
    when(contextWrapper.pathParam(context, "id")).thenReturn("1");
    when(transactionRepository.get("1")).thenReturn(Optional.of(transaction));

    // when
    controller.getOne(context);

    // then
    verify(contextWrapper).json(context, transaction);
  }

  @Test public void shouldNotGetOneTransaction() {
    // given
    String id = "1";
    when(contextWrapper.pathParam(context, "id")).thenReturn(id);
    when(transactionRepository.get(id)).thenReturn(Optional.empty());

    // when
    controller.getOne(context);

    // then
    verify(contextWrapper).json(context,
        "transaction with id " + id + " does not exist",
        HttpStatus.NOT_FOUND_404);
  }

  @Test public void shouldGetAllTransactions() {
    // given
    BlockingQueue<Transaction> transactions = new LinkedBlockingQueue<>();
    when(transactionRepository.getAll()).thenReturn(transactions);

    // when
    controller.getAll(context);

    // then
    verify(contextWrapper).json(context, transactions);
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

    User user = User.builder()
        .id("testId")
        .name("John")
        .surname("Kovalsky")
        .build();

    Account account = Account.builder()
        .user(user)
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