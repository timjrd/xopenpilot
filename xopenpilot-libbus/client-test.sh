#!/bin/bash

mvn compile

mvn exec:java -Dexec.mainClass="fr.ubordeaux.xopenpilot.libbus.ClientTest" -Dexec.args="test localhost 9090"
