#!/usr/bin/env bash

cd /Code/rockscript-app
cp -f rockscript-app-server/target/rockscript-app.jar /Code/rockscript.github.io/downloads
cd /Code/rockscript.github.io
git add .
git commit -m "Release rockscript app"
git push
