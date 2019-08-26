package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
import com.pwittchen.money.transfer.api.model.User;
import com.pwittchen.money.transfer.api.repository.AccountRepository;
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
import org.mockito.junit.MockitoJUnitRunner;

import static com.google.common.truth.Truth.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryAccountRepositoryTest {

  private AccountRepository accountRepository;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() {
    accountRepository = new InMemoryAccountRepository();
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
    List<Account> accounts = accountRepository.getAll();

    // then
    assertThat(accounts.size()).isEqualTo(2);
  }

  @Test
  @SuppressWarnings("OptionalGetWithoutIsPresent") // in this test, check is not needed
  public void shouldCreateNewAccount() throws Exception {
    // given
    Account account = createAccount();

    // when
    accountRepository.create(account);
    Account createdAccount = accountRepository.get(account.number()).get();

    // then
    assertThat(accountRepository.getAll().isEmpty()).isFalse();
    assertThat(createdAccount).isEqualTo(account);
    assertThat(createdAccount.user()).isEqualTo(account.user());
    assertThat(createdAccount.user().id()).isEqualTo(account.user().id());
    assertThat(createdAccount.user().name()).isEqualTo(account.user().name());
    assertThat(createdAccount.user().surname()).isEqualTo(account.user().surname());
    assertThat(createdAccount.number()).isEqualTo(account.number());
    assertThat(createdAccount.money()).isEqualTo(account.money());
  }

  @Test
  public void shouldWithdrawMoney() throws Exception {
    // given
    Account sender = createAccount();
    Account receiver = createAnotherAccount("anotherNumber");
    Money moneyToWithdraw = Money.of(CurrencyUnit.EUR, 1);
    Money expectedAmount = sender.money().minus(moneyToWithdraw);

    // when
    accountRepository.create(sender);
    accountRepository.create(receiver);
    accountRepository.transfer(sender, receiver, moneyToWithdraw);

    // then
    //noinspection OptionalGetWithoutIsPresent
    Money actualAmount = accountRepository.get(sender.number()).get().money();
    assertThat(actualAmount).isEqualTo(expectedAmount);
  }

  @Test
  public void shouldPutMoney() throws Exception {
    // given
    Account sender = createAccount();
    Account receiver = createAnotherAccount("anotherNumber");
    Money moneyToPut = Money.of(CurrencyUnit.EUR, 1);
    Money expectedAmount = receiver.money().plus(moneyToPut);

    // when
    accountRepository.create(sender);
    accountRepository.create(receiver);
    accountRepository.transfer(sender, receiver, moneyToPut);

    // then
    //noinspection OptionalGetWithoutIsPresent
    Money actualAmount = accountRepository.get(receiver.number()).get().money();
    assertThat(actualAmount).isEqualTo(expectedAmount);
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
  public void shouldClearAccounts() throws Exception {
    // given
    accountRepository.create(createAccount());
    accountRepository.create(createAccount());

    assertThat(accountRepository.getAll().size()).isEqualTo(2);

    // when
    accountRepository.clear();

    // then
    assertThat(accountRepository.getAll().isEmpty()).isTrue();
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
        .money(Money.of(CurrencyUnit.EUR, 5))
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