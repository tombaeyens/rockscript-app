/*
 * Copyright (c) 2017 RockScript.io.
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.rockscript.app;

import io.rockscript.Configuration;
import io.rockscript.FileHandler;
import io.rockscript.api.CommandHandler;
import io.rockscript.api.QueryHandler;
import io.rockscript.engine.PingHandler;

import java.util.Map;

public class AppConfiguration extends Configuration {

  public AppConfiguration() {
    configureTest();
  }

  public AppConfiguration(String[] args) {
    this();
    configureArgs(args);
  }

  public AppConfiguration(Map<String, String> configurationProperties) {
    this();
    configureProperties(configurationProperties);
  }

  @Override
  protected void initializeRequestHandlers() {
    // The first 3 request handlers are the same as in super.initializeRequestHandlers()
    addRequestHandler(new CommandHandler());
    addRequestHandler(new QueryHandler());
    addRequestHandler(new PingHandler());
    // The AppFileHandler is customized for the App
    addRequestHandler(new AppFileHandler());
  }
}
