/*
 * Copyright (c) 2017, RockScript.io. All rights reserved.
 */
package io.rockscript.app;

import io.rockscript.Server;

public class RockScriptApp {

  public static void main(String[] args) {
    new Server()
      .parseArgs("server", "-p", "8080")
      .execute();
  }
}
