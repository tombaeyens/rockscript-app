/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 */
package io.rockscript.app;

import io.rockscript.Engine;
import io.rockscript.FileHandler;
import io.rockscript.Servlet;
import io.rockscript.api.CommandHandler;
import io.rockscript.api.QueryHandler;
import io.rockscript.engine.PingHandler;

public class AppServlet extends Servlet {

  public AppServlet(Engine engine) {
    super(engine);
  }

  @Override
  protected void registerRequestHandlers() {
    requestHandler(new CommandHandler(engine));
    requestHandler(new QueryHandler(engine));
    requestHandler(new PingHandler(engine));
    requestHandler(new AppFileHandler(engine));
  }
}
