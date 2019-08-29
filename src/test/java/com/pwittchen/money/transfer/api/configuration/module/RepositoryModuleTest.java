package com.pwittchen.money.transfer.api.configuration.module;

import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.TransactionRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryAccountRepository;
import com.pwittchen.money.transfer.api.repository.inmemory.InMemoryTransactionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RepositoryModuleTest {

  private RepositoryModule repositoryModule = new RepositoryModule();

  @Test public void shouldProvideAccountRepository() {
    // when
    AccountRepository accountRepository = repositoryModule.provideAccountRepository();

    // then
    assertThat(accountRepository).isNotNull();
    assertThat(accountRepository).isInstanceOf(InMemoryAccountRepository.class);
  }

  @Test
  public void shouldProvideTransactionRepository() {
    // when
    TransactionRepository transactionRepository = repositoryModule.provideTransactionRepository();

    assertThat(transactionRepository).isNotNull();
    assertThat(transactionRepository).isInstanceOf(InMemoryTransactionRepository.class);
  }
}