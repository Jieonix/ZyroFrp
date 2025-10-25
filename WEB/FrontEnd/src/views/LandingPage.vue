<template>
  <div class="landing-page">
    <!-- 顶部导航栏 -->
    <nav class="navbar">
      <div class="nav-container">
        <!-- Logo -->
        <div class="logo">
          <h1>ZyroFrp</h1>
        </div>

        <!-- 导航菜单 -->
        <ul class="nav-menu">
          <li @click="scrollToTop" class="nav-item">首页</li>
          <li @click="goTo('#features')" class="nav-item">功能特色</li>
          <li @click="goTo('#pricing')" class="nav-item">价格方案</li>
          <li @click="goTo('#tutorials')" class="nav-item">使用教程</li>
          <li @click="goTo('#about')" class="nav-item">关于我们</li>
        </ul>

        <!-- 用户操作区域 -->
        <div class="user-actions">
          <!-- GitHub 按钮 -->
          <button class="github-btn" @click="openGitHub">
            <svg width="18" height="18" viewBox="0 0 16 16" fill="currentColor">
              <path
                d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z" />
            </svg>
            <span>GitHub</span>
          </button>

          <!-- 用户状态按钮 -->
          <div v-if="isLoggedIn" class="user-logged-in">
            <button class="dashboard-btn" @click="goToDashboard">控制台</button>
            <button class="logout-btn" @click="logout">退出</button>
          </div>
          <div v-else class="user-logged-out">
            <button class="login-btn" @click="goToLogin">登录</button>
            <button class="register-btn" @click="goToRegister">注册</button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主横幅区域 -->
    <section class="hero">
      <div class="hero-background">
        <div class="gradient-orb orb-1"></div>
        <div class="gradient-orb orb-2"></div>
        <div class="gradient-orb orb-3"></div>
        <div class="noise-overlay"></div>
      </div>

      <div class="hero-container">
        <div class="hero-content">
          <div class="hero-badge">
            <span class="badge-text">🚀 全新升级</span>
          </div>
          <h1 class="hero-title">
            <span class="title-gradient">让内网穿透变得</span>
            <span class="title-highlight">简单高效</span>
          </h1>
          <p class="hero-subtitle">
            企业级内网穿透解决方案，为开发者和企业提供稳定、高速、安全的网络连接服务
          </p>
          <div class="hero-actions">
            <button class="cta-primary" @click="handleFreeTrial">
              <span>免费试用</span>
              <svg class="btn-arrow" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <path d="M6 3L11 8L6 13" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round" />
              </svg>
            </button>
            <button class="cta-secondary" @click="scrollToFeatures">
              <span>查看演示</span>
              <svg class="btn-play" width="16" height="16" viewBox="0 0 16 16" fill="none">
                <circle cx="8" cy="8" r="7" stroke="currentColor" stroke-width="1.5" />
                <path d="M6 5V11L11 8L6 5Z" fill="currentColor" />
              </svg>
            </button>
          </div>

          <div class="trust-indicators">
            <div class="trust-item">
              <div class="trust-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <path d="M10 1L12.5 7.5L19 8L14 12L16 19L10 15L4 19L6 12L1 8L7.5 7.5L10 1Z" fill="currentColor" />
                </svg>
              </div>
              <div class="trust-text">
                <div class="trust-number">10K+</div>
                <div class="trust-label">活跃用户</div>
              </div>
            </div>
            <div class="trust-item">
              <div class="trust-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <path
                    d="M10 18C14.4183 18 18 14.4183 18 10C18 5.58172 14.4183 2 10 2C5.58172 2 2 5.58172 2 10C2 14.4183 5.58172 18 10 18Z"
                    stroke="currentColor" stroke-width="2" />
                  <path d="M10 6V10L13 13" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
                </svg>
              </div>
              <div class="trust-text">
                <div class="trust-number">99.9%</div>
                <div class="trust-label">服务可用性</div>
              </div>
            </div>
            <div class="trust-item">
              <div class="trust-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <path d="M3 12L7 16L17 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                    stroke-linejoin="round" />
                </svg>
              </div>
              <div class="trust-text">
                <div class="trust-number">24/7</div>
                <div class="trust-label">技术支持</div>
              </div>
            </div>
          </div>
        </div>

        <div class="hero-visual">
          <div class="terminal-window">
            <div class="terminal-header">
              <div class="terminal-controls">
                <div class="control control-red"></div>
                <div class="control control-yellow"></div>
                <div class="control control-green"></div>
              </div>
              <div class="terminal-title">zyrofrp-client</div>
            </div>
            <div class="terminal-body">
              <div class="terminal-line">
                <span class="prompt">$</span>
                <span class="command">./frpc -c ./frpc.toml</span>
              </div>
              <div class="terminal-line success">
                <span class="output">→ 2024/01/15 10:23:45 [I] [proxy.go:102] [e5abf6c4a3c3] proxy [web] started
                  successfully</span>
              </div>
              <div class="terminal-line success">
                <span class="output">→ 2024/01/15 10:23:45 [I] [control.go:180] [e5abf6c4a3c3] login to server
                  success</span>
              </div>
              <div class="terminal-line info">
                <span class="output">→ 2024/01/15 10:23:46 [I] [proxy.go:207] [e5abf6c4a3c3] [web] get a user
                  connection</span>
              </div>
              <div class="terminal-cursor"></div>
            </div>
          </div>

          <div class="floating-cards">
            <div class="float-card card-1">
              <div class="card-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <path d="M10 2L3 7v6c0 3.5 3 6 7 7 4-1 7-3.5 7-7V7l-7-5z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <div class="card-label">HTTP/HTTPS</div>
            </div>
            <div class="float-card card-2">
              <div class="card-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <circle cx="10" cy="10" r="8" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M10 6v4l3 2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
              <div class="card-label">TCP/UDP</div>
            </div>
            <div class="float-card card-3">
              <div class="card-icon">
                <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                  <rect x="4" y="6" width="12" height="8" rx="1" stroke="currentColor" stroke-width="1.5"/>
                  <path d="M7 10h6" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/>
                  <circle cx="10" cy="10" r="1" fill="currentColor"/>
                </svg>
              </div>
              <div class="card-label">安全加密</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能特色区域 -->
    <section id="features" class="features-section">
      <div class="container">
        <div class="section-header">
          <div class="section-badge">核心功能</div>
          <h2 class="section-title">强大而灵活的功能</h2>
          <p class="section-subtitle">为不同规模的企业和开发者提供完整的内网穿透解决方案</p>
        </div>

        <div class="features-grid">
          <div v-for="(feature, index) in coreFeatures" :key="index" class="feature-card"
            :class="{ 'featured': feature.featured, 'hovered': hoveredFeature === index }"
            @mouseenter="featureHover(index)" @mouseleave="featureHover(null)" @click="handleFeatureClick()">
            <div class="feature-content">
              <div class="feature-icon" :class="feature.iconClass">
                <svg v-if="feature.iconType === 'lightning'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                <svg v-else-if="feature.iconType === 'shield'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round" />
                  <path d="M9 12l2 2 4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                    stroke-linejoin="round" />
                </svg>
                <svg v-else-if="feature.iconType === 'settings'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2" />
                  <path d="M12 1v6m0 6v6m11-7h-6m-6 0H1" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" />
                  <path d="M20.5 7.5L16 12l4.5 4.5M3.5 7.5L8 12l-4.5 4.5" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round" />
                </svg>
                <svg v-else-if="feature.iconType === 'chart'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <path d="M18 20V10m-6 10V4m-6 16v-6" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
                </svg>
                <svg v-else-if="feature.iconType === 'protocol'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <rect x="3" y="3" width="18" height="18" rx="2" stroke="currentColor" stroke-width="2" />
                  <path d="M8 12h8m-4-4v8" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
                </svg>
                <svg v-else-if="feature.iconType === 'platform'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                  <rect x="2" y="3" width="20" height="14" rx="2" stroke="currentColor" stroke-width="2" />
                  <path d="M8 21h8m-4-4v4" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
                </svg>
                <span v-else class="icon-emoji">{{ feature.emoji }}</span>
              </div>
              <div class="feature-text">
                <h3>{{ feature.title }}</h3>
                <p>{{ feature.description }}</p>
              </div>
            </div>
            <div class="feature-arrow">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M7 4L13 10L7 16" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round" />
              </svg>
            </div>
          </div>
        </div>

        <div class="feature-showcase">
          <div class="showcase-left">
            <div class="showcase-content">
              <h3>智能路由</h3>
              <p>自动选择最优路径，确保数据传输的高效性和稳定性</p>
              <ul class="showcase-features">
                <li><svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M2 8L6 12L14 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                      stroke-linejoin="round" />
                  </svg>智能负载均衡</li>
                <li><svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M2 8L6 12L14 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                      stroke-linejoin="round" />
                  </svg>故障自动切换</li>
                <li><svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M2 8L6 12L14 4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                      stroke-linejoin="round" />
                  </svg>全球节点覆盖</li>
              </ul>
            </div>
          </div>
          <div class="showcase-visual">
            <div class="network-diagram">
              <div class="node central">智能路由</div>
              <div class="node node1">节点1</div>
              <div class="node node2">节点2</div>
              <div class="node node3">节点3</div>
              <div class="connection conn1"></div>
              <div class="connection conn2"></div>
              <div class="connection conn3"></div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 价格方案区域 -->
    <section id="pricing" class="pricing-section">
      <div class="container">
        <div class="section-header">
          <div class="section-badge">价格方案</div>
          <h2 class="section-title">选择适合您的方案</h2>
          <p class="section-subtitle">从个人开发者到企业级应用，我们都有合适的解决方案</p>
        </div>

        <div class="pricing-content">
          <div class="pricing-toggle">
            <button class="toggle-btn" :class="{ active: billingPeriod === 'monthly' }"
              @click="setBillingPeriod('monthly')">按月付费</button>
            <button class="toggle-btn" :class="{ active: billingPeriod === 'yearly' }"
              @click="setBillingPeriod('yearly')">按年付费 <span class="discount">省20%</span></button>
          </div>

          <div class="pricing-grid">
            <div v-for="(plan, index) in pricingPlans" :key="index" class="pricing-card" :class="{
              'featured': plan.featured,
              'enterprise': plan.name === '企业版',
              'hovered': hoveredPricing === index
            }" @mouseenter="pricingHover(index)" @mouseleave="pricingHover(null)">

              <div class="pricing-header">
                <div class="plan-info">
                  <h3>{{ plan.name }}</h3>
                  <p v-if="plan.description" class="plan-description">{{ plan.description }}</p>
                </div>
                <div v-if="plan.featured" class="popular-badge">
                  <span>{{ plan.badge }}</span>
                </div>
              </div>

              <div class="pricing-body">
                <div class="price">
                  <span class="currency">¥</span>
                  <span class="amount">{{ billingPeriod === 'yearly' ? plan.yearlyPrice : plan.monthlyPrice }}</span>
                  <span class="period">/{{ billingPeriod === 'yearly' ? '年' : '月' }}</span>
                </div>

                <ul class="features-list">
                  <li v-for="(feature, featureIndex) in plan.features" :key="featureIndex"
                    :class="{ 'highlight': feature.highlight }">
                    <svg class="check-icon" width="20" height="20" viewBox="0 0 24 24" fill="none">
                      <path d="M7 12L10.5 15.5L17 9" stroke="currentColor" stroke-width="3" stroke-linecap="round"
                        stroke-linejoin="round" />
                    </svg>
                    <span>{{ feature.text }}</span>
                    <span v-if="feature.description" class="feature-description">{{ feature.description }}</span>
                  </li>
                </ul>
              </div>

              <div class="pricing-footer">
                <button class="pricing-btn" :class="{
                  'primary': plan.featured,
                  'secondary': !plan.featured,
                  'enterprise': plan.name === '企业版'
                }" @click="handlePlanSelection(plan)">
                  <span>{{ plan.buttonText }}</span>
                  <svg class="btn-arrow" width="16" height="16" viewBox="0 0 16 16" fill="none">
                    <path d="M6 3L11 8L6 13" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                      stroke-linejoin="round" />
                  </svg>
                </button>
              </div>
            </div>
          </div>

          <div class="pricing-note">
            <p>所有方案均包含 7 天免费试用期 • 随时可以升级或降级 • 无隐藏费用</p>
          </div>
        </div>
      </div>
    </section>

    <!-- 使用教程区域 -->
    <section id="tutorials" class="tutorials-section">
      <div class="container">
        <div class="section-header">
          <div class="section-badge">使用教程</div>
          <h2 class="section-title">快速开始使用</h2>
          <p class="section-subtitle">简单四步，轻松搭建内网穿透服务</p>
        </div>

        <div class="tutorials-grid">
          <div v-for="(step, index) in tutorialSteps" :key="index" class="tutorial-card"
            :class="{ 'active': activeStep === index }" @mouseenter="activeStep = index"
            @mouseleave="activeStep = null">
            <div class="tutorial-number">{{ index + 1 }}</div>
            <div class="tutorial-content">
              <h3>{{ step.title }}</h3>
              <p>{{ step.description }}</p>
            </div>
            <div class="tutorial-icon">
              <svg v-if="step.iconType === 'user'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="8" r="4" stroke="currentColor" stroke-width="2" />
                <path d="M12 14c-4 0-8 2-8 6v2h16v-2c0-4-4-6-8-6z" stroke="currentColor" stroke-width="2" />
              </svg>
              <svg v-else-if="step.iconType === 'download'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M12 2v16m0 0l-4-4m4 4l4-4" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                  stroke-linejoin="round" />
                <rect x="4" y="18" width="16" height="4" rx="1" stroke="currentColor" stroke-width="2" />
              </svg>
              <svg v-else-if="step.iconType === 'settings'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <circle cx="12" cy="12" r="3" stroke="currentColor" stroke-width="2" />
                <path d="M12 1v6m0 6v6m11-7h-6m-6 0H1" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
                <path d="M20.5 7.5L16 12l4.5 4.5M3.5 7.5L8 12l-4.5 4.5" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              <svg v-else-if="step.iconType === 'rocket'" width="24" height="24" viewBox="0 0 24 24" fill="none">
                <path d="M12 2l3 7h7l-5 5 1 7-6-4-6 4 1-7-5-5h7l3-7z" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round" />
              </svg>
              <span v-else class="icon-emoji">{{ step.emoji }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 关于我们区域 -->
    <section id="about" class="about-section">
      <div class="container">
        <div class="section-header">
          <div class="section-badge">关于我们</div>
          <h2 class="section-title">专业的内网穿透服务提供商</h2>
          <p class="section-subtitle">致力于为用户提供稳定、快速、安全的内网穿透解决方案</p>
        </div>

        <div class="about-content">
          <div class="about-text">
            <p>ZyroFrp 是一个专业的 FRP 内网穿透服务平台，基于开源项目 frp 开发，为个人开发者和企业用户提供高效稳定的内网穿透服务。</p>
            <p>我们拥有多年的网络服务经验，技术团队持续优化服务性能，确保用户获得最佳的使用体验。</p>
          </div>

          <div class="stats-grid">
            <div class="stat-card">
              <div class="stat-icon">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
                  <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round" />
                  <circle cx="9" cy="7" r="4" stroke="currentColor" stroke-width="2" />
                  <path d="M23 21v-2a4 4 0 0 0-3-3.87" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                    stroke-linejoin="round" />
                  <path d="M16 3.13a4 4 0 0 1 0 7.75" stroke="currentColor" stroke-width="2" stroke-linecap="round"
                    stroke-linejoin="round" />
                </svg>
              </div>
              <div class="stat-number">10K+</div>
              <div class="stat-label">活跃用户</div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
                  <path d="M13 2L3 14h9l-1 8 10-12h-9l1-8z" stroke="currentColor" stroke-width="2"
                    stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </div>
              <div class="stat-number">99.9%</div>
              <div class="stat-label">服务可用性</div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
                  <circle cx="12" cy="12" r="10" stroke="currentColor" stroke-width="2" />
                  <path d="M2 12h20" stroke="currentColor" stroke-width="2" />
                  <path d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"
                    stroke="currentColor" stroke-width="2" />
                </svg>
              </div>
              <div class="stat-number">50+</div>
              <div class="stat-label">全球节点</div>
            </div>
            <div class="stat-card">
              <div class="stat-icon">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none">
                  <path
                    d="M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"
                    stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                </svg>
              </div>
              <div class="stat-number">24/7</div>
              <div class="stat-label">技术支持</div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 页脚 -->
    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h3>ZyroFrp</h3>
            <p>专业的 FRP 内网穿透服务</p>
          </div>
          <div class="footer-section">
            <h4>产品</h4>
            <ul>
              <li><a href="#features">功能特色</a></li>
              <li><a href="#pricing">价格方案</a></li>
              <li><a href="#tutorials">使用教程</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>支持</h4>
            <ul>
              <li><a href="#">帮助文档</a></li>
              <li><a href="#">联系我们</a></li>
              <li><a href="#">服务状态</a></li>
            </ul>
          </div>
          <div class="footer-section">
            <h4>法律</h4>
            <ul>
              <li><a href="#">服务条款</a></li>
              <li><a href="#">隐私政策</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div class="footer-bottom">
        <p>&copy; 2024 ZyroFrp. 保留所有权利。</p>
      </div>
    </footer>
  </div>
</template>

<script>
export default {
  name: 'LandingPage',
  data() {
    return {
      isLoggedIn: false,
      hoveredFeature: null,
      hoveredPricing: null,
      activeStep: null,
      billingPeriod: 'monthly',
      isDarkMode: false,

      // 核心功能数据
      coreFeatures: [
        {
          title: '高速连接',
          description: '多节点负载均衡，确保数据传输的高速稳定',
          iconType: 'lightning',
          iconClass: 'icon-lightning',
          featured: false
        },
        {
          title: '安全加密',
          description: '端到端加密传输，保护您的数据安全',
          iconType: 'shield',
          iconClass: 'icon-shield',
          featured: false
        },
        {
          title: '简单易用',
          description: '可视化配置界面，一键部署，无需技术背景',
          iconType: 'settings',
          iconClass: 'icon-settings',
          featured: false
        },
        {
          title: '实时监控',
          description: '详细的流量统计和连接状态监控',
          iconType: 'chart',
          iconClass: 'icon-chart',
          featured: false
        },
        {
          title: '多协议支持',
          description: '支持 TCP、UDP、HTTP、HTTPS 等多种协议',
          iconType: 'protocol',
          iconClass: 'icon-protocol',
          featured: false
        },
        {
          title: '跨平台兼容',
          description: '支持 Windows、macOS、Linux 等主流系统',
          iconType: 'platform',
          iconClass: 'icon-platform',
          featured: false
        }
      ],

      // 教程步骤数据
      tutorialSteps: [
        {
          title: '注册账号',
          description: '创建您的 ZyroFrp 账户',
          iconType: 'user'
        },
        {
          title: '下载客户端',
          description: '获取适合您系统的客户端',
          iconType: 'download'
        },
        {
          title: '配置隧道',
          description: '在 Web 界面创建和管理隧道',
          iconType: 'settings'
        },
        {
          title: '启动服务',
          description: '运行客户端，享受内网穿透',
          iconType: 'rocket'
        }
      ],

      pricingPlans: [
        {
          name: '免费版',
          description: '适合个人开发者和小型项目',
          monthlyPrice: '0',
          yearlyPrice: '0',
          features: [
            { text: '无限流量', highlight: false },
            { text: '单隧道连接', highlight: false },
            { text: '基础技术支持', highlight: false },
            { text: 'Web 管理界面', highlight: false }
          ],
          buttonText: '开始使用',
          featured: false
        },
        {
          name: '专业版',
          description: '适合成长型团队和商业项目',
          monthlyPrice: '0',
          yearlyPrice: '0',
          features: [
            { text: '无限流量', highlight: false },
            { text: '多隧道连接', highlight: false },
            { text: '多节点负载均衡', highlight: false },
            { text: '优先技术支持', highlight: false },
            { text: '高级监控功能', highlight: false },
            { text: '自定义域名', highlight: false },
            { text: 'API 访问权限', highlight: false }
          ],
          buttonText: '立即升级',
          featured: true,
          badge: '最受欢迎'
        },
        {
          name: '企业版',
          description: '适合大型企业和高流量应用',
          monthlyPrice: '0',
          yearlyPrice: '0',
          features: [
            { text: '无限流量', highlight: false },
            { text: '无限隧道连接', highlight: false },
            { text: '专属节点资源', highlight: true, description: '独享高性能节点' },
            { text: '24/7 专属支持', highlight: false },
            { text: '完整 API 权限', highlight: false },
            { text: '私有化部署选项', highlight: true, description: '支持本地部署' },
            { text: '定制化服务', highlight: false }
          ],
          buttonText: '联系销售',
          featured: false
        }
      ]
    }
  },
  methods: {
    // 检查登录状态
    checkLoginStatus() {
      const token = localStorage.getItem('Token')
      this.isLoggedIn = !!token
    },

    // 打开 GitHub
    openGitHub() {
      window.open('https://github.com/Jieonix/ZyroFrp', '_blank')
    },

    // 跳转页面
    goToDashboard() {
      this.$router.push('/Home')
    },
    goToLogin() {
      this.$router.push('/Login')
    },
    goToRegister() {
      this.$router.push('/Register')
    },

    // 退出登录
    logout() {
      localStorage.removeItem('Token')
      this.isLoggedIn = false
      this.$router.push('/')
    },

    goTo(target) {
      // 原生滚动到锚点
      const el = document.querySelector(target)
      if (el) el.scrollIntoView({ behavior: 'smooth' })
    },

    // 功能卡片悬停效果
    featureHover(index) {
      this.hoveredFeature = index
    },

    // 处理功能卡片点击
    handleFeatureClick() {
      // 所有功能卡片都跳转到使用教程区域
      this.goTo('#tutorials')
    },

    // 价格卡片悬停效果
    pricingHover(index) {
      this.hoveredPricing = index
    },

    // 设置计费周期
    setBillingPeriod(period) {
      this.billingPeriod = period
    },


    // 滚动到顶部
    scrollToTop() {
      window.scrollTo({
        top: 0,
        behavior: 'smooth'
      })
    },

    // 处理免费试用
    handleFreeTrial() {
      // 跳转到Home页面
      this.goToDashboard()
    },

    // 处理价格方案选择
    handlePlanSelection(plan) {
      if (plan.name === '免费版') {
        // 免费版跳转到Home页面
        this.goToDashboard()
      } else if (plan.name === '专业版') {
        // 专业版跳转到支付页面
        if (this.isLoggedIn) {
          this.goToPayment()
        } else {
          this.goToLogin()
        }
      }
      // 企业版不跳转
    },

    // 跳转到支付页面
    goToPayment() {
      // 构建支付页面URL，包含计费周期和价格信息
      const params = new URLSearchParams({
        billing: this.billingPeriod,
        monthly_price: '0',
        yearly_price: '0'
      })
      this.$router.push(`/payment?${params.toString()}`)
    },

    // 初始化 Dark 模式
    initDarkMode() {
      // 检查系统主题偏好
      if (window.matchMedia) {
        this.mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
        this.isDarkMode = this.mediaQuery.matches
        this.mediaQuery.addListener(this.handleMediaQueryChange)
      }

      // 检查本地存储的主题设置
      const savedTheme = localStorage.getItem('theme')
      if (savedTheme) {
        this.isDarkMode = savedTheme === 'dark'
      }

      // 应用主题
      this.applyTheme()
    },

    // 处理系统主题变化
    handleMediaQueryChange(e) {
      this.isDarkMode = e.matches
      this.applyTheme()
    },

    // 应用主题到页面
    applyTheme() {
      const root = document.documentElement
      if (this.isDarkMode) {
        root.classList.add('dark-theme')
      } else {
        root.classList.remove('dark-theme')
      }
      localStorage.setItem('theme', this.isDarkMode ? 'dark' : 'light')
    }
  },
  mounted() {
    this.checkLoginStatus()
    this.initDarkMode()
  },
  beforeDestroy() {
    // 清理监听器
    if (this.mediaQuery) {
      this.mediaQuery.removeListener(this.handleMediaQueryChange)
    }
  }
}
</script>


<style>
.landing-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  color: #0a0a0a;
  background: #ffffff;
  line-height: 1.6;
  overflow-x: hidden;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.85);
  backdrop-filter: blur(20px) saturate(180%);
  border-bottom: 1px solid rgba(0, 0, 0, 0.08);
  z-index: 1000;
  height: 3.5rem;
  transition: all 0.3s ease;
}

.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 24px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo-icon {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  transition: all 0.3s ease;
}

.logo:hover .logo-icon {
  transform: scale(1.05);
  box-shadow: 0 8px 25px rgba(0, 122, 255, 0.15);
}

.logo h1 {
  font-size: 1.5rem;
  font-weight: 400;
  color: #006412;
  margin: 0;
  letter-spacing: -0.5px;
}

.nav-menu {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 8px;
  height: 100%;
}

.nav-item {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 16px;
  font-size: 15px;
  font-weight: 500;
  color: #6b7280;
  cursor: pointer;
  transition: all 0.2s ease;
  position: relative;
  height: 100%;
}

.nav-item:hover {
  color: #0a0a0a;
  background: #f3f4f6;
}

.nav-item::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-item:hover::after {
  width: 100%;
}

.user-actions {
  display: flex;
  align-items: center;
  height: 100%;
}

.user-logged-in,
.user-logged-out {
  display: flex;
  align-items: center;
  height: 100%;
}

/* 按钮样式 */
.github-btn {
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: transparent;
  color: #6b7280;
  border: none;
  font-size: 15px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  height: 100%;
}

.github-btn svg {
  margin-right: 0.5rem;
}

.github-btn:hover {
  background: #f3f4f6;
  color: #0a0a0a;
}

.login-btn,
.register-btn,
.dashboard-btn,
.logout-btn {
  padding: 0 20px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
  border: none;
  height: 100%;
}

.login-btn {
  background: transparent;
  color: #6b7280;
  border: 1px solid #fcfcfc;
}

.login-btn:hover {
  background: #f3f4f6;
  color: #424242;
}

.register-btn,
.dashboard-btn {
  background: transparent;
  color: #6b7280;
}

.register-btn:hover,
.dashboard-btn:hover {
  background-color: #f3f4f6;
  color: #424242;
}

.logout-btn {
  background: transparent;
  color: #6b7280;
}

.logout-btn:hover {
  background: #f3f4f6;
  color: #dc2626;
}


/* 主横幅区域 */
.hero {
  padding: 120px 0 80px;
  background: linear-gradient(135deg, #fafbfc 0%, #ffffff 100%);
  position: relative;
  overflow: hidden;
  height: 100vh;
  display: flex;
  align-items: center;
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  pointer-events: none;
}

.gradient-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(100px);
  opacity: 0.15;
  animation: float 20s ease-in-out infinite;
}

.orb-1 {
  width: 600px;
  height: 600px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  top: -200px;
  right: -200px;
  animation-delay: 0s;
}

.orb-2 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #34d399 0%, #10b981 100%);
  bottom: -100px;
  left: -100px;
  animation-delay: 5s;
  animation-direction: reverse;
}

.orb-3 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #6ee7b7 0%, #34d399 100%);
  top: 50%;
  left: 60%;
  animation-delay: 10s;
}

.noise-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  opacity: 0.03;
  background-image: url("data:image/svg+xml,%3Csvg viewBox='0 0 200 200' xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='noiseFilter'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.65' numOctaves='3' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23noiseFilter)'/%3E%3C/svg%3E");
  pointer-events: none;
}

@keyframes float {

  0%,
  100% {
    transform: translate(0, 0) rotate(0deg);
  }

  33% {
    transform: translate(30px, -30px) rotate(120deg);
  }

  66% {
    transform: translate(-20px, 20px) rotate(240deg);
  }
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  padding: 0 24px;
  position: relative;
  z-index: 2;
}

.hero-content {
  max-width: 600px;
}

.hero-badge {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 16px;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 24px;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
  animation: pulse 2s ease-in-out infinite;
}

@keyframes pulse {

  0%,
  100% {
    transform: scale(1);
  }

  50% {
    transform: scale(1.05);
  }
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 24px;
  letter-spacing: -2px;
}

.title-gradient {
  background: linear-gradient(135deg, #0a0a0a 0%, #6b7280 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: block;
  margin-bottom: 8px;
}

.title-highlight {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  display: block;
}

.hero-subtitle {
  font-size: 20px;
  color: #6b7280;
  margin-bottom: 40px;
  line-height: 1.6;
  font-weight: 400;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 60px;
  flex-wrap: wrap;
}

/* CTA 按钮样式 */
.cta-primary,
.cta-secondary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 16px 32px;
  border-radius: 16px;
  font-size: 16px;
  font-weight: 600;
  text-decoration: none;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.cta-primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
}

.cta-primary:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 35px rgba(16, 185, 129, 0.4);
}

.cta-secondary {
  background: white;
  color: #0a0a0a;
  border: 2px solid #e5e7eb;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.cta-secondary:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
  border-color: #d1d5db;
}

.btn-arrow,
.btn-play {
  transition: transform 0.3s ease;
}

.cta-primary:hover .btn-arrow {
  transform: translateX(4px);
  filter: drop-shadow(0 0 8px rgba(16, 185, 129, 0.6));
}

.cta-secondary:hover .btn-play {
  transform: scale(1.1);
  filter: drop-shadow(0 0 6px rgba(16, 185, 129, 0.4));
}

.trust-indicators {
  display: flex;
  gap: 48px;
  align-items: center;
}

.trust-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.trust-icon {
  width: 40px;
  height: 40px;
  background: #f3f4f6;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #10b981;
}

.trust-number {
  font-size: 24px;
  font-weight: 800;
  color: #0a0a0a;
  line-height: 1;
  margin-bottom: 4px;
}

.trust-label {
  font-size: 14px;
  color: #6b7280;
  font-weight: 500;
}

/* Hero 视觉元素 */
.hero-visual {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 24px;
}

.terminal-window {
  background: #1e1e1e;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.25);
  width: 100%;
  max-width: 500px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  animation: terminalFloat 6s ease-in-out infinite;
}

@keyframes terminalFloat {

  0%,
  100% {
    transform: translateY(0px);
  }

  50% {
    transform: translateY(-10px);
  }
}

.terminal-header {
  background: #2d2d2d;
  padding: 12px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.terminal-controls {
  display: flex;
  gap: 8px;
}

.control {
  width: 12px;
  height: 12px;
  border-radius: 50%;
}

.control-red {
  background: #ff5f56;
}

.control-yellow {
  background: #ffbd2e;
}

.control-green {
  background: #27c93f;
}

.terminal-title {
  color: #888888;
  font-size: 14px;
  font-family: 'SF Mono', 'Monaco', 'Inconsolata', 'Fira Code', monospace;
}

.terminal-body {
  padding: 20px;
  font-family: 'SF Mono', 'Monaco', 'Inconsolata', 'Fira Code', monospace;
  font-size: 14px;
  line-height: 1.6;
  background: #1e1e1e;
}

.terminal-line {
  margin-bottom: 8px;
  opacity: 0;
  animation: fadeInUp 0.5s ease-out forwards;
}

.terminal-line:nth-child(1) {
  animation-delay: 0.5s;
}

.terminal-line:nth-child(2) {
  animation-delay: 1s;
}

.terminal-line:nth-child(3) {
  animation-delay: 1.5s;
}

.terminal-line:nth-child(4) {
  animation-delay: 2s;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.prompt {
  color: #27c93f;
  margin-right: 8px;
}

.command {
  color: #ffffff;
}

.output {
  color: #888888;
}

.success {
  color: #27c93f;
}

.info {
  color: #54c7ec;
}

.terminal-cursor {
  display: inline-block;
  width: 8px;
  height: 16px;
  background: #ffffff;
  animation: cursorBlink 1s infinite;
}

@keyframes cursorBlink {

  0%,
  50% {
    opacity: 1;
  }

  51%,
  100% {
    opacity: 0;
  }
}

.floating-cards {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.float-card {
  position: absolute;
  background: white;
  border-radius: 12px;
  padding: 10px 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.08);
  border: 1px solid rgba(0, 0, 0, 0.05);
  backdrop-filter: blur(10px);
  animation: cardFloat 8s ease-in-out infinite;
}

.card-1 {
  top: 15%;
  right: -10px;
  animation-delay: 0s;
}

.card-2 {
  top: 55%;
  left: -30px;
  animation-delay: 2s;
}

.card-3 {
  bottom: 15%;
  right: -2rem;
  animation-delay: 4s;
}

@keyframes cardFloat {

  0%,
  100% {
    transform: translateY(0px) rotate(0deg);
  }

  25% {
    transform: translateY(-3px) rotate(0.5deg);
  }

  75% {
    transform: translateY(3px) rotate(-0.5deg);
  }
}

/* 浮动卡片悬停效果 */
.float-card:hover .card-icon {
  transform: scale(1.05);
  color: #059669;
}

.float-card:hover .card-icon svg {
  transform: scale(1.05);
  filter: drop-shadow(0 0 6px rgba(16, 185, 129, 0.3));
}

.card-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 20px;
  height: 20px;
  color: #10b981;
  transition: all 0.3s ease;
}

.card-icon svg {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.card-label {
  font-size: 12px;
  font-weight: 600;
  color: #0a0a0a;
}

/* 区域标题样式 */
.section-header {
  text-align: center;
  margin-bottom: 3rem;
}

.section-badge {
  display: inline-flex;
  align-items: center;
  padding: 6px 16px;
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  color: #6b7280;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 16px;
  border: 1px solid rgba(0, 0, 0, 0.05);
}

.section-title {
  font-size: 3rem;
  font-weight: 800;
  color: #0a0a0a;
  margin-bottom: 16px;
  letter-spacing: -1px;
  line-height: 1.1;
}

.section-subtitle {
  font-size: 18px;
  color: #6b7280;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.6;
}

/* 功能特色区域 */
.features-section {
  padding: 120px 0;
  background: linear-gradient(135deg, #ffffff 0%, #fafbfc 100%);
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
}

/* 确保功能特色区域的内容垂直排列 */
.features-section .container {
  display: flex;
  flex-direction: column;
  gap: 60px;
  justify-content: center;
  flex: 1;
}

.features-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 30% 20%, rgba(0, 122, 255, 0.03) 0%, transparent 50%),
    radial-gradient(circle at 70% 80%, rgba(88, 86, 214, 0.03) 0%, transparent 50%);
  pointer-events: none;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 24px;
  position: relative;
  z-index: 2;
}

.feature-card {
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 20px;
  padding: 32px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05) 0%, rgba(88, 86, 214, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.feature-card:hover::before {
  opacity: 1;
}

.feature-card:hover,
.feature-card.featured {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 122, 255, 0.2);
}

.feature-content {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  position: relative;
  z-index: 2;
}

.feature-icon {
  width: 56px;
  height: 56px;
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #10b981;
  flex-shrink: 0;
  transition: all 0.3s ease;
}

.feature-icon svg {
  width: 24px;
  height: 24px;
  color: inherit;
}

.icon-emoji {
  font-size: 24px;
  line-height: 1;
}

.feature-card:hover .feature-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  transform: scale(1.05);
}

.feature-text h3 {
  font-size: 20px;
  font-weight: 700;
  color: #0a0a0a;
  margin-bottom: 8px;
  letter-spacing: -0.5px;
}

.feature-text p {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0;
}

.feature-arrow {
  position: absolute;
  top: 32px;
  right: 32px;
  color: #d1d5db;
  transition: all 0.3s ease;
}

.feature-card:hover .feature-arrow {
  color: #10b981;
  transform: translateX(4px);
}

.feature-showcase {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 80px;
  align-items: center;
  position: relative;
  z-index: 2;
}

.showcase-left {
  display: flex;
  align-items: center;
}

.showcase-content h3 {
  font-size: 2rem;
  font-weight: 800;
  color: #0a0a0a;
  margin-bottom: 16px;
  letter-spacing: -1px;
}

.showcase-content p {
  font-size: 18px;
  color: #6b7280;
  margin-bottom: 32px;
  line-height: 1.6;
}

.showcase-features {
  list-style: none;
  padding: 0;
  margin: 0;
}

.showcase-features li {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 16px;
  color: #0a0a0a;
  font-weight: 500;
  margin-bottom: 12px;
}

.showcase-features svg {
  color: #007AFF;
  flex-shrink: 0;
}

.showcase-visual {
  display: flex;
  justify-content: center;
  align-items: center;
}

.network-diagram {
  position: relative;
  width: 300px;
  height: 300px;
}

.node {
  position: absolute;
  background: white;
  border: 2px solid #e5e7eb;
  border-radius: 16px;
  padding: 12px 16px;
  font-size: 14px;
  font-weight: 600;
  color: #6b7280;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
}

.node.central {
  top: 55%;
  left: 34%;
  transform: translate(-50%, -50%);
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  border-color: #10b981;
  z-index: 2;
}

.node1 {
  top: 11.4%;
  left: -11.2%;
}

.node2 {
  top: 11.4%;
  right: 21.2%;
}

.node3 {
  bottom: 7%;
  left: 33.5%;
  transform: translateX(-50%);
}

.connection {
  position: absolute;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  height: 2px;
  transform-origin: left center;
  animation: pulse 2s ease-in-out infinite;
}

.conn1 {
  top: 50%;
  left: 50%;
  width: 100px;
  transform: translate(-50%, -50%) rotate(-135deg);
}

.conn2 {
  top: 50%;
  left: 50%;
  width: 100px;
  transform: translate(-50%, -50%) rotate(-45deg);
}

.conn3 {
  top: 50%;
  left: 47%;
  width: 80px;
  transform: translate(-50%, -50%) rotate(90deg);
}

@keyframes pulse {

  0%,
  100% {
    opacity: 0.3;
  }

  50% {
    opacity: 1;
  }
}

/* 价格方案区域 */
.pricing-section {
  padding: 120px 0;
  background: linear-gradient(135deg, #fafbfc 0%, #ffffff 100%);
  position: relative;
  overflow: hidden;
  height: 100vh;
  display: flex;
  align-items: center;
}

/* 价格方案区域容器样式 */
.pricing-section .container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex: 1;
}

/* 价格内容区域垂直布局 */
.pricing-section .pricing-content {
  display: flex !important;
  flex-direction: column !important;
  align-items: center !important;
  gap: 40px !important;
}

.pricing-toggle {
  display: flex;
  justify-content: center;
  gap: 4px;
  background: #f3f4f6;
  padding: 4px;
  border-radius: 16px;
  width: fit-content;
  margin: 0 auto 0.2rem auto;
}

/* 强制价格网格布局 - 最高优先级 */
.pricing-section .container .pricing-grid {
  display: grid !important;
  grid-template-columns: repeat(3, 1fr) !important;
  gap: 24px !important;
  max-width: 1200px !important;
  margin: 0 auto 1rem !important;
  align-items: start !important;
}

.toggle-btn {
  padding: 10px 24px;
  border-radius: 12px;
  border: none;
  background: transparent;
  color: #6b7280;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  white-space: nowrap;
}

.toggle-btn.active {
  background: white;
  color: #10b981;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.discount {
  background: #10b981;
  color: white;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  margin-left: 6px;
  font-weight: 700;
}


.pricing-card {
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.08);
  border-radius: 20px;
  padding: 32px 24px;
  position: relative;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  overflow: hidden;
  display: flex;
  flex-direction: column;
  height: 100%;
  min-height: 520px;
  justify-content: space-between;
}

.pricing-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.03) 0%, rgba(88, 86, 214, 0.03) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.pricing-card:hover::before {
  opacity: 1;
}

.pricing-card:hover,
.pricing-card.hovered {
  transform: translateY(-8px);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  border-color: rgba(0, 122, 255, 0.2);
}

.pricing-card.featured {
  border-color: #10b981;
  box-shadow: 0 10px 30px rgba(16, 185, 129, 0.2);
  transform: scale(1.02);
}

.pricing-card.featured:hover,
.pricing-card.featured.hovered {
  transform: scale(1.05) translateY(-8px);
  box-shadow: 0 20px 40px rgba(16, 185, 129, 0.3);
}

.pricing-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
  flex-shrink: 0;
  min-height: 60px;
}

.plan-info h3 {
  font-size: 20px;
  font-weight: 700;
  color: #0a0a0a;
  margin-bottom: 4px;
  letter-spacing: -0.5px;
}

.plan-description {
  font-size: 14px;
  color: #6b7280;
  margin: 0;
  line-height: 1.4;
}

.popular-badge {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
  flex-shrink: 0;
}

.price {
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
  margin-bottom: 32px;
  min-height: 50px;
}

.currency {
  font-size: 18px;
  font-weight: 600;
  color: #6b7280;
}

.amount {
  font-size: 42px;
  font-weight: 800;
  color: #0a0a0a;
  line-height: 1;
}

.period {
  font-size: 16px;
  font-weight: 500;
  color: #6b7280;
}

.features-list {
  list-style: none;
  padding: 0;
  margin: 0 0 24px;
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.features-list li {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 12px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
  font-size: 14px;
  color: #0a0a0a;
  line-height: 1.4;
  min-height: 20px;
}

.features-list li:last-child {
  border-bottom: none;
}

.features-list li.highlight {
  font-weight: 600;
  color: #10b981;
}

.features-list li {
  transition: all 0.3s ease;
}

.check-icon {
  width: 20px;
  height: 20px;
  background: #e5e7eb;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #6b7280;
  flex-shrink: 0;
  margin-top: 2px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
}

/* 移除单独的check-icon hover效果，只有pricing-card悬停时才变绿 */

/* pricing-card悬停时check-icon变绿 */
.pricing-card:hover .check-icon {
  background: #13e39e;
  color: #ffffff;
  transform: scale(1.05);
  box-shadow: 0 4px 16px rgba(55, 65, 81, 0.25);
}

/* pricing-card悬停时，再次悬停li元素有更强效果 */
.pricing-card:hover .features-list li:hover .check-icon {
  background: #1f2937;
  color: #059669;
  transform: scale(1.1);
  box-shadow: 0 6px 20px rgba(31, 41, 55, 0.35);
}

/* 添加对勾绘制动画效果 */
.pricing-card:hover .check-icon path {
  transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
  stroke-dasharray: 20;
  stroke-dashoffset: 0;
  animation: drawCheckmark 0.4s ease-in-out;
}

@keyframes drawCheckmark {
  0% {
    stroke-dasharray: 0 20;
    opacity: 0.3;
    transform: scale(0.7) rotate(-10deg);
  }

  50% {
    opacity: 0.7;
    transform: scale(1.1) rotate(5deg);
  }

  100% {
    stroke-dasharray: 20 20;
    opacity: 1;
    transform: scale(1) rotate(0deg);
  }
}

/* 移除SVG路径的单独hover效果 */

/* 添加更强的hover效果 */
.check-icon.animated-hover {
  animation: pulse-icon 2s infinite;
}

@keyframes pulse-icon {
  0% {
    transform: scale(1);
    filter: drop-shadow(0 0 0 rgba(16, 185, 129, 0));
  }

  50% {
    transform: scale(1.05);
    filter: drop-shadow(0 0 8px rgba(16, 185, 129, 0.4));
  }

  100% {
    transform: scale(1);
    filter: drop-shadow(0 0 0 rgba(16, 185, 129, 0));
  }
}

.feature-description {
  display: block;
  font-size: 12px;
  color: #6b7280;
  font-weight: 400;
  margin-top: 3px;
  line-height: 1.3;
}

.pricing-body {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.pricing-footer {
  margin-top: auto;
  padding-top: 16px;
}

.pricing-btn {
  width: 100%;
  padding: 14px 20px;
  border-radius: 14px;
  font-size: 15px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: none;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.pricing-btn .btn-arrow {
  transition: all 0.3s ease;
  opacity: 0.7;
}

.pricing-btn.primary {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  box-shadow: 0 8px 25px rgba(16, 185, 129, 0.3);
}

.pricing-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 35px rgba(16, 185, 129, 0.4);
}

.pricing-btn.primary:hover .btn-arrow {
  opacity: 1;
  transform: translateX(2px);
  filter: drop-shadow(0 0 8px rgba(16, 185, 129, 0.6));
}

.pricing-btn.secondary {
  background: #f3f4f6;
  color: #0a0a0a;
  border: 2px solid #e5e7eb;
}

.pricing-btn.secondary:hover {
  background: #e5e7eb;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1);
}

.pricing-btn.secondary:hover .btn-arrow {
  opacity: 1;
  transform: translateX(2px);
  color: #10b981;
  filter: drop-shadow(0 0 6px rgba(16, 185, 129, 0.5));
}

.pricing-btn.enterprise {
  background: #f3f4f6;
  color: #0a0a0a;
}

.pricing-btn.enterprise:hover {
  transform: translateY(-2px);
  background-color: #e6e7eb;
  box-shadow: 0 8px 25px rgba(107, 114, 128, 0.3);
}

.pricing-btn.enterprise:hover .btn-arrow {
  opacity: 1;
  transform: translateX(2px);
  color: #6b7280;
  filter: drop-shadow(0 0 6px rgba(107, 114, 128, 0.5));
}

.pricing-note {
  text-align: center;
  color: #6b7280;
  font-size: 14px;
  max-width: 600px;
  margin: 0 auto;
}

.pricing-note p {
  margin: 0;
}

/* 使用教程区域 */
.tutorials-section {
  padding: 120px 0;
  background: linear-gradient(135deg, #ffffff 0%, #fafbfc 100%);
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
}

.tutorials-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 70% 20%, rgba(0, 122, 255, 0.03) 0%, transparent 50%),
    radial-gradient(circle at 30% 80%, rgba(88, 86, 214, 0.03) 0%, transparent 50%);
  pointer-events: none;
}

/* 使用教程区域容器样式 */
.tutorials-section .container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex: 1;
}

.tutorials-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
  position: relative;
  z-index: 2;
}

.tutorial-card {
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 20px;
  padding: 32px 24px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.tutorial-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(0, 122, 255, 0.05) 0%, rgba(88, 86, 214, 0.05) 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.tutorial-card:hover::before,
.tutorial-card.active::before {
  opacity: 1;
}

.tutorial-card:hover,
.tutorial-card.active {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 122, 255, 0.2);
}

.tutorial-number {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 800;
  color: #10b981;
  margin-bottom: 20px;
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;
}

.tutorial-card:hover .tutorial-number,
.tutorial-card.active .tutorial-number {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  transform: scale(1.1);
}

.tutorial-content h3 {
  font-size: 20px;
  font-weight: 700;
  color: #0a0a0a;
  margin-bottom: 12px;
  letter-spacing: -0.5px;
}

.tutorial-content p {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0;
}

.tutorial-icon {
  margin-top: 20px;
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #f3f4f6 0%, #e5e7eb 100%);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #10b981;
  transition: all 0.3s ease;
}

.tutorial-card:hover .tutorial-icon,
.tutorial-card.active .tutorial-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
  transform: scale(1.05);
}

/* 关于我们区域 */
.about-section {
  padding: 120px 0;
  background: linear-gradient(135deg, #fafbfc 0%, #ffffff 100%);
  position: relative;
  height: 100vh;
  display: flex;
  align-items: center;
}

.about-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, rgba(0, 122, 255, 0.02) 0%, transparent 70%);
  pointer-events: none;
}

/* 关于我们区域容器样式 */
.about-section .container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  flex: 1;
}

.about-content {
  text-align: center;
  max-width: 1000px;
  margin: 0 auto;
  position: relative;
  z-index: 2;
}

.about-text {
  max-width: 600px;
  margin: 0 auto 60px;
}

.about-text p {
  font-size: 18px;
  color: #6b7280;
  line-height: 1.8;
  margin-bottom: 20px;
}

.about-text p:last-child {
  margin-bottom: 0;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 32px;
  margin-top: 0;
  width: 100%;
}

.stat-card {
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.06);
  border-radius: 20px;
  padding: 40px 24px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  text-align: center;
  min-height: 180px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.stat-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  border-color: rgba(0, 122, 255, 0.2);
}

.stat-icon {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #10b981;
}

.stat-icon svg {
  width: 40px;
  height: 40px;
}

.stat-number {
  font-size: 36px;
  font-weight: 800;
  color: #0a0a0a;
  margin-bottom: 12px;
  line-height: 1;
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.stat-label {
  font-size: 16px;
  color: #6b7280;
  font-weight: 500;
  line-height: 1.4;
}

/* 页脚 */
.footer {
  background: #f8fafc;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  display: flex;
  flex-direction: column;
  min-height: 200px;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  gap: 48px;
  padding-top: 5rem;
  padding-bottom: 2rem;
}

.footer-section {
  flex: 1;
  min-width: 200px;
}

.footer-section:nth-child(1) {
  margin-top: 2rem;
  margin-right: 6rem;
}

.footer-section h3 {
  font-size: 20px;
  font-weight: 700;
  color: #0a0a0a;
  margin-bottom: 16px;
  letter-spacing: -0.5px;
}

.footer-section h4 {
  font-size: 16px;
  font-weight: 600;
  color: #0a0a0a;
  margin-bottom: 16px;
}

.footer-section p {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin-bottom: 24px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  margin-bottom: 12px;
}

.footer-section a {
  color: #6b7280;
  text-decoration: none;
  transition: color 0.2s ease;
  font-size: 15px;
}

.footer-section a:hover {
  color: #007AFF;
}

.footer-bottom {
  text-align: center;
  padding: 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
  color: #6b7280;
  font-size: 14px;
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 平滑滚动 */
html {
  scroll-behavior: smooth;
}

/* 通用容器 */
.container {
  max-width: 100rem;
  margin: 0 auto;
  padding: 0 24px;
  position: relative;
  z-index: 2;
}

/* 响应式设计 */
@media (max-width: 1024px) {
  .hero-container {
    grid-template-columns: 1fr;
    gap: 60px;
    text-align: center;
  }

  .hero-content {
    max-width: 600px;
    margin: 0 auto;
  }

  .hero-title {
    font-size: 3rem;
  }

  .trust-indicators {
    justify-content: center;
    flex-wrap: wrap;
  }

  .feature-showcase {
    grid-template-columns: 1fr;
    gap: 60px;
  }

  .showcase-visual {
    order: -1;
  }

  .pricing-section .pricing-grid {
    grid-template-columns: repeat(2, 1fr) !important;
    gap: 20px;
    max-width: 800px;
  }

  .pricing-card.featured {
    transform: none;
  }

  .pricing-card.featured:hover {
    transform: translateY(-8px);
  }

  .tutorials-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
  }
}

@media (max-width: 768px) {
  .navbar {
    height: 3rem;
  }

  /* 移动端优化check-icon效果 */
  .features-list li:active .check-icon {
    transform: scale(1.05);
    filter: drop-shadow(0 0 4px rgba(16, 185, 129, 0.4));
    background: linear-gradient(135deg, #059669 0%, #047857 100%);
  }

  .features-list li:active span {
    color: #10b981;
  }

  .nav-container {
    padding: 0 16px;
  }

  .nav-menu {
    display: none;
  }

  .user-actions {
    gap: 8px;
  }

  .github-btn span {
    display: none;
  }

  .hero {
    padding: 80px 0 60px;
    height: 100vh;
  }

  .hero-container {
    padding: 0 16px;
  }

  .hero-title {
    font-size: 2.5rem;
  }

  .hero-subtitle {
    font-size: 18px;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
    gap: 12px;
  }

  .cta-primary,
  .cta-secondary {
    width: 100%;
    max-width: 300px;
    justify-content: center;
  }

  .trust-indicators {
    flex-direction: column;
    gap: 24px;
  }

  .tutorials-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .tutorial-card {
    padding: 24px 20px;
  }

  .tutorial-number {
    width: 50px;
    height: 50px;
    font-size: 20px;
  }

  .tutorial-content h3 {
    font-size: 18px;
  }

  .tutorial-content p {
    font-size: 15px;
  }

  .about-text {
    margin-bottom: 40px;
  }

  .about-text p {
    font-size: 16px;
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 24px;
  }

  .stat-card {
    padding: 32px 20px;
    min-height: 160px;
  }

  .stat-icon {
    font-size: 32px;
    margin-bottom: 16px;
  }

  .stat-number {
    font-size: 28px;
    margin-bottom: 10px;
  }

  .stat-label {
    font-size: 14px;
  }

  /* 小屏幕额外样式 */
  .tutorials-section {
    padding: 80px 0;
  }

  .tutorial-card {
    padding: 20px 16px;
  }

  .tutorial-number {
    width: 45px;
    height: 45px;
    font-size: 18px;
  }

  .tutorial-content h3 {
    font-size: 16px;
  }

  .tutorial-content p {
    font-size: 14px;
  }

  .tutorial-icon {
    width: 40px;
    height: 40px;
  }

  .about-section {
    padding: 80px 0;
  }

  .about-text p {
    font-size: 15px;
  }

  .stats-grid {
    grid-template-columns: 1fr;
    gap: 20px;
    margin-top: 40px;
  }

  .stat-card {
    padding: 32px 20px;
    min-height: 140px;
  }

  .stat-icon {
    font-size: 28px;
    margin-bottom: 14px;
  }

  .stat-number {
    font-size: 24px;
    margin-bottom: 8px;
  }

  .stat-label {
    font-size: 13px;
  }

  .footer-content {
    gap: 32px;
    padding: 40px 16px 32px;
  }

  .terminal-window {
    max-width: 100%;
  }

  .floating-cards {
    display: none;
  }

  .section-header {
    margin-bottom: 60px;
  }

  .section-title {
    font-size: 2rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .features-section .container {
    gap: 40px;
  }

  .features-section {
    height: 100vh;
  }

  .pricing-section {
    height: 100vh;
  }

  .tutorials-section {
    height: 100vh;
  }

  .about-section {
    height: 100vh;
  }

  .feature-card {
    padding: 24px;
  }

  .showcase-content h3 {
    font-size: 1.5rem;
  }

  .network-diagram {
    width: 250px;
    height: 250px;
  }

  .pricing-toggle {
    flex-direction: column;
    align-items: center;
    gap: 12px;
  }

  .toggle-btn {
    width: 100%;
    max-width: 300px;
  }

  .pricing-section .pricing-grid {
    grid-template-columns: 1fr !important;
    gap: 20px;
    max-width: 500px;
  }

  .pricing-card {
    padding: 32px 24px;
  }

  .amount {
    font-size: 36px;
  }
}

@media (max-width: 480px) {
  .hero-title {
    font-size: 2rem;
  }

  .hero-subtitle {
    font-size: 16px;
  }

  .section-title {
    font-size: 1.75rem;
  }

  .feature-card {
    padding: 20px;
  }

  .feature-icon {
    width: 48px;
    height: 48px;
  }

  .feature-text h3 {
    font-size: 18px;
  }

  .feature-text p {
    font-size: 15px;
  }

  .pricing-card {
    padding: 24px 20px;
  }

  .amount {
    font-size: 32px;
  }

  .currency {
    font-size: 18px;
  }

  .period {
    font-size: 16px;
  }

  .features-list li {
    font-size: 15px;
  }

  .pricing-btn {
    padding: 14px 20px;
    font-size: 15px;
  }
}

/* 极小屏幕处理 */
@media (max-width: 480px) {
  .footer-content {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .footer-section {
    min-width: auto;
    width: 100%;
  }
}

/* 移动端导航菜单按钮 */
.mobile-menu-btn {
  display: none;
  background: transparent;
  border: none;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: background 0.2s ease;
}

.mobile-menu-btn:hover {
  background: #f3f4f6;
}

@media (max-width: 768px) {
  .mobile-menu-btn {
    display: block;
  }
}


/* 选择文本样式 */
::selection {
  background: rgba(16, 185, 129, 0.2);
  color: #0a0a0a;
}

/* 焦点样式 */
button:focus-visible,
a:focus-visible {
  outline: 2px solid #10b981;
  outline-offset: 2px;
  border-radius: 4px;
}

/* 减少动画偏好设置 */
@media (prefers-reduced-motion: reduce) {

  *,
  *::before,
  *::after {
    animation-duration: 0.01ms !important;
    animation-iteration-count: 1 !important;
    transition-duration: 0.01ms !important;
  }
}

/* Dark 模式样式 */
:root.dark-theme .landing-page {
  color: #f0f0f0;
  background: #0a0a0a;
}

:root.dark-theme .navbar {
  background: rgba(10, 10, 10, 0.85);
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
}

:root.dark-theme .logo h1 {
  color: #10b981;
}

:root.dark-theme .nav-item {
  color: #a0a0a0;
}

:root.dark-theme .nav-item:hover {
  color: #f0f0f0;
  background: #1a1a1a;
}

:root.dark-theme .github-btn,
:root.dark-theme .login-btn,
:root.dark-theme .register-btn,
:root.dark-theme .dashboard-btn,
:root.dark-theme .logout-btn {
  color: #a0a0a0;
}

:root.dark-theme .github-btn:hover,
:root.dark-theme .login-btn:hover,
:root.dark-theme .register-btn:hover,
:root.dark-theme .dashboard-btn:hover,
:root.dark-theme .logout-btn:hover {
  background: #1a1a1a;
  color: #f0f0f0;
}

:root.dark-theme .hero {
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
}

:root.dark-theme .hero-badge {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

:root.dark-theme .title-gradient {
  background: linear-gradient(135deg, #f0f0f0 0%, #a0a0a0 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

:root.dark-theme .title-highlight {
  background: linear-gradient(135deg, #34d399 0%, #10b981 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

:root.dark-theme .hero-subtitle {
  color: #a0a0a0;
}

:root.dark-theme .cta-secondary {
  background: #1a1a1a;
  color: #f0f0f0;
  border: 2px solid #333333;
}

:root.dark-theme .cta-secondary:hover {
  background: #2a2a2a;
  border-color: #404040;
}

:root.dark-theme .trust-icon {
  background: #1a1a1a;
  color: #34d399;
}

:root.dark-theme .trust-number {
  color: #f0f0f0;
}

:root.dark-theme .trust-label {
  color: #a0a0a0;
}

/* Dark 模式终端窗口 - 白色主题 */
:root.dark-theme .terminal-window {
  background: #ffffff;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.3);
}

:root.dark-theme .terminal-header {
  background: #f8f9fa;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

:root.dark-theme .terminal-title {
  color: #666666;
}

:root.dark-theme .terminal-body {
  background: #ffffff;
}

:root.dark-theme .prompt {
  color: #10b981;
}

:root.dark-theme .command {
  color: #1a1a1a;
}

:root.dark-theme .output {
  color: #666666;
}

:root.dark-theme .success {
  color: #059669;
}

:root.dark-theme .info {
  color: #0ea5e9;
}

:root.dark-theme .terminal-cursor {
  background: #1a1a1a;
}

:root.dark-theme .float-card {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

:root.dark-theme .card-icon {
  color: #34d399;
}

:root.dark-theme .float-card:hover .card-icon {
  color: #10b981;
}

:root.dark-theme .float-card:hover .card-icon svg {
  filter: drop-shadow(0 0 8px rgba(52, 211, 153, 0.4));
}

:root.dark-theme .card-label {
  color: #f0f0f0;
}

:root.dark-theme .section-badge {
  background: linear-gradient(135deg, #1a1a1a 0%, #2a2a2a 100%);
  color: #a0a0a0;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

:root.dark-theme .section-title {
  color: #f0f0f0;
}

:root.dark-theme .section-subtitle {
  color: #a0a0a0;
}

:root.dark-theme .features-section {
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
}

:root.dark-theme .feature-card {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

:root.dark-theme .feature-card:hover {
  background: #2a2a2a;
  border-color: rgba(16, 185, 129, 0.3);
}

:root.dark-theme .feature-icon {
  background: linear-gradient(135deg, #2a2a2a 0%, #3a3a3a 100%);
  color: #34d399;
}

:root.dark-theme .feature-card:hover .feature-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

:root.dark-theme .feature-text h3 {
  color: #f0f0f0;
}

:root.dark-theme .feature-text p {
  color: #a0a0a0;
}

:root.dark-theme .showcase-content h3 {
  color: #f0f0f0;
}

:root.dark-theme .showcase-content p {
  color: #a0a0a0;
}

:root.dark-theme .showcase-features li {
  color: #f0f0f0;
}

:root.dark-theme .node {
  background: #1a1a1a;
  border: 2px solid #333333;
  color: #a0a0a0;
}

:root.dark-theme .pricing-section {
  background: linear-gradient(135deg, #1a1a1a 0%, #0a0a0a 100%);
}

:root.dark-theme .pricing-toggle {
  background: #2a2a2a;
}

:root.dark-theme .toggle-btn {
  color: #a0a0a0;
}

:root.dark-theme .toggle-btn.active {
  background: #1a1a1a;
  color: #34d399;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

:root.dark-theme .pricing-card {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.08);
}

:root.dark-theme .pricing-card:hover {
  background: #2a2a2a;
  border-color: rgba(16, 185, 129, 0.3);
}

:root.dark-theme .plan-info h3 {
  color: #f0f0f0;
}

:root.dark-theme .plan-description {
  color: #a0a0a0;
}

:root.dark-theme .amount {
  color: #f0f0f0;
}

:root.dark-theme .currency,
:root.dark-theme .period {
  color: #a0a0a0;
}

:root.dark-theme .features-list li {
  color: #f0f0f0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.06);
}

:root.dark-theme .features-list li.highlight {
  color: #34d399;
}

:root.dark-theme .check-icon {
  background: #333333;
  color: #a0a0a0;
}

:root.dark-theme .pricing-card:hover .check-icon {
  background: #34d399;
  color: #ffffff;
}

:root.dark-theme .pricing-btn.secondary {
  background: #2a2a2a;
  color: #f0f0f0;
  border: 2px solid #333333;
}

:root.dark-theme .pricing-btn.secondary:hover {
  background: #3a3a3a;
  border-color: #404040;
}

:root.dark-theme .pricing-btn.enterprise {
  background: #2a2a2a;
  color: #f0f0f0;
}

:root.dark-theme .pricing-btn.enterprise:hover {
  background: #3a3a3a;
}

:root.dark-theme .pricing-note {
  color: #a0a0a0;
}

:root.dark-theme .tutorials-section {
  background: linear-gradient(135deg, #0a0a0a 0%, #1a1a1a 100%);
}

:root.dark-theme .tutorial-card {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

:root.dark-theme .tutorial-card:hover {
  background: #2a2a2a;
  border-color: rgba(16, 185, 129, 0.3);
}

:root.dark-theme .tutorial-number {
  background: linear-gradient(135deg, #2a2a2a 0%, #3a3a3a 100%);
  color: #34d399;
}

:root.dark-theme .tutorial-card:hover .tutorial-number {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

:root.dark-theme .tutorial-content h3 {
  color: #f0f0f0;
}

:root.dark-theme .tutorial-content p {
  color: #a0a0a0;
}

:root.dark-theme .tutorial-icon {
  background: linear-gradient(135deg, #2a2a2a 0%, #3a3a3a 100%);
  color: #34d399;
}

:root.dark-theme .tutorial-card:hover .tutorial-icon {
  background: linear-gradient(135deg, #10b981 0%, #059669 100%);
  color: white;
}

:root.dark-theme .about-section {
  background: linear-gradient(135deg, #1a1a1a 0%, #0a0a0a 100%);
}

:root.dark-theme .about-text p {
  color: #a0a0a0;
}

:root.dark-theme .stat-card {
  background: #1a1a1a;
  border: 1px solid rgba(255, 255, 255, 0.06);
}

:root.dark-theme .stat-card:hover {
  background: #2a2a2a;
  border-color: rgba(16, 185, 129, 0.3);
}

:root.dark-theme .stat-number {
  background: linear-gradient(135deg, #34d399 0%, #10b981 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

:root.dark-theme .stat-label {
  color: #a0a0a0;
}

:root.dark-theme .footer {
  background: #0a0a0a;
  border-top: 1px solid rgba(255, 255, 255, 0.06);
}

:root.dark-theme .footer-section h3,
:root.dark-theme .footer-section h4 {
  color: #f0f0f0;
}

:root.dark-theme .footer-section p {
  color: #a0a0a0;
}

:root.dark-theme .footer-section a {
  color: #a0a0a0;
}

:root.dark-theme .footer-section a:hover {
  color: #34d399;
}

:root.dark-theme .footer-bottom {
  border-top: 1px solid rgba(255, 255, 255, 0.06);
  color: #a0a0a0;
}

/* Dark 模式选择文本样式 */
:root.dark-theme ::selection {
  background: rgba(52, 211, 153, 0.3);
  color: #f0f0f0;
}

/* Dark 模式焦点样式 */
:root.dark-theme button:focus-visible,
:root.dark-theme a:focus-visible {
  outline: 2px solid #34d399;
  outline-offset: 2px;
  border-radius: 4px;
}
</style>