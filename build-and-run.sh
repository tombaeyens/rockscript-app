#!/usr/bin/env bash

mvn -f ../rockscript/pom.xml -Pizza clean install
mvn -Pizza clean install
cd ../rockscript
java -jar /Code/rockscript-app/target/rockscript.jar server -ed
