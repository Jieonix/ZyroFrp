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
          <h1 class="hero-title">专业的 FRP 内网穿透服务</h1>
          <p class="hero-subtitle">稳定、快速、安全的内网穿透解决方案，让您的本地服务轻松对外访问</p>
          <div class="hero-actions">
            <button class="cta-primary" @click="goToRegister">立即开始</button>
            <button class="cta-secondary" @click="scrollToFeatures">了解更多</button>
          </div>
        </div>
        <div class="hero-visual">
          <div class="network-diagram">
            <div class="diagram-item">
              <div class="device-icon">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="2" y="2" width="20" height="16" rx="2" />
                  <path d="M2 8h20" />
                </svg>
              </div>
              <span class="diagram-label">本地服务</span>
            </div>
            <div class="arrow">→</div>
            <div class="diagram-item">
              <div class="device-icon">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M12 2a10 10 0 0 1 10 10c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2A10 10 0 0 1 12 2z" />
                  <path d="M8 14h8" />
                  <path d="M8 18h8" />
                </svg>
              </div>
              <span class="diagram-label">ZyroFrp</span>
            </div>
            <div class="arrow">→</div>
            <div class="diagram-item">
              <div class="device-icon">
                <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <circle cx="12" cy="12" r="10" />
                  <path d="M2 12h20" />
                  <path
                    d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z" />
                </svg>
              </div>
              <span class="diagram-label">互联网</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 功能特色区域 -->
    <section id="features" class="features-section">
      <div class="container">
        <h2 class="section-title">核心功能</h2>
        <div class="features-grid">
          <div class="feature-card" v-for="(feature, index) in features" :key="index"
            :style="{ animationDelay: `${index * 0.2}s` }" @mouseenter="featureHover(index)"
            @mouseleave="featureHover(null)">
            <div class="feature-icon" :class="{ 'icon-hover': hoveredFeature === index }">
              <svg width="48" height="48" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path :d="feature.icon" />
              </svg>
            </div>
            <h3>{{ feature.title }}</h3>
            <p>{{ feature.description }}</p>
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
            :class="{ featured: plan.featured }" :style="{ animationDelay: `${index * 0.15}s` }"
            @mouseenter="pricingHover(index)" @mouseleave="pricingHover(null)">
            <div v-if="plan.featured" class="popular-badge">{{ plan.badge }}</div>
            <h3>{{ plan.name }}</h3>
            <div class="price">{{ plan.price }}<span>/月</span></div>
            <ul class="features-list">
              <li v-for="(feature, featureIndex) in plan.features" :key="featureIndex">
                {{ feature }}
              </li>
            </ul>
            <button class="pricing-btn" :class="{ primary: plan.featured }" @click="goToRegister">
              {{ plan.buttonText }}
            </button>
          </div>
        </div>
      </div>
    </section>

    <!-- 使用教程区域 -->
    <section id="tutorials" class="tutorials-section">
      <div class="container">
        <h2 class="section-title">快速上手</h2>
        <div class="tutorials-steps">
          <div class="step">
            <div class="step-number">1</div>
            <h3>注册账号</h3>
            <p>创建您的 ZyroFrp 账户</p>
          </div>
          <div class="step">
            <div class="step-number">2</div>
            <h3>下载客户端</h3>
            <p>获取适合您系统的客户端</p>
          </div>
          <div class="step">
            <div class="step-number">3</div>
            <h3>配置隧道</h3>
            <p>在 Web 界面创建和管理隧道</p>
          </div>
          <div class="step">
            <div class="step-number">4</div>
            <h3>启动服务</h3>
            <p>运行客户端，享受内网穿透</p>
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
        }
      ],
      pricingPlans: [
        {
          name: '免费版',
          price: '¥0',
          features: [
            '基础流量 1GB/月',
            '单节点连接',
            '基础技术支持',
            'Web 管理界面'
          ],
          buttonText: '开始使用',
          featured: false
        },
        {
          name: '专业版',
          price: '¥9.9',
          features: [
            '流量 10GB/月',
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
          price: '¥29.9',
          features: [
            '流量 50GB/月',
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
  mounted() {
    this.checkLoginStatus()
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
    goToTutorials() {
      this.$router.push('/usingTutorials')
    },

    // 退出登录
    logout() {
      localStorage.removeItem('Token')
      this.isLoggedIn = false
      this.$router.push('/')
    },

    // 滚动到指定区域
    scrollToSection(id) {
      const el = document.querySelector(id)
      if (el) el.scrollIntoView({ behavior: 'smooth' })
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
    }
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
  padding: 120px 0 80px;
  background: linear-gradient(135deg, #f5f5f7 0%, #ffffff 100%);
  min-height: 580px;
  display: flex;
  align-items: center;
}

.hero-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  padding: 0 22px;
}

.hero-content {
  text-align: left;
}

.hero-title {
  font-size: 3rem;
  font-weight: 400;
  margin-bottom: 16px;
  background: linear-gradient(135deg, #1d1d1f 0%, #424245 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  line-height: 1.1;
}

.hero-subtitle {
  font-size: 21px;
  color: #86868b;
  margin-bottom: 32px;
  max-width: 500px;
  line-height: 1.4;
}

.hero-actions {
  display: flex;
  gap: 16px;
  margin-bottom: 0;
}

/* 网络示意图 */
.network-diagram {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
}

.diagram-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.device-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0071e3 0%, #0077ed 100%);
  border-radius: 20px;
  color: white;
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.3);
}

.diagram-label {
  font-size: 14px;
  font-weight: 500;
  color: #1d1d1f;
}

.arrow {
  font-size: 24px;
  color: #0071e3;
  font-weight: bold;
}

/* 通用容器 */
.container {
  max-width: 80rem;
  margin: 0 auto;
  padding: 0 22px;
}

/* 区域标题 */
.section-title {
  font-size: 40px;
  font-weight: 400;
  text-align: center;
  width: 20rem;
  margin-bottom: 48px;
  color: #1d1d1f;
}

/* 功能特色区域 */
.features-section {
  padding: 80px 0;
  background: #f5f5f7;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 32px;
  max-width: 1000px;
  margin: 0 auto;
}

.feature-card {
  width: 17rem;
  height: 17rem;
  background: white;
  padding: 1rem;
  border-radius: 18px;
  text-align: center;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.feature-card:hover {
  transform: translateY(-4px);
}

.feature-icon {
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0071e3 0%, #0077ed 100%);
  border-radius: 20px;
  color: white;
  margin-bottom: 16px;
  box-shadow: 0 8px 25px rgba(0, 113, 227, 0.3);
}

.feature-card h3 {
  font-size: 21px;
  font-weight: 600;
  margin-bottom: 12px;
  color: #1d1d1f;
}

.feature-card p {
  color: #86868b;
  line-height: 1.5;
}

/* 价格方案区域 */
.pricing-section {
  padding: 80px 0;
  background: white;
}

.pricing-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

.pricing-card {
  background: #f5f5f7;
  padding: 32px;
  border-radius: 18px;
  text-align: center;
  position: relative;
}

.pricing-card.featured {
  background: linear-gradient(135deg, #0071e3 0%, #0077ed 100%);
  color: white;
  transform: scale(1.05);
}

.popular-badge {
  position: absolute;
  top: -12px;
  left: 50%;
  transform: translateX(-50%);
  background: #ff9500;
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 600;
}

.pricing-card h3 {
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 16px;
}

.price {
  font-size: 48px;
  font-weight: 700;
  margin-bottom: 24px;
}

.price span {
  font-size: 16px;
  font-weight: 400;
}

.features-list {
  list-style: none;
  padding: 0;
  margin: 0 0 32px 0;
}

.features-list li {
  padding: 8px 0;
  border-bottom: 1px solid rgba(0, 0, 0, 0.1);
}

.pricing-card.featured .features-list li {
  border-bottom-color: rgba(255, 255, 255, 0.2);
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
}

.pricing-btn.primary {
  background: white;
  color: #0071e3;
}

.pricing-btn:not(.primary) {
  background: #0071e3;
  color: white;
}

/* 使用教程区域 */
.tutorials-section {
  padding: 80px 0;
  background: #f5f5f7;
}

.tutorials-steps {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
  max-width: 1000px;
  margin: 0 auto 48px;
}

.step {
  text-align: center;
}

.step-number {
  width: 60px;
  height: 60px;
  background: #0071e3;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: 600;
  margin: 0 auto 16px;
}

.step h3 {
  font-size: 21px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1d1d1f;
}

.step p {
  color: #86868b;
}

.tutorial-btn {
  padding: 12px 24px;
  background: #0071e3;
  color: white;
  border: none;
  border-radius: 980px;
  font-size: 17px;
  font-weight: 400;
  cursor: pointer;
  transition: background 0.3s;
}

.tutorial-btn:hover {
  background: #0077ed;
}

.tutorials-actions {
  text-align: center;
}

/* 关于我们区域 */
.about-section {
  padding: 80px 0;
  background: white;
}

.about-text {
  max-width: 600px;
  margin: 0 auto;
  text-align: center;
}

.about-text p {
  font-size: 21px;
  color: #86868b;
  line-height: 1.5;
  margin-bottom: 24px;
}

.stats {
  display: flex;
  justify-content: center;
  gap: 48px;
  max-width: 600px;
  margin: 48px auto 0;
}

.stat {
  text-align: center;
}

.stat-number {
  font-size: 40px;
  font-weight: 700;
  color: #1d1d1f;
  margin-bottom: 8px;
}

.stat-label {
  color: #86868b;
  font-size: 14px;
}

/* 页脚 */
.footer {
  background: #f5f5f7;
  padding: 48px 0 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
  margin-bottom: 32px;
  max-width: 1000px;
  margin: 0 auto;
}

.footer-section h3,
.footer-section h4 {
  margin-bottom: 16px;
  color: #1d1d1f;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section li {
  margin-bottom: 8px;
}

.footer-section a {
  color: #86868b;
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #0071e3;
}

.footer-bottom {
  text-align: center;
  padding-top: 24px;
  border-top: 1px solid rgba(0, 0, 0, 0.1);
  color: #86868b;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-menu {
    display: none;
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
    font-size: 18px;
    margin-left: auto;
    margin-right: auto;
  }

  .hero-actions {
    flex-direction: column;
    align-items: center;
  }

  .network-diagram {
    flex-direction: column;
    gap: 20px;
  }

  .arrow {
    transform: rotate(90deg);
  }

  .features-grid {
    grid-template-columns: 1fr;
  }

  .pricing-grid {
    grid-template-columns: 1fr;
  }

  .tutorials-steps {
    grid-template-columns: 1fr;
  }

  .footer-content {
    grid-template-columns: 1fr;
  }

  .section-title {
    font-size: 32px;
    margin-right: 5rem;
  }

  .stats {
    flex-direction: column;
    gap: 24px;
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