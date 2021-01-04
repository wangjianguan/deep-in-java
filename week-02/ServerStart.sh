#!/bin/bash

echo '开始启动Server'

curPath=$(cd `dirname $0`; pwd)

mvn clean package

java -jar $curPath/target/week-02-1.0.jar
