package com.pwittchen.money.transfer.api.validation.implementation;

import com.pwittchen.money.transfer.api.validation.AccountValidation;
import org.junit.Test;

public class DefaultAccountValidationTest {

  private AccountValidation accountValidation = new DefaultAccountValidation();

  @Test
  public void shouldNotCreateAnyErrorsIfAccountIsValid() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfAccountNumberIsNull() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfAccountNumberIsEmpty() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfUserIdIsNull() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfUserIdIsEmpty() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfUserNameIsNull() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfUserNameIsEmpty() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfUserSurnameIsNull() {
    //TODO: implement
  }

  @Test
  public void shouldCreateErrorIfUserSurnameIsEmpty() {
    //TODO: implement
  }
}