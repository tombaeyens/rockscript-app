#!/usr/bin/env bash

cd /Code/rockscript
mvn -DskipTests clean install
mvn -f /Code/rockscript-app/pom.xml -Pizza clean install
java -cp /Code/rockscript-app/rockscript-app-httpfiles/src/main/resources/:/Code/rockscript-app/rockscript-app-server/target/rockscript-app.jar io.rockscript.app.AppServer examples
