package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import io.reactivex.observers.TestObserver;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

@SuppressWarnings("unchecked") // these warnings are not relevant for unit tests below
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
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
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
    assertThat(createdAccount.user().id()).isEqualTo(account.user().id());
    assertThat(createdAccount.user().name()).isEqualTo(account.user().name());
    assertThat(createdAccount.user().surname()).isEqualTo(account.user().surname());
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
    testObserver.assertErrorMessage(
        new AccountAlreadyExistsException(account.number()).getMessage()
    );
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldUpdateAccount() {
    // given
    TestObserver testObserver = new TestObserver();
    Account account = createAccount();
    Account anotherAccount = createAnotherAccount(account.number());
    accountRepository.create(account).subscribe();

    // when
    accountRepository.update(account.number(), anotherAccount).subscribe(testObserver);

    // then
    testObserver.assertComplete();
    assertThat(accountRepository.get(account.number()).get()).isEqualTo(anotherAccount);
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldUpdateAccountAndItsNumber() {
    // given
    TestObserver testObserver = new TestObserver();
    Account account = createAccount();
    String newAccountNumber = UUID.randomUUID().toString();
    Account anotherAccount = createAnotherAccount(newAccountNumber);
    accountRepository.create(account).subscribe();

    // when
    accountRepository.update(account.number(), anotherAccount).subscribe(testObserver);

    // then
    testObserver.assertComplete();
    assertThat(accountRepository.get(newAccountNumber).get()).isEqualTo(anotherAccount);
    assertThat(accountRepository.get(account.number()).isPresent()).isFalse();
  }

  @Test
  public void shouldNotUpdateAccountIfItDoesNotExist() {
    // given
    TestObserver testObserver = new TestObserver();
    Account account = createAccount();

    // when
    accountRepository.update(account.number(), account).subscribe(testObserver);

    // then
    testObserver.assertError(AccountNotExistsException.class);
    testObserver.assertErrorMessage(new AccountNotExistsException(account.number()).getMessage());
  }

  @Test
  public void shouldDeleteAccount() {
    // given
    TestObserver testObserver = new TestObserver();
    Account account = createAccount();
    accountRepository.create(account).subscribe();

    // when
    accountRepository.delete(account.number()).subscribe(testObserver);

    // then
    testObserver.assertComplete();
    assertThat(accountRepository.get(account.number()).isPresent()).isFalse();
  }

  @Test
  public void shouldNotDeleteAccountIfItDoesNotExist() {
    // given
    TestObserver testObserver = new TestObserver();
    String numberWhichDoesNotExist = "numberWhichDoesNotExist";

    // when
    accountRepository.delete(numberWhichDoesNotExist).subscribe(testObserver);

    // then
    testObserver.assertError(AccountNotExistsException.class);
    testObserver.assertErrorMessage(
        new AccountNotExistsException(numberWhichDoesNotExist).getMessage()
    );
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