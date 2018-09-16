package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import com.pwittchen.money.transfer.api.validation.implementation.DefaultTransactionValidation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ValidationModuleTest {

  private ValidationModule validationModule = new ValidationModule();

  @Mock
  private AccountRepository accountRepository;

  @Test
  public void shouldProvideTransactionValidation() {
    // when
    TransactionValidation validation = validationModule.provideTransactionValidation(
        accountRepository
    );

    // then
    assertThat(validation).isNotNull();
    assertThat(validation).isInstanceOf(DefaultTransactionValidation.class);
  }
}