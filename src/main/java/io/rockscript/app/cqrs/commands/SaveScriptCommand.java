package io.rockscript.app.cqrs.commands;

import io.rockscript.app.scriptrepo.ScriptRepository;
import io.rockscript.api.Command;
import io.rockscript.engine.Configuration;
import io.rockscript.engine.Script;
import io.rockscript.netty.router.BadRequestException;

public class SaveScriptCommand extends Command<CreateScriptResponse> {

  protected Script script;

  @Override
  public CreateScriptResponse execute(Configuration configuration) {
    ScriptRepository scriptRepository = configuration.getObject(ScriptRepository.class);
    if (script==null) {
      if (script.getName()!=null) {
        scriptRepository.saveScriptText(script.getName(), script.getText());
      } else {
        throw new BadRequestException("script.name is required");
      }
    } else {
      throw new BadRequestException("script is required");
    }
    return null;
  }

  public Script getScript() {
    return script;
  }

  public void setScript(Script script) {
    this.script = script;
  }
}
