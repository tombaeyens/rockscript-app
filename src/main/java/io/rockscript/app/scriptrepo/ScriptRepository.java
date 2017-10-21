package io.rockscript.app.scriptrepo;


import io.rockscript.engine.Configuration;
import io.rockscript.engine.Script;
import io.rockscript.engine.impl.ScriptStore;

import java.util.*;

public class ScriptRepository {

  Map<String,Script> undeployedScripts = new HashMap<>();
  ScriptStore scriptStore;

  public ScriptRepository(Configuration configuration) {
    this.scriptStore = configuration.getScriptStore();
  }

  public List<Script> getLatestScriptTexts() {
    Map<String,Script> scriptsByName = new TreeMap<>(new Comparator<String>() {
      @Override
      public int compare(String o1, String o2) {
        return o1.compareTo(o2);
      }
    });
    Map<String, List<Script>> scriptStoreScripts = scriptStore.getScripts();
    if (scriptStoreScripts!=null) {
      for (String scriptName: scriptStoreScripts.keySet()) {
        List<Script> scriptVersions = scriptStoreScripts.get(scriptName);
        if (!scriptVersions.isEmpty()) {
          Script latestVersion = scriptVersions.get(scriptVersions.size()-1);
          scriptsByName.put(scriptName, latestVersion);
        }
      }
    }
    scriptsByName.putAll(undeployedScripts);
    return new ArrayList<>(scriptsByName.values());
  }

  public void saveScriptText(String scriptName, String scriptText) {
    Script script = new Script();
    script.setName(scriptName);
    script.setText(scriptText);
    undeployedScripts.put(scriptName, script);
  }
}
