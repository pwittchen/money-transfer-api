package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryAccountRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryTransactionRepository;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.TransactionValidation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryModuleTest {

  private RepositoryModule repositoryModule = new RepositoryModule();

  @Mock
  private AccountRepository accountRepository;

  @Mock
  private TransactionValidation transactionValidation;

  @Mock
  private AccountValidation accountValidation;

  @Test
  public void shouldProvideAccountRepository() {
    // when
    AccountRepository accountRepository = repositoryModule.provideAccountRepository(
        accountValidation
    );

    // then
    assertThat(accountRepository).isNotNull();
    assertThat(accountRepository).isInstanceOf(InMemoryAccountRepository.class);
  }

  @Test
  public void shouldProvideTransactionRepository() {
    // when
    TransactionRepository transactionRepository = repositoryModule.provideTransactionRepository(
        accountRepository, transactionValidation
    );

    assertThat(transactionRepository).isNotNull();
    assertThat(transactionRepository).isInstanceOf(InMemoryTransactionRepository.class);
  }
}