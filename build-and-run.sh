#!/usr/bin/env bash

cd /Code/rockscript
mvn -DskipTests clean install
mvn -f /Code/rockscript-app/pom.xml -Pizza clean install
/Code/rockscript-app/run.sh
