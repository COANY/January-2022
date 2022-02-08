#!/bin/bash

#jar包文件路径及名称（目录按照各自配置）
APP_NAME=/data/jar/huashu/manage-api-0.0.1-SNAPSHOT.jar

#日志文件路径及名称（目录按照各自配置）
LOG_FILE=/data/jar/huashu/manage-api.log

#查询进程，并杀掉当前jar/java程序

pid=`ps -ef|grep $APP_NAME | grep -v grep | awk '{print $2}'`
kill -9 $pid
echo "$pid进程终止成功"

sleep 2

#判断jar包文件是否存在，如果存在启动jar包，并时时查看启动日志

if test -e $APP_NAME
then
echo '文件存在,开始启动此程序...'

# 启动jar包，指向日志文件，2>&1 & 表示打开或指向同一个日志文件
nohup java -jar -Dloader.path=./lib/ -Xms1024m -Xmx1024m $APP_NAME > $LOG_FILE 2>&1 &

#实时查看启动日志（此处正在想办法启动成功后退出）
tail -f $LOG_FILE

#输出启动成功（上面的查看日志没有退出，所以执行不了，可以去掉）

echo '$APP_NAME 启动成功...'
else
echo '$APP_NAME 文件不存在,请检查。'
fi
