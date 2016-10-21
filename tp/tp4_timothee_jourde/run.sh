#!/usr/bin/env bash

javac -cp RXTXcomm.jar:. *.java

export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:.

java -cp RXTXcomm.jar:. Test $*
