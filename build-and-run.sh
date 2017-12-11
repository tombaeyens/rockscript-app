#!/usr/bin/env bash

cd ../rockscript
mvn -Pizza clean install
mvn -f ../rockscript-app/pom.xml -Pizza clean install
java -cp /Code/rockscript-app/src/main/resources/:/Code/rockscript-app/target/rockscript.jar io.rockscript.app.AppServer examples
