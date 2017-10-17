#!/usr/bin/env bash

mvn -DskipTests -f ../rockscript/pom.xml clean install
mvn -Pizza clean install
rock server
