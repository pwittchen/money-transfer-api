package com.pwittchen.money.transfer.api.controller;

import com.pwittchen.money.transfer.api.command.CommitTransactionCommand;
import com.pwittchen.money.transfer.api.controller.context.ContextWrapper;
import com.pwittchen.money.transfer.api.model.Transaction;
import com.pwittchen.money.transfer.api.query.GetAllTransactionsQuery;
import com.pwittchen.money.transfer.api.query.GetTransactionQuery;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;
import org.eclipse.jetty.http.HttpStatus;
import org.joda.money.Money;

public class TransactionController {

  private ContextWrapper contextWrapper;
  private GetTransactionQuery getTransactionQuery;
  private GetAllTransactionsQuery getAllTransactionsQuery;
  private CommitTransactionCommand commitTransactionCommand;

  @Inject
  public TransactionController(
      ContextWrapper contextWrapper,
      GetTransactionQuery getTransactionQuery,
      GetAllTransactionsQuery getAllTransactionsQuery,
      CommitTransactionCommand commitTransactionCommand
  ) {
    this.contextWrapper = contextWrapper;
    this.getTransactionQuery = getTransactionQuery;
    this.getAllTransactionsQuery = getAllTransactionsQuery;
    this.commitTransactionCommand = commitTransactionCommand;
  }

  @OpenApi(
      method = HttpMethod.GET,
      path = "/transaction/:id",
      description = "gets single transaction with a given id",
      pathParams = @OpenApiParam(name = "id", type = Integer.class),
      responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transaction.class)),
          @OpenApiResponse(status = "404", content = @OpenApiContent(from = String.class))
      }
  )
  public void getOne(final Context context) {
    String id = contextWrapper.pathParam(context, "id");
    Optional<Transaction> transaction = getTransactionQuery.run(id);

    if (transaction.isPresent()) {
      contextWrapper.json(context, transaction.get());
    } else {
      String message = String.format("transaction with id %s does not exist", id);
      contextWrapper.json(context, message, HttpStatus.NOT_FOUND_404);
    }
  }

  @OpenApi(
      method = HttpMethod.GET,
      path = "/transaction",
      description = "gets all transactions",
      responses = @OpenApiResponse(
          status = "200",
          content = @OpenApiContent(from = Transaction.class, isArray = true)
      )
  )
  public void getAll(final Context context) {
    contextWrapper.json(context, getAllTransactionsQuery.run());
  }

  @OpenApi(
      method = HttpMethod.POST,
      path = "/transaction",
      description = "commits a transaction",
      pathParams = {
          @OpenApiParam(name = "from"),
          @OpenApiParam(name = "to"),
          @OpenApiParam(name = "currency"),
          @OpenApiParam(name = "money")
      },
      responses = {
          @OpenApiResponse(status = "200", content = @OpenApiContent(from = Transaction.class)),
          @OpenApiResponse(status = "400", content = @OpenApiContent(from = String.class))
      }
  )
  public void commit(final Context context) {
    String from = contextWrapper.formParam(context, "from");
    String to = contextWrapper.formParam(context, "to");
    Optional<Money> money = parseMoney(context);

    if (money.isEmpty()) {
      contextWrapper.json(context, "invalid money format", HttpStatus.BAD_REQUEST_400);
      return;
    }

    commit(context, createTransaction(from, to, money.get()));
  }

  private Optional<Money> parseMoney(Context context) {
    try {
      Money money = Money.parse(String.format("%s %s",
          contextWrapper.formParam(context, "currency"),
          contextWrapper.formParam(context, "money"))
      );
      return Optional.of(money);
    } catch (Exception exception) {
      return Optional.empty();
    }
  }

  private Transaction createTransaction(String senderNumber, String receiverNumber, Money money) {
    return Transaction.builder()
        .id(UUID.randomUUID().toString())
        .createdAt(LocalDateTime.now())
        .fromNumber(senderNumber)
        .toNumber(receiverNumber)
        .money(money)
        .build();
  }

  private void commit(final Context context, final Transaction transaction) {
    try {
      commitTransactionCommand.run(transaction);
      contextWrapper.json(context, transaction, HttpStatus.OK_200);
    } catch (Exception exception) {
      contextWrapper.json(context, exception.getMessage(), HttpStatus.BAD_REQUEST_400);
    }
  }
}
