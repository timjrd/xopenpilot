#!/bin/bash

mvn compile

mvn exec:java -Dexec.mainClass="fr.ubordeaux.xopenpilot.libbus.ServerTest" -Dexec.args="test 9090"
