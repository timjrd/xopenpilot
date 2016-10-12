#!/usr/bin/env bash

javac *.java

java Server &

java Client 42 2

sleep 2
