package io.rockscript.app;

import io.rockscript.Engine;
import io.rockscript.EnginePlugin;
import io.rockscript.app.scriptrepo.ScriptRepository;

public class AppPlugin implements EnginePlugin {

  @Override
  public void created(Engine engine) {
    engine.object(new ScriptRepository(engine));
  }

  @Override
  public void start(Engine engine) {

  }

  @Override
  public void stop(Engine engine) {

  }
}
