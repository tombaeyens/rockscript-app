package io.rockscript.app;

import io.rockscript.app.scriptrepo.ScriptRepository;
import io.rockscript.Engine;
import io.rockscript.engine.EngineModule;

public class AppModule implements EngineModule {

  @Override
  public void configured(Engine engine) {
    engine.object(new ScriptRepository(engine));
  }
}
