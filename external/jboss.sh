#          ##
##                                                                          ##
### ====================================================================== ###
# Este script soluciona mi problemilla
# Script template para demonios
#!/bin/sh
 
programa=/home/ubuntu/jboss/jboss-4.2.3.GA-Insitutoswiss-UbuntuLinux/ 
startup=$programa/bin/run.sh
shutdown=$programa/bin/shutdown.sh 

start(){
 echo -n $"Starting service: "
 $startup -b 0.0.0.0 &  
 RETVAL=$?
echo
}
 
stop(){
 echo -n $"Stopping service: "
 $shutdown -S
  ETVAL=$?
echo
}
 
restart(){
 stop
 sleep 10
 start
}
 
# Dependiento del parametro que se le pase
#start - stop - restart ejecuta la función correspondiente.
case "$1" in
start)
rm -r /home/ubuntu/jboss/jboss-4.2.3.GA-Insitutoswiss-UbuntuLinux/server/default/tmp/ /home/ubuntu/jboss/jboss-4.2.3.GA-Insitutoswiss-UbuntuLinux/server/default/work/
 start
 ;;
stop)
 stop
 ;;
restart)
 restart
 ;;
*)
 echo $"Usar: $0 {start|stop|restart}"
 exit 1
esac
 
exit 0
