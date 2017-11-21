#!/usr/bin/env bash

( cd ../rockscript ; mvn -DskipTests clean install )
mvn -Pizza clean install
rm -rf target/appengine-staging
mkdir target/appengine-staging
cp src/main/appengine/app.yaml target/appengine-staging
cp target/rockscript.jar target/appengine-staging
( cd target/appengine-staging ; gcloud -q app deploy )