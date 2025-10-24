# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

ZyroFrp 是一个完整的内网穿透(FRP)系统，采用多模块全栈架构：

- **前端**: Vue 3 + Vite + TypeScript + Vant UI (移动端)
- **后端**: Spring Boot 3.4.3 + Java 21 + JPA + MyBatis
- **核心服务**: Go 语言开发的 FRP 客户端和服务端
- **数据库**: MySQL 8.0

## 项目结构

```
ZyroFrp/
├── WEB/                    # Web应用模块
│   ├── FrontEnd/          # Vue 3前端应用
│   └── BackEnd/           # Spring Boot后端应用
├── FRP/                   # FRP核心服务
│   └── SourceCode/ZyroFrp/ # Go语言FRP实现
└── IOS/                   # iOS应用
```

## 常用开发命令

### 前端开发 (WEB/FrontEnd/)
```bash
# 安装依赖
npm install

# 开发服务器
npm run dev

# 生产构建
npm run build

# 运行测试
npm run test:unit
```

### 后端开发 (WEB/BackEnd/)
```bash
# 使用Maven包装器开发
./mvnw spring-boot:run

# 构建项目
./mvnw clean package

# 运行测试
./mvnw test

# 清理项目
./mvnw clean
```

### FRP核心服务 (FRP/SourceCode/ZyroFrp/)
```bash
# 构建客户端和服务端
make build

# 仅构建服务端
make frps

# 仅构建客户端
make frpc

# 运行测试
make test

# 代码格式化
make fmt

# 静态检查
make vet

# 清理构建产物
make clean

# 将Web资源编译到二进制文件
make file

# 完整开发流程
make all
```

### FRP Web界面开发
```bash
# 服务端Web界面 (FRP/SourceCode/ZyroFrp/web/frps/)
yarn install
make dev
make build

# 客户端Web界面 (FRP/SourceCode/ZyroFrp/web/frpc/)
yarn install
make dev
make build
```

## 架构说明

### 多层次架构设计
1. **表现层**: Vue 3前端 + Vant UI移动端组件
2. **API层**: Spring Boot RESTful API
3. **业务层**: 用户管理、隧道配置管理
4. **数据层**: MySQL数据库持久化
5. **核心服务层**: Go语言FRP核心服务

### 核心功能模块

#### 前端模块 (Vue 3)
- 用户认证系统 (注册、登录、密码重置)
- 隧道管理界面
- 管理员后台
- 响应式移动端设计
- 状态管理 (Pinia)
- 路由系统 (Vue Router)

#### 后端模块 (Spring Boot)
- Spring Security + JWT认证
- 用户管理API
- 隧道配置管理
- 邮件服务集成
- JPA + MyBatis数据持久化

#### FRP核心服务 (Go)
- **客户端(frpc)**: 支持HTTP、HTTPS、TCP、UDP、STCP、XTCP等代理类型
- **服务端(frps)**: 代理转发、分组管理、端口管理、指标监控
- 插件系统 (客户端和服务端)
- 健康检查机制
- Prometheus指标集成

### 技术特性

#### 安全机制
- JWT Token认证
- Spring Security权限控制
- TLS加密传输
- 用户角色管理

#### 可扩展性
- 插件化架构 (Go模块)
- 多种代理协议支持
- 容器化部署 (Docker)
- 跨平台支持

## 开发注意事项

### 前端开发
- 使用 Vite 路径别名 `@` 指向 `src` 目录
- SVG 图标存放在 `src/assets/icons/`，通过 vite-plugin-svg-icons 插件使用
- 采用 Composition API 编写组件
- 使用 Vant UI 组件库保持移动端一致性

### 后端开发
- Java 21 + Spring Boot 3.4.3
- 使用 Lombok 简化代码
- JWT Token 有效期和刷新机制
- 数据库连接池配置
- 邮件服务配置

### FRP核心开发
- Go 1.24.0
- 模块化设计，客户端和服务端分离
- 配置文件支持多种格式 (TOML、JSON)
- 插件系统支持功能扩展
- 完整的单元测试和端到端测试

### 测试策略
- 前端: Vitest 单元测试
- 后端: JUnit 5 集成测试
- FRP核心: Go test + Ginkgo/Gomega + E2E测试

## 构建和部署

### 开发环境启动顺序
1. 启动 MySQL 数据库
2. 启动 Spring Boot 后端服务
3. 启动 Vue 3 前端开发服务器
4. 根据需要启动 FRP 服务端或客户端

### 生产构建
1. 前端: `npm run build`
2. 后端: `./mvnw clean package`
3. FRP核心: `make build` (或使用预编译二进制文件)

### 跨平台编译
FRP核心支持通过 build-all-platforms.sh 脚本进行全平台编译，支持 Linux、macOS、Windows 等多个架构。