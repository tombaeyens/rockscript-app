package io.rockscript.app;

import io.rockscript.app.scriptrepo.ScriptRepository;
import io.rockscript.engine.Configuration;
import io.rockscript.engine.EngineModule;

public class AppModule implements EngineModule {

  @Override
  public void configured(Configuration configuration) {
    configuration.object(new ScriptRepository(configuration));
  }
}
