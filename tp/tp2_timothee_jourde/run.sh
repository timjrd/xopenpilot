#!/usr/bin/env bash

javac *.java

java Server 2> /dev/null &

java Client 42 2

sleep 2
