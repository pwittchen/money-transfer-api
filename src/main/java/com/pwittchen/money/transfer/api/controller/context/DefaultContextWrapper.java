package com.pwittchen.money.transfer.api.controller.context;

import io.javalin.http.Context;

public class DefaultContextWrapper implements ContextWrapper {

  @Override public String formParam(Context context, String param) {
    return context.formParam(param);
  }

  @Override public void json(Context context, Object object) {
    context.json(object);
  }

  @Override public void json(Context context, Object object, int status) {
    context.status(status).json(object);
  }
}
