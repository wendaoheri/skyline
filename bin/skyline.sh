#!/bin/bash

cd ${0%/*}/..

CLASSPATH="${HADOOP_CONF_DIR}:${YARN_CONF_DIR}:${CLASSPATH}"

export SERVER_JAVA_OPTS=" -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector -DserverName=$SERVER_NAME -Xms8192m -Xmx8192m -XX:MaxNewSize=128m -XX:+UseConcMarkSweepGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -verbose:gc -Xloggc:logs/gc.log "
_JAR_KEYWORDS=skyline-rest-service.jar
SPRING_PROFILE=$2
APP_NAME=skyline-rest-service
PID=$(ps aux | grep ${_JAR_KEYWORDS} | grep -v grep | awk '{print $2}' )

function check_if_process_is_running {
 if [ "$PID" = "" ]; then
   return 1
 fi
 ps -p $PID | grep "java"
 return $?
}


case "$1" in
  status)
    if check_if_process_is_running
    then
      echo -e "\033[32m $APP_NAME is running \033[0m"
    else
      echo -e "\033[32m $APP_NAME not running \033[0m"
    fi
    ;;
  stop)
    if ! check_if_process_is_running
    then
      echo  -e "\033[32m $APP_NAME  already stopped \033[0m"
      exit 0
    fi
    kill -9 $PID
    echo -e "\033[32m Waiting for process to stop \033[0m"
    NOT_KILLED=1
    for i in {1..20}; do
      if check_if_process_is_running
      then
        echo -ne "\033[32m . \033[0m"
        sleep 1
      else
        NOT_KILLED=0
        break
      fi
    done
    echo
    if [ $NOT_KILLED = 1 ]
    then
      echo -e "\033[32m Cannot kill process \033[0m"
      exit 1
    fi
    echo  -e "\033[32m $APP_NAME already stopped \033[0m"
    ;;
  start)
    if [ "$PID" != "" ] && check_if_process_is_running
    then
      echo -e "\033[32m $APP_NAME already running \033[0m"
      exit 1
    fi
   echo $JAVA_OPTS $SERVER_JAVA_OPTS $_JAR_KEYWORDS
   nohup java -jar $JAVA_OPTS $SERVER_JAVA_OPTS $_JAR_KEYWORDS  > startup.log 2>&1 &
   echo -ne "\033[32m Starting \033[0m"
    for i in {1..20}; do
        sleep 1
        if check_if_process_is_running
         then
           echo -ne "\033[32m.\033[0m"
        else
           echo  -e "\033[32m $APP_NAME started \033[0m"
           break
        fi
    done

    if check_if_process_is_running
     then
       echo  -e "\033[32m $APP_NAME fail \033[0m"
    fi
    ;;
  restart)
    $0 stop
    if [ $? = 1 ]
    then
      exit 1
    fi
    $0 start
    ;;
  *)
    echo "Usage: $0 {start|stop|restart|status}"
    exit 1
esac

exit 0