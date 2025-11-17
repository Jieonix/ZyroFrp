规定1：所有回答以及输出必须全部使用中文

规定2：输出时要有条理，一条说完换行继续下一条，让人一眼看懂

规定3：所有文档编写均使用中文

规定4：我不喜欢过多注释，更不喜欢注释有一股ai味

# 仓库协作指引

## 项目结构与模块划分

- 核心隧道代码位于 `FRP/SourceCode/ZyroFrp`，`cmd/`、`client/`、`server/`、`pkg/` 分别承担可执行文件、运行时逻辑和共享库。  
- `conf/` 提供 TOML/INI 示例，`dockerfiles/` 与 `hack/` 负责打包与自动化脚本。  
- `WEB/BackEnd` 为 Spring Boot 网关，直接与 `WEB/Mysql` 的表结构交互。  
- `WEB/FrontEnd` 是 Vite + Vue 3 SPA，模块存放在 `src/modules/<feature>`，静态资源位于 `public/`，产物输出 `dist/`。  
- `IOS/ZyroFrp` 使用 `ZyroFrp-iOS.xcodeproj`，并包含 `ZyroFrp-iOSTests`、`ZyroFrp-iOSUITests`。  

## 构建、测试与开发命令

- `cd FRP/SourceCode/ZyroFrp && make build` 生成 `bin/frps`、`bin/frpc`；`make fmt`、`make fmt-more`、`make gci` 对齐格式与导入。  
- `make gotest` 运行 Go 单测；`make e2e` 通过 `hack/run-e2e.sh` 做端到端测试，可用 `FRPC_PATH`/`FRPS_PATH` 校验兼容版本。  
- `cd WEB/BackEnd && ./mvnw spring-boot:run -Dspring.profiles.active=dev` 热启动 API；`./mvnw package` 生成 `target/` JAR。  
- `cd WEB/FrontEnd && npm install && npm run dev` 启动 Vite 服务；`npm run build` 重新构建 `dist/`。  
- `npm run test:unit` 覆盖 Vue 组件，`npm run test:auth(:headed|:debug)` 执行登录冒烟脚本。  
- `xcodebuild -project IOS/ZyroFrp/ZyroFrp-iOS.xcodeproj -scheme ZyroFrp-iOS build` 构建 iOS 包，模拟器使用 `-destination "platform=iOS Simulator,name=iPhone 15"`。  

## 编码风格与命名规范

- Go 代码遵守 `gofmt` + `gofumpt`，导入由 `gci` 分组为 `standard`、`default`、`github.com/fatedier/frp/...`。  
- 导出类型使用 PascalCase，包名保持小写，示例 `pkg/featuregate`。  
- Vue 采用两个空格缩进、单引号，Composition API 模块置于 `src/modules/<domain>`。  
- Java 后端使用四空格缩进、包名 `cn.zyroo.<component>`，DTO 使用 Lombok，`@Service` 与 `@Repository` 独立文件。  
- `IOS/ZyroFrp` 中 Swift/Objective-C 控制器名称需与 storyboard 标识一致并保持 PascalCase。  

## 测试准则

- `make gotest` 覆盖率需保持，新增 Go 包旁边补充 `*_test.go`。  
- Vue 单测放在 `tests/` 或组件同目录 `.test.js`，UI 变更可用 Vitest 快照。  
- 后端集成测试执行 `./mvnw test`，依赖 Testcontainers/H2，测试类命名 `*Tests.java`。  
- iOS 测试分别在 `ZyroFrp-iOSTests`、`ZyroFrp-iOSUITests`，通过 `xcodebuild test` 运行。  
- 行为变更时至少补跑 `make e2e` 与 `npm run test:auth`，避免回归。  

## 提交与 PR 规范

- 提交信息保持精炼中文，现在时描述影响范围，示例 `更新开发状态显示；修复bug；修改自动化部署逻辑`。  
- PR 必须说明问题背景、涉及模块（如 `FRP`、`WEB/BackEnd` 等）与测试证据（附关键命令输出）。  
- UI 或移动端改动需附截图/录屏，并关联相关 issue 或说明配置、迁移影响。  
- 同时涉及 Go 与 Web 的 PR 应说明资产重建方式（如 `make file`、`npm run build`），便于复现。  

## 安全与配置提示

- 禁止提交任何密钥；令牌、SMTP 凭据放入本地 `.env` 或 `application-*.yml`，并在 PR 中记录需求。  
- 使用 `FRP/SourceCode/ZyroFrp/conf/*.toml` 的脱敏示例统一配置；复现问题时优先提供 Dockerfile，保证端口与 TLS 一致。  
- 数据库迁移测试依赖 `WEB/Mysql` 的 SQL 样例，避免导入生产数据。  
- 提交前执行 `make clean`，清理 `target/`、`dist/` 等构建产物，除非这些文件即为目标交付物。  
