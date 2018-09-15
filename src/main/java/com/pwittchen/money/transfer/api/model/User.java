package com.pwittchen.money.transfer.api.model;

public final class User {
  private final String id;
  private final String name;
  private final String surname;

  private User() {
    this(builder());
  }

  private User(final Builder builder) {
    this(builder.id, builder.name, builder.surname);
  }

  private User(final String id, final String name, final String surname) {
    this.id = id;
    this.name = name;
    this.surname = surname;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String id() {
    return id;
  }

  public static Builder id(final String id) {
    return builder().id(id);
  }

  public String name() {
    return name;
  }

  public static Builder name(final String name) {
    return builder().name(name);
  }

  public String surname() {
    return surname;
  }

  public static Builder surname(final String surname) {
    return builder().surname(surname);
  }

  public static class Builder {
    private String id;
    private String name;
    private String surname;

    private Builder() {
    }

    public Builder id(final String id) {
      this.id = id;
      return this;
    }

    public Builder name(final String name) {
      this.name = name;
      return this;
    }

    public Builder surname(final String surname) {
      this.surname = surname;
      return this;
    }

    public User build() {
      return new User(this);
    }
  }
}
