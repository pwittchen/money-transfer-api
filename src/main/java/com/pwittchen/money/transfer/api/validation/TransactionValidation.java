package com.pwittchen.money.transfer.api.validation;

import com.pwittchen.money.transfer.api.model.Transaction;
import java.util.Optional;

public interface TransactionValidation {
  Optional<Exception> getCommitError(Transaction transaction);
}
