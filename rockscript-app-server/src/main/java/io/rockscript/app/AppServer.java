/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 */
package io.rockscript.app;

import io.rockscript.Engine;
import io.rockscript.Server;
import io.rockscript.TestEngine;
import io.rockscript.http.server.HttpServer;

public class AppServer extends Server {

  @Override
  protected TestEngine createEngine() {
    return new TestEngine();
  }

  @Override
  protected HttpServer createHttpServer(Engine engine) {
    return new HttpServer(DEFAULT_ROCKSCRIPT_PORT)
      .servlet(new AppServlet(engine))
      // .filter(new Authentication())
      ;
  }

  public static void main(String[] args) {
    runServerTillCtrlC(new AppServer(), args);
  }
}
