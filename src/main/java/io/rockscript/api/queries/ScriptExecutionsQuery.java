/*
 * Copyright ©2017, RockScript.io. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.rockscript.api.queries;

import com.google.gson.Gson;
import io.rockscript.engine.Configuration;
import io.rockscript.api.model.ScriptVersion;
import io.rockscript.engine.impl.*;
import io.rockscript.netty.router.*;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/** Handles GET requests for /scripts */
@Get("/scriptExecutions")
public class ScriptExecutionsQuery implements RequestHandler {

  public static class ScriptExecution {
    public String id;
    public String scriptShortName;
    public String scriptName;
    public Integer scriptVersion;
    public Instant start;
    public Instant end;
    public ScriptExecution(){
    }
    public ScriptExecution(String scriptExecutionId) {
      this.id = scriptExecutionId;
    }
  }

  public static class ScriptExecutionList {
    private final Map<String, ScriptVersion> scriptVersionsById;
    Map<String,ScriptExecution> scriptExecutions = new LinkedHashMap<>();

    public ScriptExecutionList(Map<String, ScriptVersion> scriptVersionsById) {
      this.scriptVersionsById = scriptVersionsById;
    }

    public void processEvent(Event event) {
      ExecutionEvent executionEvent = (ExecutionEvent) event;
      String scriptExecutionId = executionEvent.getScriptExecutionId();
      ScriptExecution scriptExecution = scriptExecutions
        .computeIfAbsent(scriptExecutionId, se -> new ScriptExecution(scriptExecutionId));

      if (executionEvent instanceof ScriptStartedEvent) {
        scriptExecution.start = executionEvent.getTime();
        ScriptStartedEvent scriptStartedEvent = (ScriptStartedEvent) executionEvent;
        String scriptVersionId = scriptStartedEvent.getScriptVersionId();
        ScriptVersion scriptVersion = scriptVersionsById.get(scriptVersionId);
        scriptExecution.scriptName = scriptVersion.getName();
        scriptExecution.scriptShortName = getScriptShortName(scriptVersion.getName());
        scriptExecution.scriptVersion = scriptVersion.getVersion();

      } else if (executionEvent instanceof ScriptEndedEvent) {
        scriptExecution.end = executionEvent.getTime();
      }
    }
    private static String getScriptShortName(String name) {
      int lastSlashIndex = name.lastIndexOf('/');
      if (lastSlashIndex>=0 && name.length()>lastSlashIndex+1) {
        return name.substring(lastSlashIndex+1);
      }
      return name;
    }
  }

  @Override
  public void handle(AsyncHttpRequest request, AsyncHttpResponse response, Context context) {
    Configuration configuration = context.get(Configuration.class);
    Gson gson = configuration.getGson();

    Map<String, ScriptVersion> scriptVersionsById = new HashMap<>();
    configuration
      .getScriptStore()
      .getScripts()
      .forEach(script->script
        .getScriptVersions()
        .forEach(scriptVersion->scriptVersionsById.put(scriptVersion.getId(), scriptVersion)));

    ScriptExecutionList list = new ScriptExecutionList(scriptVersionsById);
    EventStore eventStore = configuration.getEventStore();
    eventStore
      .getEvents()
      .forEach(list::processEvent);

    Collection<ScriptExecution> scriptExecutions = list.scriptExecutions.values();
    response
      .bodyString(gson.toJson(scriptExecutions))
      .headerContentTypeApplicationJson()
      .status(200)
      .send();
  }
}
