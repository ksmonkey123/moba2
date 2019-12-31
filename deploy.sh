#!/bin/bash

echo "running maven build..."
mvn -U clean package || exit
echo "done!"

echo "installing on host..."
scp core/target/moba-core.jar othala:~/moba-core.jar
scp proxy/target/moba-proxy.jar othala:~/moba-proxy.jar
echo "done!"
echo
echo "restarting host... (Ctrl-C to abort)"
ssh -t othala 'sudo reboot'
