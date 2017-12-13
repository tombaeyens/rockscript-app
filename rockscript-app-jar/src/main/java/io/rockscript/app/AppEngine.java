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

import io.rockscript.Engine;
import io.rockscript.FileHandler;
import io.rockscript.util.Maps;

import java.util.Map;

import static io.rockscript.util.Maps.entry;
import static io.rockscript.util.Maps.hashMap;

public class AppEngine extends Engine {

  public AppEngine(String[] args) {
    super(args);
  }

  public AppEngine(Map<String,String> configuration) {
    super(configuration);
  }

  public AppEngine() {
    super(hashMap(entry(CFG_KEY_ENGINE, CFG_VALUE_ENGINE_TEST)));
  }

  @Override
  protected FileHandler createFileHandler() {
    return new AppFileHandler(this);
  }
}
