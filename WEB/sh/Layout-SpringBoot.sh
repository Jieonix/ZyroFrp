#!/bin/bash

# -------------------------------
# Layout-SpringBoot.sh
# 功能：部署并后台启动 Spring Boot jar
# -------------------------------

# 1. 设置 jar 路径和日志路径
JAR_PATH="/www/wwwroot/ZyroFrp/Backend/ZyroFrp.jar"
LOG_PATH="/www/wwwroot/ZyroFrp/Backend/nohup.out"

# 2. 查找旧进程并停止
PID=$(ps -ef | grep "$JAR_PATH" | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
    echo "检测到旧进程 PID=$PID，正在停止..."
    kill -9 $PID
    echo "旧进程已停止。"
fi

# 3. 启动新的 jar（后台 + nohup）
echo "启动新的 Spring Boot 应用..."
nohup java -jar "$JAR_PATH" > "$LOG_PATH" 2>&1 &
NEW_PID=$!
echo "新进程 PID=$NEW_PID，日志路径：$LOG_PATH"

# 4. 完成
echo "Spring Boot 项目部署完成！"
