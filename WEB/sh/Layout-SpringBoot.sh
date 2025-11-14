#!/bin/bash

# -------------------------------
# Layout-SpringBoot.sh
# 功能：部署并后台启动 Spring Boot jar
# -------------------------------

set -e

# 1. 设置路径
JAR_PATH="/www/wwwroot/ZyroFrp/Backend/ZyroFrp.jar"
LOG_PATH="/www/wwwroot/ZyroFrp/Backend/nohup.out"
JAR_NAME=$(basename "$JAR_PATH")

# 使用 SDKMAN 安装的 Java 完整路径
JAVA_CMD="/root/.sdkman/candidates/java/current/bin/java"

echo "使用 Java: $JAVA_CMD"
$JAVA_CMD -version

# 检查 Java 是否存在
if [ ! -f "$JAVA_CMD" ]; then
    echo "错误：Java 未找到在 $JAVA_CMD"
    exit 1
fi

# 检查jar文件
if [ ! -f "$JAR_PATH" ]; then
    echo "错误：jar文件不存在 - $JAR_PATH"
    exit 1
fi

# 2. 停止旧进程
echo "查找旧进程..."
PID=$(ps -ef | grep "java.*$JAR_NAME" | grep -v grep | awk '{print $2}')

if [ -n "$PID" ]; then
    echo "检测到旧进程 PID=$PID，正在停止..."
    kill $PID
    sleep 5
    if ps -p $PID > /dev/null; then
        echo "正常停止失败，强制杀死进程..."
        kill -9 $PID
        sleep 2
    fi
    echo "旧进程已停止。"
else
    echo "未找到运行的旧进程。"
fi

# 3. 备份旧日志（可选）
if [ -f "$LOG_PATH" ]; then
    mv "$LOG_PATH" "${LOG_PATH}.old"
    echo "已备份旧日志文件。"
fi

# 4. 启动新进程
echo "启动新的 Spring Boot 应用..."
nohup $JAVA_CMD -jar "$JAR_PATH" > "$LOG_PATH" 2>&1 &
NEW_PID=$!

# 5. 检查启动状态
sleep 5
if ps -p $NEW_PID > /dev/null; then
    echo "启动成功！新进程 PID=$NEW_PID"
    echo "日志路径：$LOG_PATH"
else
    echo "启动失败！请检查日志：$LOG_PATH"
    exit 1
fi

echo "Spring Boot 项目部署完成！"