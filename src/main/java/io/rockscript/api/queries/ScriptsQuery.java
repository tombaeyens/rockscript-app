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

import io.rockscript.app.scriptrepo.ScriptRepository;
import io.rockscript.engine.Configuration;
import io.rockscript.api.model.ScriptVersion;
import io.rockscript.netty.router.*;

import java.util.List;

/** Handles GET requests for /scripts */
@Get("/scripts")
public class ScriptsQuery implements RequestHandler {

  @Override
  public void handle(AsyncHttpRequest request, AsyncHttpResponse response, Context context) {
    Configuration configuration = context.get(Configuration.class);
    ScriptRepository scriptRepository = configuration.getObject(ScriptRepository.class);
    List<ScriptVersion> latestScriptVersions = scriptRepository.getLatestScriptTexts();
    response
      .bodyJson(latestScriptVersions)
      .headerContentTypeApplicationJson()
      .status(200)
      .send();
  }
}
