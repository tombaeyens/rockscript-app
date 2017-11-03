package io.rockscript.app.scriptrepo;


import io.rockscript.api.model.Script;
import io.rockscript.engine.Configuration;
import io.rockscript.api.model.ScriptVersion;
import io.rockscript.engine.impl.ScriptStore;

import java.util.*;

public class ScriptRepository {

  Map<String,ScriptVersion> undeployedScripts = new HashMap<>();
  ScriptStore scriptStore;

  public ScriptRepository(Configuration configuration) {
    this.scriptStore = configuration.getScriptStore();
  }

  public List<ScriptVersion> getLatestScriptTexts() {
    Map<String,ScriptVersion> scriptsByName = new TreeMap<>(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    });
    List<Script> scripts = scriptStore.getScripts();
    if (scripts!=null) {
      for (Script script: scripts) {
        List<ScriptVersion> scriptVersions = script.getScriptVersions();
        if (!scriptVersions.isEmpty()) {
          ScriptVersion latestVersion = scriptVersions.get(scriptVersions.size() - 1);
          scriptsByName.put(script.getName(), latestVersion);
        }
      }
    }
    scriptsByName.putAll(undeployedScripts);
    return new ArrayList<>(scriptsByName.values());
  }

  public void saveScriptText(String scriptName, String scriptText) {
    ScriptVersion scriptVersion = new ScriptVersion();
    scriptVersion.setName(scriptName);
    scriptVersion.setText(scriptText);
    undeployedScripts.put(scriptName, scriptVersion);
  }
}
