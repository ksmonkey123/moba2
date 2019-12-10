#!/bin/bash

echo "running maven build..."
mvn -U clean package
echo "done!"

echo "installing on host..."
cd target
scp *.jar othala:~/moba-core.jar
echo "done!"
echo
echo "restarting host... (Ctrl-C to abort)"
ssh -t othala 'sudo reboot'
