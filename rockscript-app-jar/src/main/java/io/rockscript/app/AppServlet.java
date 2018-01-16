/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 */
package io.rockscript.app;

import io.rockscript.Engine;
import io.rockscript.Servlet;

import java.util.Map;

public class AppServlet extends Servlet {

  public AppServlet() {
  }

  public AppServlet(Engine engine) {
    super(engine);
  }

  @Override
  protected Engine createEngine(Map<String, String> configuration) {
    return new AppConfiguration(configuration)
      .build();
  }
}
