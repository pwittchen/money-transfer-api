package com.pwittchen.money.transfer.api.model;

import java.util.Objects;

public class Response {
  private final String message;
  private final Object object;

  private Response() {
    this(builder());
  }

  private Response(Builder builder) {
    this(builder.message, builder.object);
  }

  private Response(final String message, final Object object) {
    this.message = message;
    this.object = object;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String message() {
    return message;
  }

  public Object object() {
    return object;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Response response = (Response) o;

    return Objects.equals(message, response.message)
        && Objects.equals(object, response.object);
  }

  @Override public int hashCode() {
    return Objects.hash(message, object);
  }

  public static class Builder {
    private String message;
    private Object object;

    private Builder() {
    }

    public Builder message(final String message) {
      this.message = message;
      return this;
    }

    public Builder object(final Object object) {
      this.object = object;
      return this;
    }

    public Response build() {
      return new Response(this);
    }
  }
}
