package com.pwittchen.money.transfer.api.repository.inmemory;

import com.pwittchen.money.transfer.api.model.Account;
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

  @Rule public ExpectedException expectedException = ExpectedException.none();

  @Before public void setUp() {
    accountRepository = new InMemoryAccountRepository();
  }

  @Test public void shouldGetEmptyResultWhenAccountDoesNotExist() {
    // given
    String invalidNumber = "invalidNumber";

    // when
    Optional<Account> optional = accountRepository.get(invalidNumber);

    // then
    assertThat(optional.isPresent()).isFalse();
  }

  @Test public void shouldGetAccountWhenItExists() {
    // given
    Account account = createAccount();
    String accountNumber = account.number();
    accountRepository.create(account);

    // when
    Optional<Account> optional = accountRepository.get(accountNumber);

    // then
    assertThat(optional.isPresent()).isTrue();
  }

  @Test public void shouldGetAllAccounts() {
    // given
    accountRepository.create(createAccount());
    accountRepository.create(createAccount());

    // when
    List<Account> accounts = accountRepository.getAll();

    // then
    assertThat(accounts.size()).isEqualTo(2);
  }

  @Test public void shouldCreateNewAccount() {
    // given
    Account account = createAccount();

    // when
    accountRepository.create(account);
    //noinspection OptionalGetWithoutIsPresent
    Account createdAccount = accountRepository.get(account.number()).get();

    // then
    assertThat(accountRepository.getAll().isEmpty()).isFalse();
    assertThat(createdAccount).isEqualTo(account);
    assertThat(createdAccount.owner()).isEqualTo(account.owner());
    assertThat(createdAccount.number()).isEqualTo(account.number());
    assertThat(createdAccount.money()).isEqualTo(account.money());
  }

  @Test public void shouldWithdrawMoney() {
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

  @Test public void shouldDepositMoney() {
    // given
    Account sender = createAccount();
    Account receiver = createAnotherAccount("anotherNumber");
    Money moneyToDeposit = Money.of(CurrencyUnit.EUR, 1);
    Money expectedAmount = receiver.money().plus(moneyToDeposit);

    // when
    accountRepository.create(sender);
    accountRepository.create(receiver);
    accountRepository.transfer(sender, receiver, moneyToDeposit);

    // then
    //noinspection OptionalGetWithoutIsPresent
    Money actualAmount = accountRepository.get(receiver.number()).get().money();
    assertThat(actualAmount).isEqualTo(expectedAmount);
  }

  private Account createAccount() {
    return Account
        .builder()
        .owner("testOwner")
        .number(UUID.randomUUID().toString())
        .money(Money.of(CurrencyUnit.EUR, 0))
        .createdAt(LocalDateTime.now())
        .build();
  }

  private Account createAnotherAccount(String number) {
    return Account
        .builder()
        .owner("anotherOwner")
        .number(number)
        .money(Money.of(CurrencyUnit.EUR, 5))
        .createdAt(LocalDateTime.now())
        .build();
  }
}