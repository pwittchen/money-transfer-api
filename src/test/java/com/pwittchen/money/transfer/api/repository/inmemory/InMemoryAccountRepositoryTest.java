package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
import com.pwittchen.money.transfer.api.validation.AccountValidation;
import com.pwittchen.money.transfer.api.validation.exception.AccountAlreadyExistsException;
import com.pwittchen.money.transfer.api.validation.exception.AccountNotExistsException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyAccountNumberException;
import com.pwittchen.money.transfer.api.validation.exception.EmptyUserIdException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryAccountRepositoryTest {

  private AccountRepository accountRepository;

  @Mock
  private AccountValidation accountValidation;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    accountRepository = new InMemoryAccountRepository(accountValidation);
    accountRepository.clear();
  }

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
  public void shouldGetAccountWhenItExists() throws Exception {
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
  public void shouldGetAllAccounts() throws Exception {
    // given
    accountRepository.create(createAccount());
    accountRepository.create(createAccount());

    // when
    List<Account> accounts = accountRepository.get();

    // then
    assertThat(accounts.size()).isEqualTo(2);
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldCreateNewAccount() throws Exception {
    // given
    Account account = createAccount();
    when(accountValidation.validate(account)).thenReturn(Optional.empty());

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
  public void shouldNotCreateNewAccountWithNumberWhichAlreadyExists() throws Exception {
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
  public void shouldNotCreateAccountIfItErrorOccurred() throws Exception {
    // given
    Account account = createAccount();
    when(accountValidation.validate(account)).thenReturn(Optional.of(new EmptyUserIdException()));

    // then
    expectedException.expect(EmptyUserIdException.class);
    expectedException.expectMessage(
        new EmptyUserIdException().getMessage()
    );

    // then
    accountRepository.create(account);
  }

  @Test
  public void shouldWithdrawMoney() throws Exception {
    // given
    Account account = createAccount();
    Money moneyToWithdraw = Money.of(CurrencyUnit.EUR, 1);
    Money expectedAmount = account.money().minus(moneyToWithdraw);

    // when
    accountRepository.create(account);
    accountRepository.withdrawMoney(account, moneyToWithdraw);

    // then
    //noinspection OptionalGetWithoutIsPresent
    Money actualAmount = accountRepository.get(account.number()).get().money();
    assertThat(actualAmount).isEqualTo(expectedAmount);
  }

  @Test(expected = AccountNotExistsException.class)
  public void shouldNotWithdrawMoneyIfAccountDoesNotExist() {
    // given
    Account account = createAccount();
    Money money = Money.of(CurrencyUnit.EUR, 1);

    // when
    accountRepository.withdrawMoney(account, money);

    // then exception is thrown
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNotWithdrawMoneyIfErrorOccurred() throws Exception {
    // given
    Account account = createAccount();
    Money money = Money.of(CurrencyUnit.EUR, 1);
    when(accountValidation.validate(account)).thenReturn(
        Optional.of(new EmptyAccountNumberException())
    );

    // when
    accountRepository.create(account);
    accountRepository.withdrawMoney(account, money);

    // then exception is thrown
  }

  @Test
  public void shouldPutMoney() throws Exception {
    // given
    Account account = createAccount();
    Money moneyToPut = Money.of(CurrencyUnit.EUR, 1);
    Money expectedAmount = account.money().plus(moneyToPut);

    // when
    accountRepository.create(account);
    accountRepository.putMoney(account, moneyToPut);

    // then
    //noinspection OptionalGetWithoutIsPresent
    Money actualAmount = accountRepository.get(account.number()).get().money();
    assertThat(actualAmount).isEqualTo(expectedAmount);
  }

  @Test(expected = AccountNotExistsException.class)
  public void shouldNotPutMoneyIfAccountDoesNotExist() {
    // given
    Account account = createAccount();
    Money money = Money.of(CurrencyUnit.EUR, 1);

    // when
    accountRepository.putMoney(account, money);

    // then exception is thrown
  }

  @Test(expected = EmptyAccountNumberException.class)
  public void shouldNotPutMoneyIfErrorOccurred() throws Exception {
    // given
    Account account = createAccount();
    Money money = Money.of(CurrencyUnit.EUR, 1);
    when(accountValidation.validate(account)).thenReturn(
        Optional.of(new EmptyAccountNumberException())
    );

    // when
    accountRepository.create(account);
    accountRepository.putMoney(account, money);

    // then exception is thrown
  }

  @Test
  public void shouldDeleteAccount() throws Exception {
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

  @Test
  public void shouldNotDeleteAccountIfItIsEmpty() {
    // given
    String emptyNumber = "";

    // when
    expectedException.expect(EmptyAccountNumberException.class);
    expectedException.expectMessage(
        new EmptyAccountNumberException().getMessage()
    );

    // then
    accountRepository.delete(emptyNumber);
  }

  @Test
  public void shouldClearAccounts() throws Exception {
    // given
    accountRepository.create(createAccount());
    accountRepository.create(createAccount());

    assertThat(accountRepository.get().size()).isEqualTo(2);

    // when
    accountRepository.clear();

    // then
    assertThat(accountRepository.get().isEmpty()).isTrue();
  }

  private Account createAccount() {
    return Account
        .builder()
        .user(createUser())
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 0))
        .createdAt(LocalDateTime.now())
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
        .createdAt(LocalDateTime.now())
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