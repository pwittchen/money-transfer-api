package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.google.common.truth.Truth.assertThat;

public class InMemoryAccountRepositoryTest {

  private AccountRepository accountRepository = new InMemoryAccountRepository();

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

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
    accountRepository.create(account);

    // when
    Optional<Account> optional = accountRepository.get(accountNumber);

    // then
    assertThat(optional.isPresent()).isTrue();
  }

  @Test
  public void shouldGetAllAccounts() {
    // given
    accountRepository.create(createAccount());
    accountRepository.create(createAccount());

    // when
    Map<String, Account> accounts = accountRepository.get();

    // then
    assertThat(accounts.size()).isEqualTo(2);
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldCreateNewAccount() {
    // given
    Account account = createAccount();

    // when
    accountRepository.create(account);
    Account createdAccount = accountRepository.get(account.number()).get();

    // then
    assertThat(accountRepository.get().isEmpty()).isFalse();
    assertThat(createdAccount).isEqualTo(account);
    assertThat(createdAccount.user()).isEqualTo(account.user());
    assertThat(createdAccount.user().id()).isEqualTo(account.user().id());
    assertThat(createdAccount.user().name()).isEqualTo(account.user().name());
    assertThat(createdAccount.user().surname()).isEqualTo(account.user().surname());
    assertThat(createdAccount.number()).isEqualTo(account.number());
    assertThat(createdAccount.money()).isEqualTo(account.money());
  }

  @Test
  public void shouldNotCreateNewAccountWithNumberWhichAlreadyExists() {
    // given
    Account account = createAccount();
    accountRepository.create(account);

    // when
    expectedException.expect(AccountAlreadyExistsException.class);
    expectedException.expectMessage(
        new AccountAlreadyExistsException(account.number()).getMessage()
    );

    // then
    accountRepository.create(account);
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldUpdateAccount() {
    // given
    Account account = createAccount();
    Account anotherAccount = createAnotherAccount(account.number());
    accountRepository.create(account);

    // when
    accountRepository.update(account.number(), anotherAccount);

    // then
    assertThat(accountRepository.get(account.number()).get()).isEqualTo(anotherAccount);
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldUpdateAccountAndItsNumber() {
    // given
    Account account = createAccount();
    String newAccountNumber = UUID.randomUUID().toString();
    Account anotherAccount = createAnotherAccount(newAccountNumber);
    accountRepository.create(account);

    // when
    accountRepository.update(account.number(), anotherAccount);

    // then
    assertThat(accountRepository.get(newAccountNumber).get()).isEqualTo(anotherAccount);
    assertThat(accountRepository.get(account.number()).isPresent()).isFalse();
  }

  @Test
  public void shouldNotUpdateAccountIfItDoesNotExist() {
    // given
    Account account = createAccount();

    // when
    expectedException.expect(AccountNotExistsException.class);
    expectedException.expectMessage(new AccountNotExistsException(account.number()).getMessage());

    // then
    accountRepository.update(account.number(), account);
  }

  @Test
  public void shouldDeleteAccount() {
    // given
    Account account = createAccount();
    accountRepository.create(account);

    // when
    accountRepository.delete(account.number());

    // then
    assertThat(accountRepository.get(account.number()).isPresent()).isFalse();
  }

  @Test
  public void shouldNotDeleteAccountIfItDoesNotExist() {
    // given
    String numberWhichDoesNotExist = "numberWhichDoesNotExist";

    // when
    expectedException.expect(AccountNotExistsException.class);
    expectedException.expectMessage(
        new AccountNotExistsException(numberWhichDoesNotExist).getMessage()
    );

    // then
    accountRepository.delete(numberWhichDoesNotExist);
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

  private Account createAnotherAccount(String number) {
    return Account
        .builder()
        .user(createAnotherUser())
        .number(number)
        .money(Money.of(CurrencyUnit.GBP, 5))
        .build();
  }

  private User createAnotherUser() {
    return User
        .builder()
        .id(UUID.randomUUID().toString())
        .name("testName")
        .surname("testSurname")
        .build();
  }
}