/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 */
package io.rockscript.app;

import io.rockscript.Engine;
import io.rockscript.Server;
import io.rockscript.test.TestEngine;
import io.rockscript.http.server.HttpServer;

public class AppServer extends Server {

  public AppServer(String[] args) {
    super(args);
  }

  @Override
  protected Engine createEngine(String[] args) {
    return new AppEngine(args);
  }

  @Override
  public HttpServer createHttpServer(Engine engine) {
    AppServlet appServlet = createAppServlet(engine);
    return createHttpServer(appServlet, DEFAULT_ROCKSCRIPT_PORT);
  }

  public static AppServlet createAppServlet(Engine engine) {
    return new AppServlet(engine);
  }

  public static HttpServer createHttpServer(AppServlet appServlet, int port) {
    return new HttpServer(port)
      .servlet(appServlet)
      // .filter(new Authentication())
      ;
  }

  public static HttpServer createHttpServer(Engine engine, int port) {
    return createHttpServer(createAppServlet(engine), port);
  }

  public static void main(String[] args) {
    runServer(new AppServer(args));
  }
}
