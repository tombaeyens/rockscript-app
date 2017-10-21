package io.rockscript.app.cqrs.queries;

import io.rockscript.netty.router.AsyncHttpServerConfiguration;
import io.rockscript.netty.router.AsyncHttpServerModule;

public class AppRequestHandlersModule implements AsyncHttpServerModule {

  @Override
  public void register(AsyncHttpServerConfiguration configuration) {
    configuration
      .scan(ScriptExecutionQuery.class)
      .scan(ScriptExecutionsQuery.class)
      .scan(ScriptsQuery.class)
      .notFound(FileHandler.class);
  }
}
