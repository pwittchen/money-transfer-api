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
    Response responseOne = Response.builder().message("test").object("obj").build();
    Response responseTwo = Response.builder().message("test").object("obj").build();

    // then
    assertThat(responseOne.message()).isEqualTo(responseTwo.message());
    assertThat(responseOne.object()).isEqualTo(responseTwo.object());
    assertThat(responseOne.equals(responseTwo)).isTrue();
  }

  @Test
  public void objectsShouldBeEqualWhenTheyAreTheSameInstance() {
    // when
    Response response = Response.builder().message("test").object(new Object()).build();

    // then
    assertThat(response.equals(response)).isTrue();
  }

  @Test
  public void objectsShouldNotBeEqualWhenOneIsNull() {
    // when
    Response response = Response.builder().message("test").object(new Object()).build();

    // then
    assertThat(response.equals(null)).isFalse();
  }

  @Test
  public void objectsShouldNotBeEqualWhenOneHasDifferentType() {
    // when
    Response response = Response.builder().message("test").object(new Object()).build();

    // then
    assertThat(response.equals(new Object())).isFalse();
  }


  @Test
  public void objectsShouldBeInTheSameBucket() {
    // when
    Response responseOne = Response.builder().message("test").object("obj").build();
    Response responseTwo = Response.builder().message("test").object("obj").build();

    // then
    assertThat(responseOne.hashCode() == responseTwo.hashCode()).isTrue();
  }

  @Test
  public void objectsShouldNotBeEqualWhenMessageIsDifferent() {
    // when
    Response responseOne = Response.builder().message("test1").object("obj").build();
    Response responseTwo = Response.builder().message("test2").object("obj").build();

    // then
    assertThat(responseOne.equals(responseTwo)).isFalse();
  }

  @Test
  public void objectsShouldNotBeEqualWhenObjectIsDifferent() {
    // when
    Response responseOne = Response.builder().message("test").object("obj1").build();
    Response responseTwo = Response.builder().message("test").object("obj2").build();

    // then
    assertThat(responseOne.equals(responseTwo)).isFalse();
  }
}