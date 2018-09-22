package com.pwittchen.money.transfer.api;

import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ApplicationTest {

  @Test
  public void shouldCreateApplicationObject() {
    // when
    Application application = new Application();

    // then
    assertThat(application).isNotNull();
  }

  @Test
  public void shouldStartApplication() {
    // given
    String[] args = {};

    // when
    Application.main(args);

    // then no exception is thrown
  }
}