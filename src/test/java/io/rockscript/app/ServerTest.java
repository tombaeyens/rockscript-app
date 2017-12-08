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
import io.rockscript.Servlet;
import io.rockscript.api.commands.DeployScriptVersionCommand;
import io.rockscript.api.commands.EndServiceFunctionCommand;
import io.rockscript.api.commands.ScriptExecutionResponse;
import io.rockscript.api.commands.StartScriptExecutionCommand;
import io.rockscript.api.model.ScriptVersion;
import io.rockscript.api.queries.ScriptExecutionQuery;
import io.rockscript.api.queries.ScriptExecutionsQuery;
import io.rockscript.engine.impl.ContinuationReference;
import io.rockscript.http.server.HttpServer;
import io.rockscript.test.LatestServerExceptionListener;
import io.rockscript.test.SimpleImportProvider;
import io.rockscript.test.server.AbstractServerTest;
import io.rockscript.util.Io;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ServerTest extends AbstractServerTest {

  @Override
  protected HttpServer createHttpServer() {
    // engine is initialized above in the setUp
    assertNotNull(engine);

    Servlet servlet = new AppServlet(engine);
    servlet.exceptionListener(new LatestServerExceptionListener());

    return new HttpServer(PORT)
      .servlet(servlet)
      // .filter(new Authentication())
      ;
  }

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
      .getBody());
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

    ScriptExecutionResponse startScriptResponse = new StartScriptExecutionCommand()
      .scriptVersionId(scriptId)
      .execute(engine);

    String scriptExecutionId = startScriptResponse.getScriptExecutionId();

    ContinuationReference continuationReference = SimpleImportProvider.removeFirstContinuationReference(scriptExecutionId);

    newPost("/command")
      .bodyJson(new EndServiceFunctionCommand()
        .continuationReference(continuationReference))
      .execute()
      .assertStatusOk()
      .getBodyAs(ScriptExecutionResponse.class);

    newPost("/command")
      .bodyJson(new StartScriptExecutionCommand()
        .scriptVersionId(scriptId))
      .execute()
      .assertStatusOk()
      .getBodyAs(ScriptExecutionResponse.class);

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

    Object body = newGet("/scriptExecution/se1")
      .execute()
      .assertStatusOk()
      .getBody();

    ScriptExecutionQuery.ScriptExecutionDetails scriptExecutionDetails = newGet("scriptExecution/se1")
      .execute()
      .assertStatusOk()
      .getBodyAs(ScriptExecutionQuery.ScriptExecutionDetails.class);

    assertTrue(scriptExecutionDetails.getScriptVersion().getScriptName().contains("\n"));
  }

  private String deployShortTestScript(String scriptName, String scriptText) {
    assertNotNull(scriptText);
    return newPost("/command")
        .bodyJson(new DeployScriptVersionCommand()
          .scriptName(scriptName)
          .scriptText(scriptText))
        .execute()
        .assertStatusOk()
        .getBodyAs(ScriptVersion.class)
        .getId();
  }

}