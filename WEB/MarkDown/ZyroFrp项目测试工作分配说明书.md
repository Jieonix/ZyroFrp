# ZyroFrp项目测试工作分配说明书



## 项目概述

**项目名称：** ZyroFrp内网穿透管理平台
**项目性质：** 前后端分离的Web应用
**前端技术：** Vue 3 + Vite + Vant UI
**后端技术：** Spring Boot
**测试工具：** Playwright（图形化录制）





## 团队成员及职责分工

### 人员分配

| 角色 | 姓名 | 主要职责 |
|------|:-----|----------|
| **A（开发人员）** | Sha | 项目开发、代码编写、Bug修复、技术支持 |
| **B（测试人员）** | Yang | 负责分配的页面测试 |
| **C（测试人员）** | Liu | 负责分配的页面测试 |





## 页面测试任务分配

### Yang 负责的页面（12个）

| 序号 | 页面名称 | 路由路径 | 页面类型 | 主要功能 |
|-------|-------------------------|--------------------------|------------------|------------------|
|   1   |         Dashboard       |        /dashboard        | Admin | 管理员控制台主页 |
|   2   |       UserManagement    |     /user-management     | Admin | 用户管理 |
|   3   |      EmailManagement    |     /email-management    | Admin | 邮件管理 |
|   4   |       LogManagement     |      /log-management     | Admin | 日志管理 |
|   5   |        Statistics       | /statistics | Admin | 统计分析 |
|   6   |     TunnelManagement    | /tunnel-management | Admin | 隧道管理 |
|   7   |      AuthManagement     | /auth-management | Admin | 认证管理 |
|   8   |  AnnouncementManagement | /announcement-management | Admin | 公告管理 |
|   9   |     AnswerManagement    | /answer-management | Admin | 答题管理 |
|   10  |    QuestionManagement   | /question-management | Admin | 题库管理 |
|   11  |    EmailCodeManagement  | /email-code-management | Admin | 邮件码管理 |
|   12  |     ServerManagement    | /server-management | Admin | 服务器管理 |

![image-20251116161017754](/Users/kelvin-serendipity/Library/Application Support/typora-user-images/image-20251116161017754.png)

###  Liu 负责的页面（12个）

| 序号 | 页面名称 | 路由路径 | 页面类型 | 主要功能 |
|------|----------|----------|----------|----------|
| 1 | Login | /login | Auth | 用户登录 |
| 2 | Register | /register | Auth | 用户注册 |
| 3 | ResetPassword | /reset-password | Auth | 密码重置 |
| 4 | Admin_Login | /admin-login | Auth | 管理员登录 |
| 5 | UserDashboard | /user-dashboard | User | 用户控制台 |
| 6 | UserCheckIn | /user-check-in | User | 用户签到 |
| 7 | RealNameAuth | /real-name-auth | User | 实名认证 |
| 8 | TunnelList | /tunnel-list | User | 隧道列表 |
| 9 | AddTunnel | /add-tunnel | User | 添加隧道 |
| 10 | TunnelConfig | /tunnel-config | User | 隧道配置 |
| 11 | FRPDownloads | /frp-downloads | User | FRP客户端下载 |
| 12 | UserTutorials | /user-tutorials | User | 用户教程 |

![image-20251116161241671](/Users/kelvin-serendipity/Library/Application Support/typora-user-images/image-20251116161241671.png)

![image-20251116161434233](/Users/kelvin-serendipity/Library/Application Support/typora-user-images/image-20251116161434233.png)

![image-20251116161532068](/Users/kelvin-serendipity/Library/Application Support/typora-user-images/image-20251116161532068.png)

![image-20251116161634242](/Users/kelvin-serendipity/Library/Application Support/typora-user-images/image-20251116161634242.png)

![image-20251116161742834](/Users/kelvin-serendipity/Library/Application Support/typora-user-images/image-20251116161742834.png)





## 测试环境配置指南

### 测试环境信息

**测试网站地址**

http://192.168.10.50:5173



**数据库地址**

**192.168.10.50:3306**



后端API地址

192.168.10.50:8085



项目测试错误报告

https://www.kdocs.cn/l/ceFIp8trMr6x



项目待开发功能登记册

https://www.kdocs.cn/l/ch55r5wmmacC



测试用例
https://www.kdocs.cn/l/cjbzHvnnRGmB



### 安装步骤

#### 1. 安装Node.js

**Windows系统：**

```bash
# 右键 “PowerShell” → 以管理员运行
Set-ExecutionPolicy Bypass -Scope Process -Force; \
[System.Net.ServicePointManager]::SecurityProtocol = \
[System.Net.ServicePointManager]::SecurityProtocol -bor 3072; \
iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))

// 验证安装
choco -v
 
// 使用Chocolatey安装
choco install nodejs-lts
 
// 先重启powershell后验证安装
node --version
npm –version

```

#### 2. 创建测试项目目录
```bash
# 创建测试工作目录
mkdir zyroo-test
cd zyroo-test
```

#### 3. 初始化Playwright项目
```bash
# 更新power shell执行策略
Set-ExecutionPolicy -Scope CurrentUser RemoteSigned -Force

# 初始化新的Playwright项目
npm init playwright@latest

# 按照提示选择：
# - TypeScript or JavaScript: 选择 JavaScript
# - 测试文件夹名: 使用默认的 tests
# - 是否添加GitHub Actions: 选择 No
```

#### 4. 安装浏览器
```bash
# 安装Playwright浏览器
npx playwright install

# 验证安装
npx playwright --version
```

#### 5. 配置Playwright配置文件

创建或修改 `playwright.config.js` 文件：

```javascript
const { defineConfig, devices } = require('@playwright/test'); 
 
module.exports = defineConfig({ 
  testDir: './tests', 
  timeout: 30 * 10000,
 
  expect: { 
    timeout: 5000
  }, 
 
  fullyParallel: true, 
  forbidOnly: !!process.env.CI, 
  retries: process.env.CI ? 2 : 0, 
  workers: process.env.CI ? 1 : undefined, 
 
  reporter: 'html', 
 
  use: { 
    baseURL: 'http://192.168.10.50:5173',
    trace: 'on-first-retry',
    headless: false
  }, 
 
  projects: [ 
    { 
      name: 'chromium', 
      use: { ...devices['Desktop Chrome'],
        launchOptions: {
          slowMo: 2000  // 调整速率（2000毫秒为2秒）
        }
       }, 
    }, 
 
    { 
      name: 'firefox', 
      use: { ...devices['Desktop Firefox'] }, 
    }, 
 
    { 
      name: 'webkit', 
      use: { ...devices['Desktop Safari'] }, 
    }, 
  ], 
 
});
```

### 测试目录结构

```
zyroo-test/
├── tests/                  # 测试文件
│   ├── admin/              # Admin页面测试
│   ├── auth/               # Auth页面测试
│   └── user/               # User页面测试
├── test-results/           # 测试结果
├── playwright-report/      # 测试报告
├── package.json            # 项目配置
└── playwright.config.js    # Playwright配置
```





## Playwright图形化操作指南

### 基本操作流程

#### 1. 启动录制模式
```bash
# 方式一：录制新的测试
npx playwright codegen http://192.168.10.50:5173

# 方式二：录制特定页面
npx playwright codegen http://192.168.10.50:5173/xxxxxx

```

#### 2. 录制测试步骤
1. **打开浏览器**：Playwright会自动打开浏览器
2. **执行操作**：在浏览器中进行正常操作
3. **查看代码**：右侧会自动生成对应的代码
4. **复制代码**：将生成的代码复制到测试文件中

### 图形化界面功能

#### Playwright Inspector界面说明：
- **操作记录**：显示所有录制的操作
- **元素定位器**：帮助找到页面元素
- **代码预览**：实时显示生成的代码
- **断言工具**：添加验证语句

#### 常用工具按钮：
- **Record**：开始/停止录制
- **Pick Locator**：选择页面元素
- **Assert**：添加断言
- **Copy**：复制生成的代码



## 每个页面的简单测试流程

### 标准流程：简单三步测试法（图形化录制）

#### 第一步：打开页面并录制
1. **启动录制**：在命令行运行
   
   ```bash
   # 方式一：录制新的测试
   npx playwright codegen http://192.168.10.50:5173
   ```
   
2. **导航到要测试的页面**：在浏览器中点击菜单或直接输入URL

3. **开始录制操作**：点击Playwright界面上的"Record"按钮

#### 第二步：把页面上的东西都点一遍
1. **所有按钮**：点击页面上所有能看到的按钮
2. **所有输入框**：在输入框里输入一些测试内容
3. **所有链接**：点击页面上所有链接
4. **所有下拉菜单**：选择不同的选项
5. **所有复选框/单选框**：勾选和取消选择

#### 第三步：保存测试文件
1. **停止录制**：点击"Stop"按钮
2. **复制代码**：点击"Copy"按钮复制生成的代码
3. **保存文件**：创建测试文件并粘贴代码
4. **运行测试**：用UI模式查看测试过程
   
   ```bash
   # 使用chrom运行测试
   npx playwright test --project=chromium
   ```



## 交付要求和标准

### 每个页面的交付物

#### 交付的内容：
1. **测试文件**：`内容.test.js`
   
   - 存放位置：`tests/admin/`、`tests/auth/`或`tests/user/`
   
2. **测试用例**：
   
   - 至少包含基本功能测试
   
   - 包含主要交互操作
   
   - 包含基本验证
   
   - 写测试用例到测试用例Excel文档
   
     https://www.kdocs.cn/l/cjbzHvnnRGmB
   
3. **问题记录**：
   - 发现的Bug列表
   
   - 页面异常情况
   
   - 记录问题到项目测试错误报告Excel文档
   
     https://www.kdocs.cn/l/ceFIp8trMr6x

### 质量标准

#### 测试文件要求：
- **语法正确**：能够正常运行
- **逻辑清晰**：测试步骤易于理解
- **注释完整**：重要步骤有中文注释
- **断言充分**：关键操作都有验证

#### 功能覆盖要求：
- **页面访问**：能够正常打开页面

- **主要功能**：核心业务流程能够执行

- **基本验证**：操作结果能够验证

- **错误处理**：简单错误情况能够处理

  

## 常用Playwright命令

### 基础命令
```bash
# 安装依赖
npm install

# 安装浏览器
npx playwright install

# 运行所有测试（UI模式，推荐）
npx playwright test --ui

# 运行特定测试文件（UI模式）
npx playwright test tests/auth/Login.spec.js --ui

# 运行所有测试（后台运行）
npx playwright test

# 调试模式运行
npx playwright test --debug

# 录制新测试（推荐）
npx playwright codegen http://192.168.10.50:5173

# 录制特定页面
npx playwright codegen http://192.168.10.50:5173/login

# 生成测试报告
npx playwright test --reporter=html

# 查看测试报告
npx playwright show-report
```

### 录制和调试
```bash
# 启动录制界面（测试整个网站）
npx playwright codegen http://192.168.10.50:5173

# 调试特定测试
npx playwright test tests/页面名.spec.js --debug

# 显示跟踪信息
npx playwright test --trace on

# 运行测试并显示浏览器
npx playwright test --headed

# 在特定浏览器运行
npx playwright test --project=chromium
npx playwright test --project=firefox
npx playwright test --project=webkit
```





## 总结

本说明书详细说明了ZyroFrp项目的测试工作分配、环境配置、测试流程和交付要求。

测试人员需要：

1. **按照标准流程为每个页面编写测试脚本**

3. **使用Playwright图形化录制功能简化测试编写**

4. **按时交付符合质量标准的测试文件**

5. **及时记录和报告发现的问题**

   

---

**文档版本：** V1.0
**创建日期：** 2025年
**最后更新：** 2025年
**维护人员：** Sha（开发）