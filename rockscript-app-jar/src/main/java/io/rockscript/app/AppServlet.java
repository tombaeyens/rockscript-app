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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import java.util.Map;

public class AppServlet extends Servlet {

  public AppServlet() {
  }

  public AppServlet(Engine engine) {
    super(engine);
  }

  @Override
  protected Engine createEngine(Map<String, String> configuration) {
    return new AppEngine(configuration);
  }
}
