package com.pwittchen.money.transfer.api.model;

public final class User {
  private final String id;
  private final String name;
  private final String surname;
  private final String email;
  private final String address;

  public User(final String id, final String name, final String surname, final String email,
      final String address) {
    this.id = id;
    this.name = name;
    this.surname = surname;
    this.email = email;
    this.address = address;
  }

  public String id() {
    return id;
  }

  public String name() {
    return name;
  }

  public String surname() {
    return surname;
  }

  public String email() {
    return email;
  }

  public String address() {
    return address;
  }

  //TODO: add builder
}
