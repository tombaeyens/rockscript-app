package io.rockscript.api.queries;

import io.rockscript.app.Authentication;
import io.rockscript.netty.router.AsyncHttpServerConfiguration;
import io.rockscript.netty.router.AsyncHttpServerModule;

public class AppRequestHandlersModule implements AsyncHttpServerModule {

  @Override
  public void register(AsyncHttpServerConfiguration configuration) {
    configuration
      .interceptor(new Authentication())
      .scan(ScriptExecutionQuery.class)
      .scan(ScriptExecutionsQuery.class)
      .scan(ScriptsQuery.class)
      .notFound(FileHandler.class);
  }
}
