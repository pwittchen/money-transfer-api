package com.pwittchen.money.transfer.api.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class ResponseTest {

  @Test
  public void constructorShouldBePrivate() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {

    // when
    Constructor<Response> constructor = Response.class.getDeclaredConstructor();

    // then
    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

    constructor.setAccessible(true);
    constructor.newInstance();
  }

  @Test
  public void objectsShouldBeEqual() {
    // when
    Response responseOne = Response.builder().message("test").build();
    Response responseTwo = Response.builder().message("test").build();

    // then
    assertThat(responseOne.message()).isEqualTo(responseTwo.message());
    assertThat(responseOne.equals(responseTwo)).isTrue();
  }

  @Test
  public void objectsShouldBeEqualWhenTheyAreTheSameInstance() {
    // when
    Response response = Response.builder().message("test").build();

    // then
    assertThat(response.equals(response)).isTrue();
  }

  @Test
  public void objectsShouldNotBeEqualWhenOneIsNull() {
    // when
    Response response = Response.builder().message("test").build();

    // then
    assertThat(response.equals(null)).isFalse();
  }

  @Test
  public void objectsShouldBeInTheSameBucket() {
    // when
    Response responseOne = Response.builder().message("test").build();
    Response responseTwo = Response.builder().message("test").build();

    // then
    assertThat(responseOne.hashCode() == responseTwo.hashCode()).isTrue();
  }
}