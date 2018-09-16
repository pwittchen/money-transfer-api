package com.pwittchen.money.transfer.api.model;

import java.util.Objects;

public class Response {
  private final String message;

  private Response() {
    this(builder());
  }

  private Response(Builder builder) {
    this(builder.message);
  }

  private Response(String message) {
    this.message = message;
  }

  public static Builder builder() {
    return new Builder();
  }

  public String message() {
    return message;
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Response response = (Response) o;
    return Objects.equals(message, response.message);
  }

  @Override public int hashCode() {
    return Objects.hash(message);
  }

  public static class Builder {
    private String message;

    private Builder() {
    }

    public Builder message(final String message) {
      this.message = message;
      return this;
    }

    public Response build() {
      return new Response(this);
    }
  }
}
