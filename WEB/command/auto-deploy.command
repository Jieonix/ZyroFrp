#!/bin/bash

# 自动化部署脚本 - ZyroFrp项目
# 版本: 1.0

# 服务器配置
SERVER_HOST="us.main.zyroo.cn"
SERVER_USER="root"
SERVER_PASSWORD="Shajie@123"

# 项目路径配置
LOCAL_PROJECT_ROOT="/Users/kelvin-serendipity/Documents/GitHub/ZyroFrp/WEB"
FRONTEND_LOCAL_DIR="$LOCAL_PROJECT_ROOT/FrontEnd"
BACKEND_LOCAL_DIR="$LOCAL_PROJECT_ROOT/BackEnd"

# 服务器路径配置
SERVER_FRONTEND_DIR="/www/wwwroot/ZyroFrp/Frontend"
SERVER_BACKEND_DIR="/www/wwwroot/ZyroFrp/Backend"

# API配置
EFFECTIVE_API_ORIGIN=${ZYRO_DEPLOY_API_ORIGIN:-$ZYRO_API_ORIGIN}
if [ -z "$EFFECTIVE_API_ORIGIN" ]; then
    echo "请先设置 ZYRO_DEPLOY_API_ORIGIN（推荐）或 ZYRO_API_ORIGIN 环境变量"
    exit 1
fi
API_ORIGIN_VALUE=${EFFECTIVE_API_ORIGIN%/}
if [ "$API_ORIGIN_VALUE" = "auto" ]; then
    echo "部署脚本检测到 ZYRO_API_ORIGIN=auto，请设置 ZYRO_DEPLOY_API_ORIGIN=https://your-domain.com 以便部署使用"
    exit 1
fi
API_BASE_URL="${API_ORIGIN_VALUE}/backend/"
export ZYRO_API_ORIGIN="$API_ORIGIN_VALUE"

# 环境配置
EFFECTIVE_ENV=${ZYRO_DEPLOY_ENV:-$ZYRO_ENV}
if [ -z "$EFFECTIVE_ENV" ]; then
    echo "请先设置 ZYRO_DEPLOY_ENV（推荐）或 ZYRO_ENV 环境变量"
    exit 1
fi
if [ -n "$ZYRO_DEPLOY_ENV" ]; then
    echo "检测到 ZYRO_DEPLOY_ENV=${ZYRO_DEPLOY_ENV}，将临时覆盖 ZYRO_ENV"
fi
export ZYRO_ENV="$EFFECTIVE_ENV"

# Java进程配置
if [ -z "$ZYRO_BACKEND_PORT" ]; then
    echo "请先设置 ZYRO_BACKEND_PORT 环境变量"
    exit 1
fi
JAVA_PORT=$ZYRO_BACKEND_PORT
JAR_FILE_NAME="ZyroFrp.jar"
BACKUP_JAR_NAME="ZyroFrp_backup.jar"

# 颜色输出配置
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# ================================
# 工具函数
# ================================

# 打印带颜色的信息
print_info() {
    echo -e "${BLUE}[信息]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[成功]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[警告]${NC} $1"
}

print_error() {
    echo -e "${RED}[错误]${NC} $1"
}

# 检查命令是否存在
check_command() {
    if ! command -v $1 &> /dev/null; then
        print_error "$1 命令未找到，请先安装"
        exit 1
    fi
}

# 显示进度条
show_progress() {
    local current=$1
    local total=$2
    local desc=$3
    local width=30
    local percentage=$((current * 100 / total))
    local filled=$((current * width / total))
    local empty=$((width - filled))

    printf "\r%s: [" "$desc"
    printf "%*s" $filled | tr ' ' '='
    printf "%*s" $empty | tr ' ' '-'
    printf "] %d%%" $percentage

    if [ $current -eq $total ]; then
        echo " ✓"
    fi
}

# 监控文件上传进度（通过文件大小估算）
monitor_upload_with_progress() {
    local file_path=$1
    local description=$2
    local remote_path=$3

    if [ ! -f "$file_path" ]; then
        print_error "文件不存在: $file_path"
        return 1
    fi

    local file_size=$(stat -f%z "$file_path" 2>/dev/null || stat -c%s "$file_path" 2>/dev/null)
    local size_mb=$(echo "$file_size" | awk '{printf "%.1f", $1/1024/1024}')

    print_info "开始上传: $(basename "$file_path") (${size_mb}MB)"

    # 后台启动上传进程
    sshpass -p "$SERVER_PASSWORD" rsync -av --progress -e "ssh -o StrictHostKeyChecking=no" "$file_path" "$SERVER_USER@$SERVER_HOST:$remote_path/" > /tmp/rsync_output_$$ 2>&1 &
    local rsync_pid=$!

    # 监控上传进度
    local progress=0
    local start_time=$(date +%s)

    while kill -0 $rsync_pid 2>/dev/null; do
        progress=$((progress + 5))
        if [ $progress -gt 95 ]; then
            progress=95
        fi
        show_progress $progress 100 "$description"
        sleep 1
    done

    # 等待进程完成
    wait $rsync_pid
    local exit_code=$?

    # 显示完成进度
    show_progress 100 100 "$description"

    # 检查上传结果
    if [ $exit_code -eq 0 ]; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        print_success "$(basename "$file_path") 上传完成 (用时: ${duration}秒)"
    else
        print_error "$(basename "$file_path") 上传失败"
        if [ -f "/tmp/rsync_output_$$" ]; then
            print_error "错误信息:"
            cat "/tmp/rsync_output_$$"
        fi
    fi

    # 清理临时文件
    rm -f "/tmp/rsync_output_$$"
    return $exit_code
}


# 检查上一个命令是否执行成功
check_result() {
    if [ $? -ne 0 ]; then
        print_error "$1 失败"
        exit 1
    fi
}

# 等待用户确认
confirm() {
    read -p "$1 (y/n): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        print_info "用户取消操作"
        exit 1
    fi
}

# ================================
# 主要功能函数
# ================================

# 1. 设置生产环境变量并构建前端项目
build_frontend() {
    print_info "步骤1: 设置生产环境变量并构建前端项目"

    # 使用动态环境检测，无需设置固定环境变量

    cd "$FRONTEND_LOCAL_DIR"

    # 检查node_modules是否存在
    if [ ! -d "node_modules" ]; then
        print_warning "node_modules不存在，正在安装依赖..."
        npm install
        check_result "安装前端依赖"
    fi

    print_info "执行前端构建（使用 API_ORIGIN=${API_ORIGIN_VALUE}）..."
    npm run build
    check_result "前端构建"

    print_success "前端构建完成"
}

# 2. 连接服务器并部署前端
deploy_frontend() {
    print_info "步骤2-4: 连接服务器并部署前端文件"

    # 使用sshpass进行自动化操作
    check_command "sshpass"
    check_command "rsync"

    print_info "连接服务器并清空前端目录..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST "rm -rf $SERVER_FRONTEND_DIR/dist/*"
    check_result "清空服务器前端目录"

    print_info "开始上传前端文件..."

    # 使用进度条上传前端文件
    sshpass -p "$SERVER_PASSWORD" rsync -av --progress -e "ssh -o StrictHostKeyChecking=no" "$FRONTEND_LOCAL_DIR/dist/" "$SERVER_USER@$SERVER_HOST:$SERVER_FRONTEND_DIR/dist/" > /tmp/rsync_frontend_$$ 2>&1 &
    local rsync_pid=$!

    # 监控上传进度
    local progress=0
    local start_time=$(date +%s)

    while kill -0 $rsync_pid 2>/dev/null; do
        if [ $progress -lt 90 ]; then
            progress=$((progress + 3))
        elif [ $progress -lt 98 ]; then
            progress=$((progress + 1))
        fi
        show_progress $progress 100 "前端文件上传"
        sleep 1
    done

    # 等待进程完成
    wait $rsync_pid
    local exit_code=$?

    # 显示完成进度
    show_progress 100 100 "前端文件上传"

    # 检查上传结果
    if [ $exit_code -eq 0 ]; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        print_success "前端文件上传完成 (用时: ${duration}秒)"
        check_result "上传前端文件"
    else
        print_error "前端文件上传失败"
        if [ -f "/tmp/rsync_frontend_$$" ]; then
            print_error "错误信息:"
            cat "/tmp/rsync_frontend_$$"
        fi
        rm -f "/tmp/rsync_frontend_$$"
        exit 1
    fi

    # 清理临时文件
    rm -f "/tmp/rsync_frontend_$$"

    # 设置文件权限（忽略特殊文件）
    print_info "设置前端文件权限..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "find $SERVER_FRONTEND_DIR -type f ! -name '.user.ini' -exec chmod 644 {} \; 2>/dev/null && find $SERVER_FRONTEND_DIR -type d -exec chmod 755 {} \; 2>/dev/null || echo '权限设置完成，部分文件可能无法修改'"
    check_result "设置文件权限"

    print_success "前端部署完成"
}

# 3. 构建后端项目
build_backend() {
    print_info "步骤5: 开始构建后端项目"

    cd "$BACKEND_LOCAL_DIR"

    # 检查Maven
    check_command "java"
    check_command "./mvnw"

    # 清理系统文件和target目录
    print_info "清理系统文件和构建缓存..."
    find . -name ".DS_Store" -delete 2>/dev/null || true
    rm -rf target 2>/dev/null || true

    print_info "执行后端构建..."
    ./mvnw clean package -DskipTests
    check_result "后端构建"

    # 检查jar文件是否生成
    if [ ! -f "target/$JAR_FILE_NAME" ]; then
        print_error "后端jar文件未生成: target/$JAR_FILE_NAME"
        exit 1
    fi

    print_success "后端构建完成"
}

# 4. 部署后端到服务器
deploy_backend() {
    print_info "步骤6-8: 连接服务器并部署后端文件"

    print_info "清空服务器后端目录..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST "rm -rf $SERVER_BACKEND_DIR/*"
    check_result "清空服务器后端目录"

    local jar_file="$BACKEND_LOCAL_DIR/target/$JAR_FILE_NAME"
    local jar_size=$(stat -f%z "$jar_file" 2>/dev/null || stat -c%s "$jar_file" 2>/dev/null)

    print_info "开始上传后端JAR文件 ($(echo "$jar_size" | awk '{printf "%.1f MB", $1/1024/1024}'))..."

    # 使用进度条上传JAR文件
    sshpass -p "$SERVER_PASSWORD" rsync -av --progress -e "ssh -o StrictHostKeyChecking=no" "$jar_file" "$SERVER_USER@$SERVER_HOST:$SERVER_BACKEND_DIR/" > /tmp/rsync_jar_$$ 2>&1 &
    local rsync_pid=$!

    # 监控上传进度
    local progress=0
    local start_time=$(date +%s)

    while kill -0 $rsync_pid 2>/dev/null; do
        if [ $progress -lt 85 ]; then
            progress=$((progress + 4))
        elif [ $progress -lt 98 ]; then
            progress=$((progress + 1))
        fi
        show_progress $progress 100 "后端JAR上传"
        sleep 1
    done

    # 等待进程完成
    wait $rsync_pid
    local exit_code=$?

    # 显示完成进度
    show_progress 100 100 "后端JAR上传"

    # 检查上传结果
    if [ $exit_code -eq 0 ]; then
        local end_time=$(date +%s)
        local duration=$((end_time - start_time))
        print_success "后端JAR上传完成 (用时: ${duration}秒)"
        check_result "上传后端jar文件"
    else
        print_error "后端JAR上传失败"
        if [ -f "/tmp/rsync_jar_$$" ]; then
            print_error "错误信息:"
            cat "/tmp/rsync_jar_$$"
        fi
        rm -f "/tmp/rsync_jar_$$"
        exit 1
    fi

    # 清理临时文件
    rm -f "/tmp/rsync_jar_$$"

    # 设置JAR文件权限为755
    print_info "设置后端JAR文件权限..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "chmod 755 $SERVER_BACKEND_DIR/$JAR_FILE_NAME"
    check_result "设置JAR文件权限"

    print_success "后端部署完成"
}

# 检查服务是否正在运行
check_service_status() {
    local max_attempts=30
    local attempt=1
    local wait_time=2

    print_info "等待服务启动并检查状态..."

    while [ $attempt -le $max_attempts ]; do
        printf "\r检查服务状态: %d/%d (等待 %d 秒)" $attempt $max_attempts $((attempt * wait_time))

        # 检查端口是否在监听
        local port_status=$(sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
            "netstat -tlnp 2>/dev/null | grep :$JAVA_PORT" | head -1)

        if [ ! -z "$port_status" ]; then
            echo
            print_success "服务启动成功！"

            # 尝试获取更详细的服务信息
            print_info "服务详细信息:"
            sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
                "echo '端口监听状态:' && netstat -tlnp | grep :$JAVA_PORT && echo 'Java进程:' && ps aux | grep '$JAR_FILE_NAME' | grep -v grep"

            return 0
        fi

        # 检查是否有错误日志
        if [ $attempt -eq 10 ] || [ $attempt -eq 20 ]; then
            echo
            print_info "检查启动日志..."
            sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
                "cd $SERVER_BACKEND_DIR && tail -10 nohup.out 2>/dev/null | grep -E '(ERROR|Exception|Failed)' || echo '暂无错误信息'"
            echo
        fi

        sleep $wait_time
        ((attempt++))
    done

    echo
    print_error "服务启动超时！"

    # 显示最后的日志信息
    print_warning "显示最后的启动日志:"
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "cd $SERVER_BACKEND_DIR && tail -20 nohup.out"

    return 1
}

# 5. 重启后端服务
restart_backend_service() {
    print_info "步骤9-10: 重启后端服务"

    print_info "关闭端口 $JAVA_PORT 上的所有程序..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "pkill -f '$JAR_FILE_NAME' 2>/dev/null || true"

    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "fuser -k $JAVA_PORT/tcp 2>/dev/null || true"

    print_info "清理旧的日志文件..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "cd $SERVER_BACKEND_DIR && rm -f nohup.out"

    print_info "创建启动脚本..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "cat > $SERVER_BACKEND_DIR/start_service.sh << 'EOF'
#!/bin/bash
cd $SERVER_BACKEND_DIR
# 确保进程完全脱离终端
nohup java -jar -Xms256m -Xmx512m $JAR_FILE_NAME > nohup.out 2>&1 < /dev/null &
exit 0
EOF"

    # 设置脚本权限
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST \
        "chmod +x $SERVER_BACKEND_DIR/start_service.sh"

    print_info "在后台启动新的jar服务..."
    sshpass -p "$SERVER_PASSWORD" ssh -o StrictHostKeyChecking=no -n $SERVER_USER@$SERVER_HOST \
        "$SERVER_BACKEND_DIR/start_service.sh"

    if [ $? -eq 0 ]; then
        print_success "后端服务已启动"
        print_info "服务将在后台持续运行"
        echo
        print_info "服务信息:"
        echo "  后端API: ${API_BASE_URL}"
        echo "  服务端口: $JAVA_PORT"
        echo "  服务器: $SERVER_HOST"
        echo "  日志文件: $SERVER_BACKEND_DIR/nohup.out"
    else
        print_error "启动命令执行失败"
        return 1
    fi
}


# ================================
# 主程序
# ================================

main() {
    echo "============================================"
    echo "         ZyroFrp 自动化部署脚本"
    echo "============================================"
    echo

    # 显示配置信息
    print_info "当前配置:"
    echo "  服务器地址: $SERVER_HOST"
    echo "  前端目标目录: $SERVER_FRONTEND_DIR"
    echo "  后端目标目录: $SERVER_BACKEND_DIR"
    echo "  Java端口: $JAVA_PORT"
    echo

    # 确认开始部署
    confirm "是否开始自动化部署？"

    # 记录开始时间
    start_time=$(date +%s)

    # 执行部署步骤
    build_frontend
    deploy_frontend
    build_backend
    deploy_backend
    restart_backend_service

    # 计算总用时
    end_time=$(date +%s)
    total_time=$((end_time - start_time))

    echo
    echo "============================================"
    print_success "部署完成！总用时: ${total_time}秒"
    echo "============================================"
    echo

    print_info "部署信息:"
    echo "  前端地址: ${API_ORIGIN_VALUE}"
    echo "  后端API: ${API_BASE_URL}"
    echo "  服务器: $SERVER_HOST"
    echo

    print_warning "请检查服务是否正常运行"
}

# 错误处理
trap 'print_error "脚本执行过程中发生错误，请检查上述输出"; exit 1' ERR

# 脚本入口
if [ "${BASH_SOURCE[0]}" == "${0}" ]; then
    main "$@"
fi
