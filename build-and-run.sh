#!/usr/bin/env bash

cd ../rockscript
mvn -Pizza clean install
mvn -f ../rockscript-app/pom.xml -Pizza clean install
java -jar /Code/rockscript-app/target/rockscript.jar server -ed
