package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.repository.exception.AccountAlreadyExistsException;
import io.reactivex.observers.TestObserver;
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

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test check is not needed
  public void shouldCreateNewAccount() {
    // given
    TestObserver testObserver = new TestObserver();
    Account account = createAccount();

    // when
    accountRepository.create(account).subscribe(testObserver);
    Account createdAccount = accountRepository.get(account.number()).get();

    // then
    testObserver.assertComplete();
    assertThat(accountRepository.get().isEmpty()).isFalse();
    assertThat(createdAccount).isEqualTo(account);
    assertThat(createdAccount.user()).isEqualTo(account.user());
    assertThat(createdAccount.number()).isEqualTo(account.number());
    assertThat(createdAccount.money()).isEqualTo(account.money());
  }

  @Test
  public void shouldNotCreateNewAccountWithNumberWhichAlreadyExists() {
    // given
    TestObserver testObserver = new TestObserver();
    Account account = createAccount();
    accountRepository.create(account).subscribe();

    // when
    accountRepository.create(account).subscribe(testObserver);

    // then
    testObserver.assertError(AccountAlreadyExistsException.class);
  }

  @Test
  public void shouldUpdateAccount() {
    //TODO: implement
  }

  @Test
  public void shouldUpdateAccountAndItsNumber() {
    //TODO: implement

  }

  @Test
  public void shouldNotUpdateAccountIfItDoesNotExist() {
    //TODO: implement

  }

  @Test
  public void shouldDeleteAccount() {
    //TODO: implement

  }

  @Test
  public void shouldNotDeleteAccountIfItDoesNotExist() {
    //TODO: implement
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