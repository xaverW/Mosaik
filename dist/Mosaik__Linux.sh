#!/bin/sh
#
# Wenn der Arbeitsspeicher knapp ist, kann das helfen:
# java -Xms128M -Xmx1G -jar ./Mosaik.jar "$@"


dir=$(dirname $(readlink -f "$0"))
cd "$dir"

if [ -n "$JAVA_HOME" ]; then
  $JAVA_HOME/bin/java -jar ./Mosaik.jar "$@"
else
  java -jar ./Mosaik.jar "$@"
fi

cd $OLDPWD
