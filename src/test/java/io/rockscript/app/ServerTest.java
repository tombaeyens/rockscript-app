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
package io.rockscript.app;

import com.google.gson.reflect.TypeToken;
import io.rockscript.api.queries.ScriptExecutionQuery;
import io.rockscript.api.queries.ScriptExecutionsQuery;
import io.rockscript.api.commands.*;
import io.rockscript.api.model.ScriptVersion;
import io.rockscript.engine.impl.ContinuationReference;
import io.rockscript.test.AbstractServerTest;
import io.rockscript.test.SimpleImportProvider;
import io.rockscript.util.Io;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ServerTest extends AbstractServerTest {

  @Override
  public void setUp() {
    super.setUp();
    SimpleImportProvider.setUp();
  }

  @Test
  public void testWebFiles() {
    System.out.println(newGet("/")
      .execute()
      .assertStatusOk()
      .getBodyAsString());
  }

  @Test
  public void testGetScripts() {
    String script1Version1 = deployShortTestScript(
      "script one",
      Io.getResourceAsString("testscripts/short-script.rs"));

    String script1Version2 = deployShortTestScript(
      "script one",
      Io.getResourceAsString("testscripts/short-script.rs"));

    String script2Version1 = deployShortTestScript(
      "script two",
      Io.getResourceAsString("testscripts/short-script.rs"));

    List<ScriptVersion> scriptVersions = newGet("/scripts")
      .execute()
      .assertStatusOk()
      .getBodyAs(new TypeToken<List<ScriptVersion>>() {}.getType());

    assertEquals(2, scriptVersions.size());
  }

  @Test
  public void testGetScriptExecutions() {
    String scriptId = deployShortTestScript(
      "sn",
      Io.getResourceAsString("testscripts/short-script.rs"));

    assertNotNull(scriptId);

    EngineStartScriptExecutionResponse startScriptResponse = new StartScriptExecutionCommand()
        .scriptVersionId(scriptId)
        .execute(getConfiguration());

    String scriptExecutionId = startScriptResponse.getScriptExecutionId();

    ContinuationReference continuationReference = SimpleImportProvider.removeFirstContinuationReference(scriptExecutionId);

    newPost("command")
      .bodyObject(new EndServiceFunctionCommand()
        .continuationReference(continuationReference))
      .execute()
      .assertStatusOk()
      .getBodyAs(ScriptExecutionResponse.class);

    newPost("command")
      .bodyObject(new StartScriptExecutionCommand()
        .scriptVersionId(scriptId))
      .execute()
      .assertStatusOk()
      .getBodyAs(EngineStartScriptExecutionResponse.class);

    List<ScriptExecutionsQuery.ScriptExecution> scriptExecutions = newGet("scriptExecutions")
      .execute()
      .assertStatusOk()
      .getBodyAs(new TypeToken<List<ScriptExecutionsQuery.ScriptExecution>>() {}.getType());

    ScriptExecutionsQuery.ScriptExecution scriptExecution = scriptExecutions.get(0);
    assertEquals("se1", scriptExecution.id);
    assertEquals("sn", scriptExecution.scriptName);
    assertEquals("sn", scriptExecution.scriptShortName);
    assertEquals(1, (int)scriptExecution.scriptVersion);
    assertNotNull(scriptExecution.start);
    assertNotNull(scriptExecution.end);

    scriptExecution = scriptExecutions.get(1);
    assertEquals("se2", scriptExecution.id);
    assertEquals("sn", scriptExecution.scriptName);
    assertEquals("sn", scriptExecution.scriptShortName);
    assertEquals(1, (int)scriptExecution.scriptVersion);
    assertNotNull(scriptExecution.start);
    assertNull(scriptExecution.end);

    Object body = newGet("scriptExecution/se1")
      .execute()
      .assertStatusOk()
      .getBody();

    ScriptExecutionQuery.ScriptExecution scriptExecutionDetails = newGet("scriptExecution/se1")
      .execute()
      .assertStatusOk()
      .getBodyAs(new TypeToken<ScriptExecutionQuery.ScriptExecution>() {}.getType());

    assertTrue(scriptExecutionDetails.scriptText.contains("\n"));
  }

  private String deployShortTestScript(String scriptName, String scriptText) {
    assertNotNull(scriptText);
    return newPost("command")
        .bodyObject(new DeployScriptVersionCommand()
          .scriptName(scriptName)
          .scriptText(scriptText))
        .execute()
        .assertStatusOk()
        .getBodyAs(ScriptVersion.class)
        .getId();
  }

}