/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 */
package io.rockscript.app;

import io.rockscript.Engine;
import io.rockscript.FileHandler;
import io.rockscript.http.servlet.ServerRequest;
import io.rockscript.http.servlet.ServerResponse;
import io.rockscript.util.Io;

public class AppFileHandler extends FileHandler {

  @Override
  public boolean matches(ServerRequest request) {
    return true;
  }

  @Override
  protected boolean redirect(ServerRequest request, ServerResponse response) {
    return false;
  }

  @Override
  protected String getResource(ServerRequest request) {
    String path = request.getPathInfo();
    if (path.endsWith("/")) {
      path += "index.html";
    }
    String resource = "http"+path;
    if (Io.hasResource(resource)) {
      return resource;
    }
    return "http/index.html";
  }
}
