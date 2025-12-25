# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

ZyroFrp 是一个基于 FRP（Fast Reverse Proxy）开源项目的内网穿透服务平台，采用前后端分离架构，提供 Web 控制面板和 iOS 移动端。

**核心组件**：
- FRP 核心：`FRP/SourceCode/ZyroFrp` - Go 语言隧道引擎
- 后端 API：`WEB/BackEnd` - Spring Boot 3 + Java 21
- 前端 UI：`WEB/FrontEnd` - Vue 3 + Vite
- 数据库：`WEB/Mysql` - MySQL 8.x
- 移动端：`IOS/ZyroFrp` - iOS 原生应用

## 关键路径

| 类型 | 路径 |
|------|------|
| 前端根目录 | `WEB/FrontEnd/` |
| 后端根目录 | `WEB/BackEnd/` |
| FRP 核心 | `FRP/SourceCode/ZyroFrp/` |
| iOS 项目 | `IOS/ZyroFrp/` |
| 前端入口 | `WEB/FrontEnd/src/main.js` |
| 后端主类 | `WEB/BackEnd/src/main/java/cn/zyroo/BackendApplication.java` |
| 前端路由 | `WEB/FrontEnd/src/router/index.js` |
| API 配置 | `WEB/FrontEnd/src/modules/common/utils/api-config.js` |
| 后端配置 | `WEB/BackEnd/src/main/resources/application.yml` |
| 数据库脚本 | `WEB/Mysql/ZyroFrp.sql` |

## 构建与运行命令

### 前端 (WEB/FrontEnd)

```bash
cd WEB/FrontEnd
npm install              # 安装依赖
npm run dev             # 开发模式
npm run build           # 生产构建
npm run preview         # 预览构建产物
npm run test:unit       # 单元测试 (Vitest)
npm run test:auth       # 认证流程测试
```

### 后端 (WEB/BackEnd)

```bash
cd WEB/BackEnd
./mvnw spring-boot:run -Dspring.profiles.active=dev   # 开发模式
./mvnw package                                        # 打包
./mvnw test                                           # 运行测试
```

### FRP 核心 (FRP/SourceCode/ZyroFrp)

```bash
cd FRP/SourceCode/ZyroFrp
make build        # 构建服务端和客户端
make file         # 构建静态资源
make gotest       # Go 单元测试
make e2e          # 端到端测试
make fmt          # 代码格式化
make clean        # 清理构建产物
```

### iOS (IOS/ZyroFrp)

```bash
xcodebuild -project IOS/ZyroFrp/ZyroFrp-iOS.xcodeproj -scheme ZyroFrp-iOS build
xcodebuild test -project IOS/ZyroFrp/ZyroFrp-iOS.xcodeproj -scheme ZyroFrp-iOS
```

## 环境变量配置

### 必需的环境变量

```bash
# 环境标识
ZYRO_ENV=dev                    # 或 prod

# 后端配置
ZYRO_BACKEND_PORT=8080
DATABASE_URL=jdbc:mysql://localhost:3306/ZyroFrp
DATABASE_USERNAME=root
DATABASE_PASSWORD=your_password

# 邮件服务
MAIL_HOST=smtp.163.com
MAIL_USERNAME=your_email
MAIL_PASSWORD=your_email_password

# JWT 密钥（生产环境必须修改）
JWT_SECRET=your-secret-key
JWT_EXPIRATION_HOURS=24
```

### 配置文件加载顺序

Spring Boot 按以下顺序加载配置：
1. `application.yml`
2. `application-{profile}.yml`
3. `.env[.properties]`
4. `.env.local[.properties]`

## 项目架构

### 前端模块化结构

```
WEB/FrontEnd/src/modules/
├── admin/           # 管理员模块（用户管理、隧道管理等）
├── auth/            # 认证模块（登录、注册）
├── common/          # 通用组件和工具
├── landing/         # 落地页
├── pages/           # 页面（支付等）
└── user/            # 用户模块（仪表板、隧道配置）
```

### 后端模块划分

```
WEB/BackEnd/src/main/java/cn/zyroo/
├── announcements/   # 公告管理
├── auth/           # 认证服务
├── conf/           # 配置类
├── email/          # 邮件服务
├── log/            # 日志切面
├── servers/        # 服务器管理
├── tunnels/        # 隧道管理
└── user/           # 用户管理
```

### 前端路由

| 路由 | 功能 |
|------|------|
| `/` | 落地页 |
| `/login`, `/register` | 认证 |
| `/user-dashboard` | 用户仪表板 |
| `/tunnel-list`, `/add-tunnel` | 隧道管理 |
| `/admin-login`, `/dashboard` | 管理员入口 |
| `/user-management`, `/tunnel-management` | 管理功能 |

## 编码规范

### 所有语言
- **所有输出必须使用中文**
- 禁止提交密钥、令牌等敏感信息
- 提交前执行 `make clean` 清理构建产物

### Go (FRP 核心)
- 使用 `gofmt` + `gofumpt` 格式化
- 导入分组：`standard`、`default`、`github.com/fatedier/frp/...`
- 导出类型使用 PascalCase

### Vue (前端)
- 两个空格缩进、单引号
- 使用 Composition API
- 模块存放在 `src/modules/<domain>`

### Java (后端)
- 四空格缩进
- 包名格式：`cn.zyroo.<component>`
- DTO 使用 Lombok
- `@Service` 与 `@Repository` 独立文件

### Swift/Objective-C (iOS)
- 控制器名称使用 PascalCase
- 与 storyboard 标识保持一致

## 测试要求

- Go 新增包需补充 `*_test.go`
- Vue 单测放在 `tests/` 或组件同目录 `.test.js`
- 后端集成测试使用 `*Tests.java` 命名
- 行为变更时运行 `make e2e` 和 `npm run test:auth`

## 核心功能

### 用户端
- 用户注册/登录/密码重置
- 实名认证
- 隧道管理（创建、编辑、删除、查看配置）
- 用户签到
- FRP 客户端下载
- 使用教程

### 管理员端
- 用户管理
- 隧道管理
- 认证审核
- 公告管理
- 服务器管理
- 日志管理
- 统计分析

## 隧道协议支持

- TCP
- UDP
- HTTP
- HTTPS
