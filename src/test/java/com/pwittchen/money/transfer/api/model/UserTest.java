package com.pwittchen.money.transfer.api.model;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class UserTest {

  @Test public void constructorShouldBePrivate()
      throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {

    // when
    Constructor<User> constructor = User.class.getDeclaredConstructor();

    // then
    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

    constructor.setAccessible(true);
    constructor.newInstance();
  }

  @Test public void objectsShouldBeEqual() {
    // when
    User userOne = createUser();
    User userTwo = createUser();

    // then
    assertThat(userOne.equals(userTwo)).isTrue();
  }

  @Test public void objectsShouldBeEqualWhenTheyAreTheSameInstance() {
    // when
    User user = createUser();

    // then
    assertThat(user.equals(user)).isTrue();
  }

  @Test public void objectsShouldNotBeEqualWhenOneIsNull() {
    // when
    User user = createUser();

    // then
    assertThat(user.equals(null)).isFalse();
  }

  @Test
  public void objectsShouldNotBeEqualWhenOneHasDifferentType() {
    // when
    User user = createUser();

    // then
    assertThat(user.equals(new Object())).isFalse();
  }

  @Test public void objectsShouldNotBeEqualWhenOneHasDifferentId() {
    // given
    User userOne = createUser();
    User userTwo = User
        .builder()
        .id("2  ")
        .name("John")
        .surname("Doe")
        .build();

    // when
    boolean isTheSame = userOne.equals(userTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectsShouldNotBeEqualWhenOneHasDifferentName() {
    // given
    User userOne = createUser();
    User userTwo = User
        .builder()
        .id("1")
        .name("Stephen")
        .surname("Doe")
        .build();

    // when
    boolean isTheSame = userOne.equals(userTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectsShouldNotBeEqualWhenOneHasDifferentSurname() {
    // given
    User userOne = createUser();
    User userTwo = User
        .builder()
        .id("1")
        .name("John")
        .surname("Test")
        .build();

    // when
    boolean isTheSame = userOne.equals(userTwo);

    // then
    assertThat(isTheSame).isFalse();
  }

  @Test public void objectsShouldBeInTheSameBucket() {
    // when
    User userOne = createUser();
    User userTwo = createUser();

    // then
    assertThat(userOne.hashCode() == userTwo.hashCode()).isTrue();
  }

  private User createUser() {
    return User
        .builder()
        .id("1")
        .name("John")
        .surname("Doe")
        .build();
  }
}