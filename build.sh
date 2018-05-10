#!/bin/bash

PROJECT='five_seas'
VERSION='1.0-SNAPSHOT'
EXECUTABLE=$PROJECT'-'$VERSION

mvn compile jfx:jar
java -jar ./target/jfx/app/$EXECUTABLE-jfx.jar

