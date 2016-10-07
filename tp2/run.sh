#!/usr/bin/env bash

javac *.java

java Server 2> /dev/null

java Client $*
