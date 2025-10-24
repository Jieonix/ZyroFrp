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
          <li @click="goTo('#features')">功能特色</li>
          <li @click="goTo('#pricing')">价格方案</li>
          <li @click="goTo('#tutorials')">使用教程</li>
          <li @click="goTo('#about')">关于我们</li>
        </ul>

        <!-- 用户操作区域 -->
        <div class="user-actions">

          <!-- 用户状态按钮 -->
          <div v-if="isLoggedIn" class="user-logged-in">
            <button class="dashboard-btn" @click="goToDashboard">控制台</button>
            <button class="logout-btn" @click="logout">退出</button>
          </div>
          <div v-else class="user-logged-out">
            <button class="login-btn" @click="goToLogin">登录</button>
            <button class="register-btn" @click="goToRegister">注册</button>
          </div>
          <!-- GitHub 按钮 -->
          <button class="github-btn" @click="openGitHub">
            <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
              <path
                d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z" />
            </svg>
            GitHub
          </button>
        </div>
      </div>
    </nav>

    <!-- 主横幅区域 -->
    <section class="hero">
      <div class="hero-container">
        <div class="hero-content">
          <h1 class="hero-title">
            <span class="title-gradient">专业的 FRP 内网穿透服务</span>
          </h1>
          <p class="hero-subtitle">稳定、快速、安全的内网穿透解决方案，让您的本地服务轻松对外访问</p>
          <div class="hero-actions">
            <button class="cta-primary" @click="goToRegister">
              <span class="btn-text">立即开始</span>
              <div class="btn-hover-effect"></div>
            </button>
            <button class="cta-secondary" @click="scrollToFeatures">
              <span class="btn-text">了解更多</span>
              <div class="btn-hover-effect"></div>
            </button>
          </div>
          <div class="hero-stats">
            <div class="stat-item">
              <div class="stat-number" ref="userCount">0</div>
              <div class="stat-label">活跃用户</div>
            </div>
            <div class="stat-item">
              <div class="stat-number" ref="uptimeCount">0</div>
              <div class="stat-label">服务可用性</div>
            </div>
            <div class="stat-item">
              <div class="stat-number" ref="speedCount">0</div>
              <div class="stat-label">平均响应速度</div>
            </div>
          </div>
        </div>
        <div class="hero-visual">
          <div class="network-animation">
            <div class="network-node" v-for="(node, index) in networkNodes" :key="index" :class="`node-${index + 1}`"
              :style="{ animationDelay: `${index * 0.5}s` }">
              <div class="node-icon">
                <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path :d="node.icon" />
                </svg>
              </div>
              <div class="node-label">{{ node.label }}</div>
            </div>
            <div class="connection-lines">
              <div class="line line-1"></div>
              <div class="line line-2"></div>
              <div class="line line-3"></div>
              <div class="line line-4"></div>
            </div>
            <div class="data-particles">
              <div v-for="n in 20" :key="n" class="particle" :style="getParticleStyle(n)"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="hero-background">
        <div class="bg-gradient-1"></div>
        <div class="bg-gradient-2"></div>
        <div class="bg-gradient-3"></div>
        <div class="bg-gradient-4"></div>
        <div class="bg-gradient-5"></div>
      </div>
    </section>

    <!-- 功能特色区域 -->
    <section id="features" class="features-section">
      <div class="container">
        <h2 class="section-title">核心功能</h2>
        <div class="features-layout">
          <div class="feature-group-left">
            <div class="feature-card" v-for="(feature, index) in features.slice(0, 3)" :key="index"
              :style="{ animationDelay: `${index * 0.3}s`, ...featureSizes[index] }" @mouseenter="featureHover(index)"
              @mouseleave="featureHover(null)" :class="{ 'feature-active': hoveredFeature === index }">
              <div class="feature-icon-wrapper">
                <div class="feature-icon" :class="{ 'icon-hover': hoveredFeature === index }">
                  <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path :d="feature.icon" />
                  </svg>
                </div>
                <div class="feature-glow"></div>
              </div>
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.description }}</p>
            </div>
          </div>
          <div class="feature-group-center">
            <div class="feature-card" v-for="(feature, index) in features.slice(3, 6)" :key="index + 3"
              :style="{ animationDelay: `${(index + 3) * 0.2}s`, ...featureSizes[index + 3] }" @mouseenter="featureHover(index + 3)"
              @mouseleave="featureHover(null)" :class="{ 'feature-active': hoveredFeature === index + 3 }">
              <div class="feature-icon-wrapper">
                <div class="feature-icon" :class="{ 'icon-hover': hoveredFeature === index + 3 }">
                  <svg width="36" height="36" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path :d="feature.icon" />
                  </svg>
                </div>
                <div class="feature-glow"></div>
              </div>
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.description }}</p>
            </div>
          </div>
          <div class="feature-group-right">
            <div class="feature-card" v-for="(feature, index) in features.slice(6, 9)" :key="index + 6"
              :style="{ animationDelay: `${(index + 6) * 0.15}s`, ...featureSizes[index + 6] }" @mouseenter="featureHover(index + 6)"
              @mouseleave="featureHover(null)" :class="{ 'feature-active': hoveredFeature === index + 6 }">
              <div class="feature-icon-wrapper">
                <div class="feature-icon" :class="{ 'icon-hover': hoveredFeature === index + 6 }">
                  <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path :d="feature.icon" />
                  </svg>
                </div>
                <div class="feature-glow"></div>
              </div>
              <h3>{{ feature.title }}</h3>
              <p>{{ feature.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 价格方案区域 -->
    <section id="pricing" class="pricing-section">
      <div class="container">
        <h2 class="section-title">价格方案</h2>
        <div class="pricing-grid">
          <div v-for="(plan, index) in pricingPlans" :key="index" class="pricing-card"
            :class="{ featured: plan.featured, 'pricing-active': hoveredPricing === index }"
            :style="{ animationDelay: `${index * 0.15}s` }" @mouseenter="pricingHover(index)"
            @mouseleave="pricingHover(null)">
            <div v-if="plan.featured" class="popular-badge">
              <span class="badge-text">{{ plan.badge }}</span>
              <div class="badge-glow"></div>
            </div>
            <div class="pricing-header">
              <h3>{{ plan.name }}</h3>
              <div class="price">
                <span class="price-amount">{{ plan.price }}</span>
                <span class="price-period">/月</span>
              </div>
            </div>
            <ul class="features-list">
              <li v-for="(feature, featureIndex) in plan.features" :key="featureIndex"
                :style="{ animationDelay: `${featureIndex * 0.1}s` }">
                <div class="feature-check">
                  <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="3">
                    <path d="M20 6L9 17l-5-5" />
                  </svg>
                </div>
                <span>{{ feature }}</span>
              </li>
            </ul>
            <button class="pricing-btn" :class="{ primary: plan.featured }" @click="goToRegister">
              <span class="btn-text">{{ plan.buttonText }}</span>
              <div class="btn-hover-effect"></div>
            </button>
            <div class="pricing-glow"></div>
          </div>
        </div>
      </div>
    </section>

    <!-- 使用教程区域 -->
    <section id="tutorials" class="tutorials-section">
      <div class="container">
        <h2 class="section-title">快速上手</h2>
        <div class="tutorials-layout">
          <div class="step-group-left">
            <div class="step-large" v-for="(step, index) in tutorialSteps.slice(0, 2)" :key="index"
              :class="{ 'step-active': activeStep === index }" @mouseenter="activeStep = index"
              @mouseleave="activeStep = null">
              <div class="step-number">{{ step.number }}</div>
              <h3>{{ step.title }}</h3>
              <p>{{ step.description }}</p>
              <div class="step-details" v-if="activeStep === index">
                <div class="step-content">
                  <div class="step-icon">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path :d="step.detailIcon" />
                    </svg>
                  </div>
                  <div class="step-info">
                    <h4>{{ step.detailTitle }}</h4>
                    <p>{{ step.detailDescription }}</p>
                    <button class="step-action-btn" @click="handleStepAction(index)">
                      {{ step.actionText }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="step-group-right">
            <div class="step-small" v-for="(step, index) in tutorialSteps.slice(2, 4)" :key="index + 2"
              :class="{ 'step-active': activeStep === index + 2 }" @mouseenter="activeStep = index + 2"
              @mouseleave="activeStep = null">
              <div class="step-number">{{ step.number }}</div>
              <h3>{{ step.title }}</h3>
              <p>{{ step.description }}</p>
              <div class="step-details" v-if="activeStep === index + 2">
                <div class="step-content">
                  <div class="step-icon">
                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                      <path :d="step.detailIcon" />
                    </svg>
                  </div>
                  <div class="step-info">
                    <h4>{{ step.detailTitle }}</h4>
                    <p>{{ step.detailDescription }}</p>
                    <button class="step-action-btn" @click="handleStepAction(index + 2)">
                      {{ step.actionText }}
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 关于我们区域 -->
    <section id="about" class="about-section">
      <div class="container">
        <h2 class="section-title">关于 ZyroFrp</h2>
        <div class="about-content">
          <div class="about-text">
            <p>ZyroFrp 是一个专业的 FRP 内网穿透服务平台，致力于为用户提供稳定、快速、安全的内网穿透服务。</p>
            <p>我们拥有多年的网络服务经验，技术团队持续优化服务性能，确保用户获得最佳的使用体验。</p>
            <div class="stats">
              <div class="stat">
                <div class="stat-number">10K+</div>
                <div class="stat-label">活跃用户</div>
              </div>
              <div class="stat">
                <div class="stat-number">99.9%</div>
                <div class="stat-label">服务可用性</div>
              </div>
              <div class="stat">
                <div class="stat-number">24/7</div>
                <div class="stat-label">技术支持</div>
              </div>
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
        <div class="footer-bottom">
          <p>&copy; 2024 ZyroFrp. 保留所有权利。</p>
        </div>
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
      backgroundGradients: [
        'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)',
        'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)',
        'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)',
        'linear-gradient(135deg, #fa709a 0%, #fee140 100%)',
        'linear-gradient(135deg, #a8edea 0%, #fed6e3 100%)',
        'linear-gradient(135deg, #d299c2 0%, #fef9d7 100%)',
        'linear-gradient(135deg, #89f7fe 0%, #66a6ff 100%)'
      ],
      currentBackground: '',
      featureSizes: Array(9).fill({ width: '100%', minHeight: '180px' }),
      networkNodes: [
        {
          label: '本地服务',
          icon: 'M2 8h20 M2 12h20 M2 16h20 M3 5v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2z'
        },
        {
          label: 'ZyroFrp',
          icon: 'M12 2a10 10 0 0 1 10 10c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2A10 10 0 0 1 12 2z M8 14h8 M8 18h8'
        },
        {
          label: '互联网',
          icon: 'M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z M2 12h20'
        }
      ],
      tutorialSteps: [
        {
          number: '1',
          title: '注册账号',
          description: '创建您的 ZyroFrp 账户',
          detailIcon: 'M16 7a4 4 0 11-8 0 4 4 0 018 0z M12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z',
          detailTitle: '快速注册',
          detailDescription: '只需邮箱验证，一分钟完成注册，立即开始使用',
          actionText: '立即注册'
        },
        {
          number: '2',
          title: '下载客户端',
          description: '获取适合您系统的客户端',
          detailIcon: 'M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-8l-4-4m0 0L8 8m4-4v12',
          detailTitle: '多平台支持',
          detailDescription: '支持 Windows、macOS、Linux 等主流操作系统',
          actionText: '下载客户端'
        },
        {
          number: '3',
          title: '配置隧道',
          description: '在 Web 界面创建和管理隧道',
          detailIcon: 'M13 10V3L4 14h7v7l9-11h-7z',
          detailTitle: '可视化配置',
          detailDescription: '通过直观的 Web 界面轻松配置和管理您的隧道',
          actionText: '配置隧道'
        },
        {
          number: '4',
          title: '启动服务',
          description: '运行客户端，享受内网穿透',
          detailIcon: 'M13 10V3L4 14h7v7l9-11h-7z',
          detailTitle: '一键启动',
          detailDescription: '运行客户端程序，立即享受稳定快速的内网穿透服务',
          actionText: '启动服务'
        }
      ],
      features: [
        {
          title: '高速稳定',
          description: '多节点负载均衡，确保服务稳定运行，提供高速的网络连接体验',
          icon: 'M13 2L3 14h9l-1 8 10-12h-9l1-8z'
        },
        {
          title: '安全可靠',
          description: '端到端加密传输，多重安全防护机制，保障您的数据安全',
          icon: 'M3 11h18v11H3z M12 16a1 1 0 1 0 0-2 1 1 0 0 0 0 2z M7 7V7a5 5 0 0 1 10 0v4'
        },
        {
          title: '简单易用',
          description: '可视化配置界面，一键部署，无需复杂的技术背景即可上手',
          icon: 'M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z M14 2v6h6 M16 13H8 M16 17H8 M10 9H8'
        },
        {
          title: '实时监控',
          description: '详细的流量统计和连接状态监控，随时掌握服务运行情况',
          icon: 'M18 20V10 M12 20V4 M6 20V14'
        },
        {
          title: '多协议支持',
          description: '支持 TCP、UDP、HTTP、HTTPS 等多种协议，满足不同场景需求',
          icon: 'M8 6h13 M8 12h13 M8 18h13 M3 6h.01 M3 12h.01 M3 18h.01'
        },
        {
          title: '跨平台兼容',
          description: '支持 Windows、macOS、Linux 等主流操作系统，无缝切换',
          icon: 'M9 19v-6a2 2 0 0 0-2-2H5a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2zm0 0V9a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v10m-6 0a2 2 0 0 0 2 2h2a2 2 0 0 0 2-2m0 0V5a2 2 0 0 1 2-2h2a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-2a2 2 0 0 1-2-2z'
        },
        {
          title: '智能路由',
          description: '智能选择最优线路，自动故障切换，确保服务持续可用',
          icon: 'M3 9l9-7 9 7v11a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2z M9 22V12h6v10'
        },
        {
          title: '流量控制',
          description: '精确的流量统计和控制，帮助您合理规划资源使用',
          icon: 'M12 2v20 M17 5H9.5a3.5 3.5 0 0 0 0 7h5a3.5 3.5 0 0 1 0 7H6'
        },
        {
          title: 'API 集成',
          description: '提供完整的 API 接口，方便与其他系统集成和自动化管理',
          icon: 'M14 10l-2 1m0 0l-2-1m2 1v2.5M20 7l-2 1m2-1l-2-1m2 1v2.5M14 4l-2-1m2 1l2-1M14 4v2.5M20 4v2.5M6 10l-2 1m2-1l-2-1m2 1v2.5M3 7l2-1M3 7l2 1M3 7v2.5M6 4L4 3m2 1L8 3M6 4v2.5'
        }
      ],
      pricingPlans: [
        {
          name: '免费版',
          price: '¥0',
          features: [
            '流量 ∞ ',
            '单节点连接',
            '基础技术支持',
            'Web 管理界面'
          ],
          buttonText: '开始使用',
          featured: false
        },
        {
          name: '专业版',
          price: '¥0',
          features: [
            '流量 ∞ ',
            '多节点负载均衡',
            '优先技术支持',
            '高级监控功能',
            '自定义域名'
          ],
          buttonText: '立即购买',
          featured: true,
          badge: '最受欢迎'
        },
        {
          name: '企业版',
          price: '¥0',
          features: [
            '流量 ∞ ',
            '专属节点',
            '24/7 技术支持',
            'API 接口',
            '私有化部署'
          ],
          buttonText: '联系销售',
          featured: false
        }
      ]
    }
  },
  methods: {
    // 随机选择背景
    setRandomBackground() {
      const randomIndex = Math.floor(Math.random() * this.backgroundGradients.length)
      this.currentBackground = this.backgroundGradients[randomIndex]
    },

    // 生成随机功能卡片大小
    generateRandomFeatureSizes() {
      const sizes = []
      for (let i = 0; i < this.features.length; i++) {
        const randomWidth = Math.floor(Math.random() * 40) + 60 // 60% 到 100%
        const randomHeight = Math.floor(Math.random() * 60) + 140 // 140px 到 200px
        sizes.push({
          width: `${randomWidth}%`,
          minHeight: `${randomHeight}px`
        })
      }
      this.featureSizes = sizes
    },

    // 滚动动画效果
    scrollToSection(id) {
      const el = document.querySelector(id)
      if (el) {
        el.scrollIntoView({
          behavior: 'smooth',
          block: 'start'
        })
        // 添加滚动后的微动画
        setTimeout(() => {
          el.style.transform = 'translateY(-10px)'
          setTimeout(() => {
            el.style.transform = 'translateY(0)'
          }, 300)
        }, 500)
      }
    },

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
    goToTutorials() {
      this.$router.push('/usingTutorials')
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

    // 价格卡片悬停效果
    pricingHover(index) {
      this.hoveredPricing = index
    },

    // 粒子动画样式
    getParticleStyle(n) {
      const delay = (n * 0.1) % 2
      const duration = 2 + (n % 3)
      return {
        animationDelay: `${delay}s`,
        animationDuration: `${duration}s`
      }
    },

    // 滚动到功能区域
    scrollToFeatures() {
      this.scrollToSection('#features')
    },

    // 处理教程步骤操作
    handleStepAction(index) {
      switch (index) {
        case 0:
          this.goToRegister()
          break
        case 1:
          // 这里可以添加下载客户端的逻辑
          window.open('/download', '_blank')
          break
        case 2:
          this.$router.push('/tunnels')
          break
        case 3:
          // 这里可以添加启动服务的逻辑
          this.$router.push('/home')
          break
      }
    }
  },
  mounted() {
    this.checkLoginStatus()
    this.setRandomBackground()
    this.generateRandomFeatureSizes()

    // 数字动画效果
    const animateCounter = (element, target, duration = 2000) => {
      const start = 0
      const increment = target / (duration / 16)
      let current = start

      const timer = setInterval(() => {
        current += increment
        if (current >= target) {
          current = target
          clearInterval(timer)
        }
        element.textContent = Math.floor(current).toLocaleString()
      }, 16)
    }

    // 延迟执行数字动画
    setTimeout(() => {
      if (this.$refs.userCount) animateCounter(this.$refs.userCount, 10000)
      if (this.$refs.uptimeCount) animateCounter(this.$refs.uptimeCount, 999)
      if (this.$refs.speedCount) animateCounter(this.$refs.speedCount, 50)
    }, 500)
  }
}
</script>


<style scoped>
.landing-page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  color: #1d1d1f;
  background-color: #ffffff;
}

/* 导航栏样式 */
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(20px);
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  z-index: 1000;
  height: 3.9rem;
}

.nav-container {
  max-width: 100rem;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 22px;
}

.logo h1 {
  font-size: 21px;
  font-weight: 500;
  color: #1d1d1f;
  margin: 0;
}

.nav-menu {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-menu a {
  text-decoration: none;
  color: #1d1d1f;
  font-size: 1rem;
  font-weight: 400;
  transition: color 0.3s;
  background-color: transparent;
}

.nav-menu li {
  background-color: transparent;
  height: 4rem;
  display: flex;
  align-items: center;
  padding: 0.8rem;
  transition: all 0.3s;
  cursor: pointer;
}

.nav-menu li:hover {
  background-color: #f0f0f0;
}

.user-actions {
  display: flex;
  align-items: center;
  height: 100%;
}

/* 按钮样式 */
.github-btn,
.dashboard-btn,
.logout-btn,
.login-btn,
.register-btn {
  background-color: transparent;
  cursor: pointer;
  border: none;
  transition: all 0.3s;
  height: 100%;
  padding: 0.8rem 1rem;
  height: 4rem;
  font-size: 1rem;
}

.github-btn {
  display: flex;
  align-items: center;
}

.github-btn svg {
  margin-bottom: 0.2rem;
  margin-right: 0.3rem;
}

.github-btn:hover,
.dashboard-btn:hover,
.logout-btn:hover,
.login-btn:hover,
.register-btn:hover {
  background-color: #f0f0f0;
}

.user-actions {
  margin-left: 30rem;
}

.cta-primary,
.cta-secondary {
  padding: 12px 24px;
  border-radius: 980px;
  font-size: 17px;
  font-weight: 400;
  cursor: pointer;
  transition: all 0.3s;
}

.cta-primary {
  background: #0071e3;
  color: white;
  border: none;
}

.cta-primary:hover {
  background: #0077ed;
}

.cta-secondary {
  background: transparent;
  color: #0071e3;
  border: 1px solid #0071e3;
}

.cta-secondary:hover {
  background: rgba(0, 113, 227, 0.1);
}

/* 主横幅区域 */
.hero {
  padding: 0;
  background: linear-gradient(135deg, #f5f5f7 0%, #ffffff 100%);
  height: 100vh;
  min-height: 600px;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
  transition: background 1s ease-in-out;
}

.hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: v-bind(currentBackground);
  opacity: 0;
  transition: opacity 1s ease-in-out;
  z-index: 1;
}

.hero:hover::before {
  opacity: 0.1;
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  padding: 0 22px;
  position: relative;
  z-index: 3;
}

.hero-content {
  text-align: left;
}

.hero-title {
  font-size: 2.5rem;
  font-weight: 400;
  margin-bottom: 16px;
  line-height: 1.1;
}

.title-gradient {
  background: linear-gradient(135deg, #0071e3 0%, #00c6ff 50%, #0071e3 100%);
  background-size: 200% 200%;
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  animation: gradientShift 3s ease-in-out infinite;
}

@keyframes gradientShift {

  0%,
  100% {
    background-position: 0% 50%;
  }

  50% {
    background-position: 100% 50%;
  }
}

.hero-subtitle {
  font-size: 18px;
  color: #86868b;
  margin-bottom: 28px;
  max-width: 500px;
  line-height: 1.4;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 40px;
  position: relative;
}

.cta-primary,
.cta-secondary {
  position: relative;
  overflow: hidden;
  padding: 12px 24px;
  border-radius: 980px;
  font-size: 17px;
  font-weight: 400;
  cursor: pointer;
  transition: all 0.3s;
  border: none;
}

.cta-primary {
  background: #0071e3;
  color: white;
}

.cta-secondary {
  background: transparent;
  color: #0071e3;
  border: 1px solid #0071e3;
}

.btn-hover-effect {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.5s;
}

.cta-primary:hover .btn-hover-effect,
.cta-secondary:hover .btn-hover-effect {
  left: 100%;
}

.cta-primary:hover {
  background: #0077ed;
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.3);
}

.cta-secondary:hover {
  background: rgba(0, 113, 227, 0.1);
  transform: translateY(-2px);
}

.hero-stats {
  display: flex;
  gap: 24px;
  margin-top: 32px;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 24px;
  font-weight: 700;
  color: #0071e3;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #86868b;
}

/* 网络动画 */
.network-animation {
  position: relative;
  width: 400px;
  height: 300px;
  margin: 0 auto;
}

.network-node {
  position: absolute;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  opacity: 0;
  animation: fadeInUp 0.8s ease-out forwards;
}

.node-1 {
  top: 50%;
  left: 20%;
  transform: translateY(-50%);
}

.node-2 {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.node-3 {
  top: 50%;
  left: 80%;
  transform: translateY(-50%);
}

.node-icon {
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0071e3 0%, #00c6ff 100%);
  border-radius: 16px;
  color: white;
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.3);
  transition: all 0.3s;
}

.network-node:hover .node-icon {
  transform: scale(1.1);
  box-shadow: 0 12px 35px rgba(0, 113, 227, 0.4);
}

.node-label {
  font-size: 12px;
  font-weight: 500;
  color: #1d1d1f;
  background: rgba(255, 255, 255, 0.9);
  padding: 4px 8px;
  border-radius: 12px;
  backdrop-filter: blur(10px);
}

.connection-lines {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.line {
  position: absolute;
  height: 2px;
  background: linear-gradient(90deg, #0071e3, #00c6ff);
  top: 50%;
  transform: translateY(-50%);
  animation: pulseLine 2s ease-in-out infinite;
}

.line-1 {
  left: 20%;
  width: 30%;
  animation-delay: 0s;
}

.line-2 {
  left: 50%;
  width: 30%;
  animation-delay: 0.5s;
}

@keyframes pulseLine {

  0%,
  100% {
    opacity: 0.3;
  }

  50% {
    opacity: 1;
  }
}

.data-particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
}

.particle {
  position: absolute;
  width: 4px;
  height: 4px;
  background: #0071e3;
  border-radius: 50%;
  animation: particleMove 2s linear infinite;
}

@keyframes particleMove {
  0% {
    transform: translateX(0) translateY(0);
    opacity: 0;
  }

  10% {
    opacity: 1;
  }

  90% {
    opacity: 1;
  }

  100% {
    transform: translateX(200px) translateY(-100px);
    opacity: 0;
  }
}

.hero-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 2;
}

.bg-gradient-1,
.bg-gradient-2,
.bg-gradient-3,
.bg-gradient-4,
.bg-gradient-5 {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.08;
  transition: all 2s ease-in-out;
}

.bg-gradient-1 {
  width: 400px;
  height: 400px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  top: 10%;
  left: 10%;
  animation: float 8s ease-in-out infinite;
}

.bg-gradient-2 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  bottom: 20%;
  right: 15%;
  animation: float 10s ease-in-out infinite reverse;
}

.bg-gradient-3 {
  width: 250px;
  height: 250px;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  top: 60%;
  left: 70%;
  animation: float 12s ease-in-out infinite;
}

.bg-gradient-4 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  top: 30%;
  right: 10%;
  animation: float 14s ease-in-out infinite reverse;
}

.bg-gradient-5 {
  width: 350px;
  height: 350px;
  background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
  bottom: 10%;
  left: 20%;
  animation: float 16s ease-in-out infinite;
}

@keyframes float {

  0%,
  100% {
    transform: translateY(0px) rotate(0deg);
  }

  50% {
    transform: translateY(-20px) rotate(180deg);
  }
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 通用容器 */
.container {
  max-width: 100rem;
  margin: 0 auto;
  padding: 0 22px;
}

/* 区域标题 */
.section-title {
  font-size: 42px;
  font-weight: 300;
  text-align: center;
  margin-bottom: 60px;
  color: #1d1d1f;
  letter-spacing: -0.5px;
  position: relative;
  display: block;
  width: 100%;
  white-space: nowrap;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 2px;
  background: linear-gradient(135deg, #0071e3 0%, #00c6ff 100%);
  border-radius: 1px;
}

/* 功能特色区域 */
.features-section {
  padding: 0;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  height: 100vh;
  min-height: 600px;
  position: relative;
  overflow: hidden;
  transition: all 0.5s ease-in-out;
  display: flex;
  align-items: center;
  justify-content: center;
}

.features-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 20% 30%, rgba(0, 113, 227, 0.03) 0%, transparent 60%),
    radial-gradient(circle at 80% 70%, rgba(0, 198, 255, 0.03) 0%, transparent 60%);
  pointer-events: none;
}

.features-section:hover {
  background: linear-gradient(135deg, #f0f4f8 0%, #ffffff 100%);
}

.features-layout {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 30px;
  max-width: 1400px;
  margin: 0 auto;
  position: relative;
  z-index: 1;
  height: 70vh;
  align-items: start;
  justify-items: center;
  width: 100%;
}

.feature-group-left {
  display: flex;
  flex-direction: column;
  gap: 25px;
  margin-top: 40px;
  width: 100%;
  align-items: center;
}

.feature-group-center {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-top: 10px;
  width: 100%;
  align-items: center;
}

.feature-group-right {
  display: flex;
  flex-direction: column;
  gap: 15px;
  margin-top: 80px;
  width: 100%;
  align-items: center;
}

.feature-card {
  background: white;
  padding: 1.5rem;
  border-radius: 16px;
  text-align: center;
  box-shadow: 0 6px 25px rgba(0, 0, 0, 0.1);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(0, 113, 227, 0.1);
  min-height: 160px;
}

.feature-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(0, 113, 227, 0.05), transparent);
  transition: left 0.6s;
}

.feature-card:hover::before {
  left: 100%;
}

.feature-card:hover {
  transform: translateY(-6px) scale(1.02);
  box-shadow: 0 15px 35px rgba(0, 113, 227, 0.15);
  border-color: rgba(0, 113, 227, 0.3);
}

.feature-active {
  transform: translateY(-6px) scale(1.02);
  box-shadow: 0 15px 35px rgba(0, 113, 227, 0.15);
  border-color: rgba(0, 113, 227, 0.3);
}

.feature-icon-wrapper {
  position: relative;
  margin-bottom: 0.8rem;
}

.feature-icon {
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0071e3 0%, #00c6ff 100%);
  border-radius: 12px;
  color: white;
  box-shadow: 0 4px 15px rgba(0, 113, 227, 0.3);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  position: relative;
  z-index: 2;
}

.icon-hover {
  transform: scale(1.1) rotate(5deg);
  box-shadow: 0 12px 35px rgba(0, 113, 227, 0.4);
}

.feature-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 60px;
  height: 60px;
  background: radial-gradient(circle, rgba(0, 113, 227, 0.2) 0%, transparent 70%);
  border-radius: 50%;
  opacity: 0;
  transition: opacity 0.4s;
}

.feature-card:hover .feature-glow {
  opacity: 1;
}

.feature-card h3 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 6px;
  color: #1d1d1f;
  transition: color 0.3s;
}

.feature-card:hover h3 {
  color: #0071e3;
}

.feature-card p {
  color: #86868b;
  line-height: 1.4;
  margin-bottom: 0.8rem;
  font-size: 12px;
}


/* 价格方案区域 */
.pricing-section {
  padding: 0;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  height: 100vh;
  min-height: 600px;
  position: relative;
  transition: all 0.5s ease-in-out;
  display: flex;
  align-items: center;
}

.pricing-section:hover {
  background: linear-gradient(135deg, #ffffff 0%, #f0f4f8 100%);
}

.pricing-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  max-width: 100%;
  margin-left: 3.5rem;
}

.pricing-card {
  width: 14rem;
  background: #f5f5f7;
  padding: 1.5rem;
  border-radius: 20px;
  text-align: center;
  position: relative;
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  border: 1px solid rgba(0, 113, 227, 0.1);
  overflow: hidden;
  min-height: 380px;
}

.pricing-card:hover,
.pricing-active {
  transform: translateY(-8px) scale(1.02);
  box-shadow: 0 20px 40px rgba(0, 113, 227, 0.15);
  border-color: rgba(0, 113, 227, 0.3);
}

.pricing-card.featured {
  background: linear-gradient(135deg, #0071e3 0%, #00c6ff 100%);
  color: white;
  transform: scale(1.1);
  box-shadow: 0 20px 40px rgba(0, 113, 227, 0.3);
}

.pricing-card.featured:hover,
.pricing-card.featured.pricing-active {
  transform: scale(1.15) translateY(-8px);
  box-shadow: 0 30px 60px rgba(0, 113, 227, 0.4);
}

.popular-badge {
  width: 7rem;
  position: absolute;
  top: -12px;
  left: 50%;
  transform: translateX(-50%);
  background: linear-gradient(135deg, #ff6b35 0%, #ff8e53 100%);
  color: white;
  padding: 0.3rem 0rem;
  border-radius: 24px;
  font-size: 0.8rem;
  font-weight: 700;
  letter-spacing: 0.5px;
  box-shadow: 0 6px 20px rgba(255, 107, 53, 0.4);
  z-index: 2;
  position: relative;
  text-transform: uppercase;
  border: 2px solid rgba(255, 255, 255, 0.2);
}

.badge-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 140%;
  height: 140%;
  background: radial-gradient(circle, rgba(255, 107, 53, 0.4) 0%, transparent 70%);
  border-radius: 24px;
  animation: badgePulse 1.5s ease-in-out infinite;
}

@keyframes badgePulse {

  0%,
  100% {
    opacity: 0.5;
  }

  50% {
    opacity: 1;
  }
}

.pricing-header {
  margin-bottom: 1.5rem;
}

.pricing-card h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 0.8rem;
  transition: color 0.3s;
}

.pricing-card:hover h3,
.pricing-active h3 {
  color: #0071e3;
}

.pricing-card.featured h3 {
  color: white;
}

.price {
  font-size: 36px;
  font-weight: 700;
  margin-bottom: 1rem;
  display: flex;
  align-items: baseline;
  justify-content: center;
  gap: 4px;
}

.price-amount {
  font-size: 36px;
  line-height: 1;
}

.price-period {
  font-size: 14px;
  font-weight: 400;
  opacity: 0.8;
}

.features-list {
  list-style: none;
  padding: 0;
  margin: 0 0 2rem 0;
  text-align: left;
}

.features-list li {
  padding: 8px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
  gap: 8px;
  opacity: 0;
  animation: fadeInUp 0.5s ease-out forwards;
  font-size: 14px;
}

.pricing-card.featured .features-list li {
  border-bottom-color: rgba(255, 255, 255, 0.2);
}

.feature-check {
  width: 20px;
  height: 20px;
  background: rgba(0, 113, 227, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0071e3;
  flex-shrink: 0;
  transition: all 0.3s;
}

.pricing-card:hover .feature-check,
.pricing-active .feature-check {
  background: #0071e3;
  color: white;
  transform: scale(1.1);
}

.pricing-card.featured .feature-check {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.pricing-btn {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 980px;
  font-size: 17px;
  font-weight: 400;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 113, 227, 0.2);
}

.pricing-btn.primary {
  background: white;
  color: #0071e3;
}

.pricing-btn:not(.primary) {
  background: #0071e3;
  color: white;
}

.pricing-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.3);
}

.pricing-btn.primary:hover {
  background: #f8f9fa;
}

.pricing-btn:not(.primary):hover {
  background: #0077ed;
}

.pricing-glow {
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
  transition: left 0.6s;
}

.pricing-card:hover .pricing-glow {
  left: 100%;
}

/* 使用教程区域 */
.tutorials-section {
  padding: 0;
  background: linear-gradient(135deg, #f8f9fa 0%, #ffffff 100%);
  height: 100vh;
  min-height: 600px;
  position: relative;
  transition: all 0.5s ease-in-out;
  display: flex;
  align-items: center;
}

.tutorials-section:hover {
  background: linear-gradient(135deg, #f0f4f8 0%, #ffffff 100%);
}

.tutorials-layout {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  max-width: 1200px;
  margin: 0 auto;
  height: 70vh;
  align-items: start;
  justify-items: center;
}

.step-group-left {
  display: flex;
  flex-direction: column;
  gap: 40px;
  margin-top: 80px;
  width: 100%;
  align-items: center;
}

.step-group-right {
  display: flex;
  flex-direction: column;
  gap: 30px;
  margin-top: 40px;
  width: 100%;
  align-items: center;
}

.step-large,
.step-small {
  text-align: center;
  background: white;
  padding: 2rem;
  border-radius: 20px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  position: relative;
  overflow: hidden;
  border: 1px solid rgba(0, 113, 227, 0.1);
}

.step-large {
  min-height: 220px;
  width: 100%;
}

.step-small {
  min-height: 160px;
  width: 85%;
}

.step-large:hover,
.step-small:hover,
.step-active {
  transform: translateY(-10px);
  box-shadow: 0 25px 50px rgba(0, 113, 227, 0.2);
  border-color: rgba(0, 113, 227, 0.4);
}

.step-number {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #0071e3 0%, #00c6ff 100%);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 600;
  margin: 0 auto 12px;
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.3);
  transition: all 0.3s;
}

.step:hover .step-number,
.step-active .step-number {
  transform: scale(1.1);
  box-shadow: 0 12px 35px rgba(0, 113, 227, 0.4);
}

.step h3 {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 6px;
  color: #1d1d1f;
  transition: color 0.3s;
}

.step:hover h3,
.step-active h3 {
  color: #0071e3;
}

.step p {
  color: #86868b;
  margin-bottom: 0.8rem;
  font-size: 14px;
}

.step-details {
  position: absolute;
  top: 100%;
  left: 0;
  width: 100%;
  background: white;
  padding: 1rem;
  border-radius: 0 0 16px 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  z-index: 10;
  animation: slideDown 0.3s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }

  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.step-content {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  text-align: left;
}

.step-icon {
  width: 32px;
  height: 32px;
  background: rgba(0, 113, 227, 0.1);
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #0071e3;
  flex-shrink: 0;
}

.step-info h4 {
  font-size: 14px;
  font-weight: 600;
  margin-bottom: 4px;
  color: #1d1d1f;
}

.step-info p {
  font-size: 12px;
  color: #86868b;
  margin-bottom: 8px;
  line-height: 1.4;
}

.step-action-btn {
  padding: 6px 12px;
  background: #0071e3;
  color: white;
  border: none;
  border-radius: 16px;
  font-size: 12px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.step-action-btn:hover {
  background: #0077ed;
  transform: translateY(-1px);
}


/* 关于我们区域 */
.about-section {
  padding: 0;
  background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
  height: 100vh;
  min-height: 600px;
  transition: all 0.5s ease-in-out;
  display: flex;
  align-items: center;
}

.about-section:hover {
  background: linear-gradient(135deg, #ffffff 0%, #f0f4f8 100%);
}

.about-text {
  max-width: 40rem;
  margin: 0 auto;
  text-align: center;
  margin-left: 6.5rem;
}

.about-text p {
  font-size: 18px;
  color: #86868b;
  line-height: 1.5;
  margin-bottom: 20px;
}

.stats {
  display: flex;
  justify-content: center;
  gap: 32px;
  max-width: 600px;
  margin: 32px auto 0;
}

.stat {
  text-align: center;
}

.stat-number {
  font-size: 32px;
  font-weight: 700;
  color: #1d1d1f;
  margin-bottom: 6px;
}

.stat-label {
  color: #86868b;
  font-size: 14px;
}

/* 页脚 */
.footer {
  background: #f5f5f7;
  padding: 40px 0 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  margin-bottom: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.footer-section h3 {
  margin-bottom: 12px;
  color: #1d1d1f;
  font-size: 16px;
}

.footer-section h4 {
  margin-bottom: 12px;
  color: #1d1d1f;
  font-size: 13px;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  margin-bottom: 6px;
}

.footer-section a {
  color: #86868b;
  text-decoration: none;
  transition: color 0.3s;
  font-size: 13px;
}

.footer-section a:hover {
  color: #0071e3;
}

.footer-bottom {
  text-align: center;
  padding-top: 20px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  color: #86868b;
  font-size: 13px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-menu {
    display: none;
  }

  .hero {
    height: auto;
    min-height: 100vh;
    padding: 80px 0 40px;
  }

  .hero-container {
    grid-template-columns: 1fr;
    gap: 40px;
    text-align: center;
  }

  .hero-content {
    text-align: center;
  }

  .hero-title {
    font-size: 32px;
  }

  .hero-subtitle {
    font-size: 16px;
    margin-left: auto;
    margin-right: auto;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .features-section,
  .pricing-section,
  .tutorials-section,
  .about-section {
    height: auto;
    min-height: 100vh;
    padding: 80px 0;
  }

  .features-layout {
    grid-template-columns: 1fr;
    gap: 20px;
    height: auto;
  }

  .feature-group-left,
  .feature-group-center,
  .feature-group-right {
    margin-top: 0;
    gap: 15px;
  }

  .feature-card {
    width: 100% !important;
    margin: 0 auto;
    min-height: 140px !important;
    padding: 1rem !important;
  }

  .pricing-grid {
    grid-template-columns: 1fr;
    gap: 30px;
  }

  .tutorials-layout {
    grid-template-columns: 1fr;
    gap: 30px;
    height: auto;
  }

  .step-group-left,
  .step-group-right {
    margin-top: 0;
    gap: 20px;
  }

  .step-large,
  .step-small {
    width: 100%;
    margin: 0 auto;
  }

  .footer-content {
    grid-template-columns: 1fr;
  }

  .section-title {
    font-size: 28px;
    margin-right: 0;
    white-space: nowrap;
  }

  .stats {
    flex-direction: column;
    gap: 20px;
  }
}

/* 深色模式支持 */
@media (prefers-color-scheme: dark) {
  .landing-page {
    background-color: #000000;
    color: #f5f5f7;
  }

  .navbar {
    background: rgba(29, 29, 31, 0.8);
    border-bottom-color: rgba(255, 255, 255, 0.1);
  }

  .logo h1,
  .nav-menu a,
  .github-btn,
  .dashboard-btn,
  .logout-btn,
  .login-btn,
  .register-btn {
    color: #f5f5f7;
  }

  .github-btn:hover,
  .dashboard-btn:hover,
  .logout-btn:hover,
  .login-btn:hover,
  .register-btn:hover {
    background: rgba(255, 255, 255, 0.1);
  }

  .hero {
    background: linear-gradient(135deg, #1d1d1f 0%, #000000 100%);
  }

  .hero-title {
    background: linear-gradient(135deg, #f5f5f7 0%, #a1a1a6 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .features-section,
  .tutorials-section,
  .footer {
    background: #1d1d1f;
  }

  .feature-card,
  .pricing-card:not(.featured) {
    background: #2c2c2e;
    color: #f5f5f7;
  }

  .feature-card h3,
  .pricing-card h3,
  .step h3,
  .footer-section h3,
  .footer-section h4 {
    color: #f5f5f7;
  }

  .feature-card p,
  .step p,
  .about-text p,
  .footer-section a,
  .footer-bottom {
    color: #a1a1a6;
  }
}
</style>