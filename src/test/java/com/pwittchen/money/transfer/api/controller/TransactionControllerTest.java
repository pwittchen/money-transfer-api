package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.Response;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.validation.exception.TransferToTheSameAccountException;
import io.javalin.http.Context;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TransactionControllerTest {

  private TransactionController controller;

  @Mock
  private TransactionRepository transactionRepository;

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private ContextWrapper contextWrapper;

  @Mock
  private Context context;

  @Mock
  private Transaction transaction;

  @Mock
  private Account account;

  @Before
  public void setUp() {
    this.controller = new TransactionController(
        contextWrapper, transactionRepository, accountRepository
    );
  }

  @Test
  public void shouldGetOneTransaction() {
    // given
    when(contextWrapper.pathParam(context, "id")).thenReturn("1");
    when(transactionRepository.get("1")).thenReturn(Optional.of(transaction));

    // when
    controller.getOne(context);

    // then
    verify(contextWrapper).json(context, transaction);
  }

  @Test
  public void shouldNotGetOneTransaction() {
    // given
    String id = "1";
    when(contextWrapper.pathParam(context, "id")).thenReturn(id);
    when(transactionRepository.get(id)).thenReturn(Optional.empty());
    Response response = Response.builder()
        .message("transaction with id 1 does not exist")
        .build();

    // when
    controller.getOne(context);

    // then
    verify(contextWrapper).json(context, response, 404);
  }

  @Test
  public void shouldGetAllTransactions() {
    // given
    LinkedList<Transaction> transactions = new LinkedList<>();
    when(transactionRepository.get()).thenReturn(transactions);

    // when
    controller.getAll(context);

    // then
    verify(contextWrapper).json(context, transactions);
  }

  @Test
  public void shouldCommitTransaction() throws Exception {
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
    verify(transactionRepository).commit(any(Transaction.class));
  }

  @Test
  public void shouldNotCommitTransactionIfSenderIsNotPresent() throws Exception {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(accountRepository.get(senderNo)).thenReturn(Optional.empty());
    when(accountRepository.get(receiverNo)).thenReturn(Optional.of(account));
    Response response = Response.builder()
        .message("Trying to transfer money from or to account, which does not exist")
        .build();

    // when
    controller.commit(context);

    // then
    verify(transactionRepository, times(0)).commit(any(Transaction.class));
    verify(contextWrapper).json(context, response);
  }

  @Test
  public void shouldNotCommitTransactionIfReceiverIsNotPresent() throws Exception {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(accountRepository.get(senderNo)).thenReturn(Optional.of(account));
    when(accountRepository.get(receiverNo)).thenReturn(Optional.empty());
    Response response = Response.builder()
        .message("Trying to transfer money from or to account, which does not exist")
        .build();

    // when
    controller.commit(context);

    // then
    verify(transactionRepository, times(0)).commit(any(Transaction.class));
    verify(contextWrapper).json(context, response);
  }

  @Test
  public void shouldNotCommitTransactionIfCurrencyHasInvalidFormat() throws Exception {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(accountRepository.get(senderNo)).thenReturn(Optional.of(account));
    when(accountRepository.get(receiverNo)).thenReturn(Optional.of(account));
    when(contextWrapper.formParam(context, "currency")).thenReturn("INVALID");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");
    Response response = Response.builder().message("invalid money format").build();

    // when
    controller.commit(context);

    // then
    verify(transactionRepository, times(0)).commit(any(Transaction.class));
    verify(contextWrapper).json(context, response);
  }

  @Test
  public void shouldNotCommitTransactionIfMoneyHasInvalidFormat() throws Exception {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(accountRepository.get(senderNo)).thenReturn(Optional.of(account));
    when(accountRepository.get(receiverNo)).thenReturn(Optional.of(account));
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("INVALID");
    Response response = Response.builder().message("invalid money format").build();

    // when
    controller.commit(context);

    // then
    verify(transactionRepository, times(0)).commit(any(Transaction.class));
    verify(contextWrapper).json(context, response);
  }

  @Test
  public void shouldNotCommitTransactionIfValidationErrorOccurred() throws Exception {
    // given
    String senderNo = "senderNo";
    String receiverNo = "receiverNo";
    when(contextWrapper.formParam(context, "from")).thenReturn(senderNo);
    when(contextWrapper.formParam(context, "to")).thenReturn(receiverNo);
    when(accountRepository.get(senderNo)).thenReturn(Optional.of(account));
    when(accountRepository.get(receiverNo)).thenReturn(Optional.of(account));
    when(contextWrapper.formParam(context, "currency")).thenReturn("EUR");
    when(contextWrapper.formParam(context, "money")).thenReturn("10.00");

    TransferToTheSameAccountException exception = new TransferToTheSameAccountException();
    when(transactionRepository.commit(any(Transaction.class))).thenThrow(exception);
    Response response = Response.builder()
        .message(exception.getMessage())
        .build();

    // when
    controller.commit(context);

    // then
    verify(contextWrapper).json(context, response);
  }
}