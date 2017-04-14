#!/bin/bash

mvn compile

mvn exec:java -Dexec.mainClass="fr.ubordeaux.xopenpilot.bus.BusServer" -Dexec.args=""
