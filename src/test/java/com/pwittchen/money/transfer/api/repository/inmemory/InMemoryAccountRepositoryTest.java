package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class InMemoryAccountRepositoryTest {

  private AccountRepository accountRepository = new InMemoryAccountRepository();

  @Test
  public void shouldGetEmptyResultWhenAccountDoesNotExist() {
    // given
    String invalidNumber = "invalidNumber";

    // when
    Optional<Account> optional = accountRepository.get(invalidNumber);

    // then
    assertThat(optional.isPresent()).isFalse();
  }

  @Test
  public void shouldGetAccountWhenItExists() {
    // given
    Account account = createAccount();
    String accountNumber = account.number();
    accountRepository.create(account).subscribe();

    // when
    Optional<Account> optional = accountRepository.get(accountNumber);

    // then
    assertThat(optional.isPresent()).isTrue();
  }

  @Test
  public void shouldGetAllAccounts() {
    // given
    accountRepository.create(createAccount()).subscribe();
    accountRepository.create(createAccount()).subscribe();

    // when
    Map<String, Account> accounts = accountRepository.get();

    // then
    assertThat(accounts.size()).isEqualTo(2);
  }

  private Account createAccount() {
    return Account
        .builder()
        .user(createUser())
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 0))
        .build();
  }

  private User createUser() {
    return User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("John")
        .surname("Doe")
        .build();
  }
}