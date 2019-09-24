#!/bin/bash

echo "running maven build..."
mvn -q clean package
echo "Done!"

echo "installing on host..."
cd target
scp *.jar othala:~/moba-core.jar

echo "Done!"
