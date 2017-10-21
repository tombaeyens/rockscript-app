package io.rockscript.app.cqrs.queries;

import io.rockscript.netty.router.AsyncHttpRequest;
import io.rockscript.netty.router.AsyncHttpResponse;
import io.rockscript.netty.router.Context;
import io.rockscript.netty.router.RequestHandler;

public class FileHandler implements RequestHandler {

  @Override
  public void handle(AsyncHttpRequest request, AsyncHttpResponse response, Context context) {
    response.bodyString("Not implemented yet");
    response.statusInternalServerError();
    response.send();
  }
}
