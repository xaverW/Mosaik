#!/bin/sh
#
# Wenn mehr Speicher gebraucht wird:
# java -Xmx2G -jar ./Mosaik.jar "$@"
# java -Xmx4G -jar ./Mosaik.jar "$@"


dir=$(dirname $(readlink -f "$0"))
cd "$dir"

if [ -n "$JAVA_HOME" ]; then
  $JAVA_HOME/bin/java -Xmx4G -jar ./Mosaik.jar "$@"
else
  java -Xmx4G -jar ./Mosaik.jar "$@"
fi

cd $OLDPWD
