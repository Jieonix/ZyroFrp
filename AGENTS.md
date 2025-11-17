首要规定：所有回答以及输出必须全部使用中文

# Repository Guidelines

## Project Structure & Module Organization

The core tunneling engine lives in `FRP/SourceCode/ZyroFrp`, where `cmd/`, `client/`, `server/`, and `pkg/` host the Go binaries, runtime logic, and shared libraries, while `conf/` contains TOML/INI examples and `dockerfiles/` plus `hack/` scripts support packaging and automation. `WEB/BackEnd` provides the Spring Boot gateway that talks to MySQL schemas stored under `WEB/Mysql`, and `WEB/FrontEnd` is a Vite-powered Vue 3 SPA split into `src/modules/<feature>` folders with assets under `public/` and production bundles in `dist/`. iOS artifacts sit inside `IOS/ZyroFrp`, using the `ZyroFrp-iOS.xcodeproj` with paired `ZyroFrp-iOSTests` and `ZyroFrp-iOSUITests`.

## Build, Test, and Development Commands

- `cd FRP/SourceCode/ZyroFrp && make build` builds static `bin/frps` and `bin/frpc`; `make fmt`, `make fmt-more`, and `make gci` align formatting and imports before committing.
- `make gotest` runs the Go unit suites with coverage across `assets`, `cmd`, `client`, `server`, and `pkg`; `make e2e` executes `hack/run-e2e.sh` end-to-end tunnels (set `FRPC_PATH`/`FRPS_PATH` to test compat builds).
- `cd WEB/BackEnd && ./mvnw spring-boot:run -Dspring.profiles.active=dev` hot-reloads the API; `./mvnw package` produces a runnable JAR in `target/`.
- `cd WEB/FrontEnd && npm install && npm run dev` serves the SPA with Vite; use `npm run build` to regenerate `dist/` assets consumed by the Go embed step.
- `npm run test:unit` covers Vue components under `tests/` with Vitest and jsdom, while `npm run test:auth(:headed|:debug)` drives the login smoke script.
- `xcodebuild -project IOS/ZyroFrp/ZyroFrp-iOS.xcodeproj -scheme ZyroFrp-iOS build` produces the app bundle; pair it with `-destination "platform=iOS Simulator,name=iPhone 15"` for simulator runs.

## Coding Style & Naming Conventions

Go code follows `gofmt` defaults with `gofumpt` tightening spacing and `gci` grouping imports (`standard`, `default`, then `github.com/fatedier/frp/...`); use PascalCase for exported types and keep package names lowercase (for example, `pkg/featuregate`). Vue files stick to two-space indentation, single quotes, and Composition API modules under `src/modules/<domain>` (e.g., `modules/pages/payment`). Java backend classes follow Spring conventions—4-space indentation, package names like `cn.zyroo.<component>`, Lombok annotations for DTOs, and `@Service`/`@Repository` roles separated per file. Swift or Objective-C controllers in `IOS/ZyroFrp` should retain PascalCase type names matching storyboard identifiers.

## Testing Guidelines

Target the coverage levels produced by `make gotest` and keep new Go packages from dropping the reported percentage; place tests next to sources as `*_test.go`. Vue unit tests belong in `tests/` or alongside components as `.test.js` files using Vitest snapshots when UI changes. Backend integration tests run through `./mvnw test` and rely on Testcontainers/H2; name suites `*Tests.java`. iOS tests live in the `ZyroFrp-iOSTests` and `ZyroFrp-iOSUITests` bundles and should be runnable through `xcodebuild test`. For behavioral changes, add at least one end-to-end run via `make e2e` plus an authentication script (`npm run test:auth`) so regressions are caught before shipping.

## Commit & Pull Request Guidelines

Existing history favors compact, descriptive summaries (often Chinese) such as `更新开发状态显示；修复bug；修改自动化部署逻辑`; continue using one-line, present-tense statements that mention the touched area, then expand in the body if needed. Every pull request should include: a short problem statement, a checklist of touched modules (`FRP`, `WEB/BackEnd`, etc.), test evidence (paste key command output), and screenshots or simulator recordings for UI or mobile updates. Link related issues and call out config or migration impacts; PRs that touch both Go and web layers should describe how assets were rebuilt (`make file`, `npm run build`) to help reviewers reproduce.

## Security & Configuration Tips

Never commit secrets; keep tokens and SMTP creds in local `.env` or profile-specific YAML files (`WEB/BackEnd/src/main/resources/application-*.yml`) and document any required keys inside pull requests. Use the sanitized examples in `FRP/SourceCode/ZyroFrp/conf/*.toml` as your template, and rely on Dockerfiles when sharing reproductions so ports and TLS settings stay consistent. When testing database migrations, seed stub data via the SQL dumps inside `WEB/Mysql` instead of production exports. Always scrub binaries before pushing by running `make clean` and clearing `target/` or `dist/` outputs unless they are the intended artifacts.
