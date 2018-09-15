package com.pwittchen.money.transfer.api;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {

  private static final Logger LOG = LoggerFactory.getLogger(App.class);
  private static final int PORT = 8000;

  public static void main(String args[]) {
    Javalin app = Javalin.create().start(PORT);
    app.get("/", context -> context.result("server is running"));
    LOG.info("server is running");
  }
}
