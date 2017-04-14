#!/bin/bash

mvn compile

mvn exec:java -Dexec.mainClass="fr.ubordeaux.xopenpilot.libbus.SenderClientTest" -Dexec.args=""
