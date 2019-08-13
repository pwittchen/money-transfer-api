package com.pwittchen.money.transfer.api.controller.context;

import io.javalin.http.Context;

public interface ContextWrapper {
  String pathParam(Context context, String param);

  String formParam(Context context, String param);

  void json(Context context, Object object);

  void json(Context context, Object object, int status);
}
