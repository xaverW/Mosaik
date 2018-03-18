#!/bin/sh
#
# wenn mehr Speicher gebraucht wird:
# java -Xmx4G -jar ./Mosaik.jar "$@"
# java -Xmx8G -jar ./Mosaik.jar "$@"
#
# wenn das Erstellen des Mosaiks sehr lange dauert
# kann man damit am Rechenr weiter arbeiten
# nice -n5 ionice -c3 java -Xmx8G -jar ./Mosaik.jar
#
# und so sieht man die Programmausgabe in der Konsole
# konsole -e "java -Xmx8G -jar ./Mosaik.jar"


dir=$(dirname $(readlink -f "$0"))
cd "$dir"

if [ -n "$JAVA_HOME" ]; then
  $JAVA_HOME/bin/java -Xmx4G -jar ./Mosaik.jar "$@"
else
  java -Xmx4G -jar ./Mosaik.jar "$@"
fi

cd $OLDPWD
