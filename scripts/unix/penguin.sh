#!/bin/sh

echo
echo --- Penguin ---------------------------------------------

workingDir=`dirname $0`
. $workingDir/env.sh
$JAVACMD org.objectweb.proactive.rmi.StartNode //localhost/one &
$JAVACMD org.objectweb.proactive.rmi.StartNode //localhost/two &
sleep 3

$JAVACMD org.objectweb.proactive.examples.penguin.Penguin //localhost/one //localhost/two

killall java

echo
echo ------------------------------------------------------------
