# ZyroFrp 项目说明

## 项目背景

- ZyroFrp 的核心隧道服务基于 GitHub 上的开源项目 [fatedier/frp](https://github.com/fatedier/frp) 进行定制与扩展，所有改动均遵循原始项目的开源许可，完整保留原有版权与贡献者信息。  
- 在此基础上，我们引入 Web 控制面板、Spring Boot 网关、前端 SPA、iOS 端入口等配套模块，形成一体化的轻量级内网穿透与隧道管理平台。  

## 仓库结构概览

- `FRP/SourceCode/ZyroFrp`：核心隧道引擎，包含 `cmd/`、`client/`、`server/`、`pkg/` 等 Go 模块，`conf/` 提供示例配置，`dockerfiles/` 与 `hack/` 存放打包与自动化脚本。  
- `WEB/BackEnd`：基于 Spring Boot 的 API 网关，负责认证、租户管理与与 MySQL（`WEB/Mysql`）的交互。  
- `WEB/FrontEnd`：Vite + Vue 3 单页应用，模块化拆分在 `src/modules/<feature>`，静态资源位于 `public/`，产物输出到 `dist/` 并可嵌入 Go 服务。  
- `IOS/ZyroFrp`：移动端客户端工程，使用 `ZyroFrp-iOS.xcodeproj`，并包含 `ZyroFrp-iOSTests` 与 `ZyroFrp-iOSUITests` 提供功能及 UI 测试。  

## 核心功能

- 🌐 **多协议内网穿透**：继承 frp 原生的 TCP/UDP/HTTP/HTTPS 隧道能力，并在配置层加入更细粒度的租户与带宽控制。  
- 🧠 **集中式控制面板**：通过 Spring Boot + Vue 3 提供的控制台管理隧道、查看连接状态、更新客户端配置。  
- 📱 **多端入口**：除 Web 控制台外，iOS 客户端提供便捷的移动控制与通知体验。  
- 🔐 **安全与权限**：默认启用令牌校验、TLS 终端支持，并推荐使用脱敏配置样例与 Docker 化部署。  

## 构建与开发指南

### FRP 核心

- 进入 `FRP/SourceCode/ZyroFrp` 后执行 `make build` 生成静态 `bin/frps` 与 `bin/frpc`。  
- 提交前使用 `make fmt`、`make fmt-more`、`make gci` 统一格式与导入。  
- `make gotest` 覆盖 `assets`、`cmd`、`client`、`server`、`pkg` 的 Go 单测，`make e2e` 可通过 `hack/run-e2e.sh` 运行端到端隧道验证（支持 `FRPC_PATH`/`FRPS_PATH` 自定义）。  

### Web 后端

- 进入 `WEB/BackEnd` 后运行 `./mvnw spring-boot:run -Dspring.profiles.active=dev` 进入开发模式，`./mvnw package` 产出 `target/` 目录内的可运行 JAR。  
- Spring Boot 服务依赖 `WEB/Mysql` 提供的 schema 与样例数据，推荐通过 Docker 或 Testcontainers 启动测试数据库。  

### Web 前端

- 进入 `WEB/FrontEnd` 后执行 `npm install && npm run dev` 启动开发服务器，`npm run build` 生成 `dist/` 产物供 Go 服务 embed。  
- 单元测试通过 `npm run test:unit`（Vitest + jsdom）运行，鉴权流程可用 `npm run test:auth(:headed|:debug)` 冒烟校验。  

### iOS 客户端

- 使用 `xcodebuild -project IOS/ZyroFrp/ZyroFrp-iOS.xcodeproj -scheme ZyroFrp-iOS build` 构建，模拟器调试可附加 `-destination "platform=iOS Simulator,name=iPhone 15"`。  
- `xcodebuild test` 可针对 `ZyroFrp-iOSTests` 与 `ZyroFrp-iOSUITests` 执行自动化测试。  

## 配置与部署建议

- 通过 `FRP/SourceCode/ZyroFrp/conf/*.toml` 获取脱敏示例，生产环境的令牌、SMTP 等敏感信息务必保存在 `.env` 或 `WEB/BackEnd/src/main/resources/application-*.yml` 等私密文件中。  
- 推荐使用仓库内的 `dockerfiles/` 构建镜像，以保证端口映射、TLS 证书路径与官方示例一致；需要复现问题时提供对应 Docker Compose/命令，便于审查。  
- 提交代码前执行 `make clean` 并清理 `target/`、`dist/` 等构建产物，避免二进制污染版本库。  

## 贡献说明

- 在遵循原始 frp 许可的基础上，我们鼓励贡献者通过 issue/PR 讨论新增功能，提交信息保持中文、精炼、现在时描述。  
- PR 模板建议包含：问题背景说明、影响模块清单 (`FRP`、`WEB/BackEnd`、`WEB/FrontEnd`、`IOS` 等)、测试结果截图/命令输出、若涉及 UI/移动端则附带录屏或截图。  
- 如需新增第三方依赖或修改配置格式，请同步更新相关示例与文档，确保各端可用。  

## 许可声明

- ZyroFrp 的核心隧道部分派生自 [fatedier/frp](https://github.com/fatedier/frp)，我们感谢原项目维护者，并将持续遵守其开源许可。  
- 任何向 ZyroFrp 提交的贡献默认按照相同许可分发，请勿包含未授权或含敏感信息的内容。  
