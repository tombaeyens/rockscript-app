package io.rockscript.app.cqrs.commands;

import io.rockscript.cqrs.CommandsModule;
import io.rockscript.gson.PolymorphicTypeAdapterFactory;

public class AppCommandsModule implements CommandsModule {

  @Override
  public void registerCommands(PolymorphicTypeAdapterFactory polymorphicTypeAdapterFactory) {
    polymorphicTypeAdapterFactory
      .typeName(SaveScriptCommand.class, "createScript");
  }
}
