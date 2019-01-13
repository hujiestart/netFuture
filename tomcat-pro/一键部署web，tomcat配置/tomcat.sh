#!/bin/bash 
export JAVA_OPTS="-Xms100m -Xmx200m"
export JAVA_HOME=/root/jdk/
export CATALINA_HOME=/root/tomcat
export CATALINA_BASE="`pwd`"

case $1 in
	start)
	$CATALINA_HOME/bin/catalina.sh start
		echo start success!!
	;;
	stop)
		$CATALINA_HOME/bin/catalina.sh stop
		echo stop success!!
	;;
	restart)
	$CATALINA_HOME/bin/catalina.sh stop
		echo stop success!!
		sleep 2 
	$CATALINA_HOME/bin/catalina.sh start
	echo start success!!
	;;
	esac
exit 0
	