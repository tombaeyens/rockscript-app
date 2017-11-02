package io.rockscript.app.cqrs.commands;

import io.rockscript.api.Response;

public class CreateScriptResponse implements Response {

  @Override
  public int getStatus() {
    return 200;
  }
}
