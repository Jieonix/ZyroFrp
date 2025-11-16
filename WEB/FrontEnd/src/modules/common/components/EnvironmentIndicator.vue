<template>
  <div class="environment-indicator" :class="environmentClass" :title="environmentTooltip">
    <div class="env-dot"></div>
    <div class="env-text">{{ environmentText }}</div>
  </div>
</template>

<script setup>
import { computed } from 'vue'

// 获取当前环境
const getEnvironment = () => {
  // 根据当前域名动态判断环境（优先级最高）
  const hostname = window.location.hostname

  // 生产域名判断
  if (hostname === 'zyroo.cn' || hostname.includes('zyroo.cn')) {
    return 'prod'
  }

  // 开发环境判断
  if (hostname === 'localhost' || hostname === '127.0.0.1') {
    return 'dev'
  }

  // 最后使用构建时注入的环境变量
  if (import.meta.env.VITE_APP_ENV) {
    return import.meta.env.VITE_APP_ENV
  }

  // 默认为开发环境
  return 'dev'
}

const environment = computed(() => getEnvironment())

// 环境显示文本
const environmentText = computed(() => {
  return environment.value === 'prod' ? 'PROD' : 'DEV'
})

// 环境样式类
const environmentClass = computed(() => {
  return {
    'env-prod': environment.value === 'prod',
    'env-dev': environment.value === 'dev'
  }
})

// 环境提示文本
const environmentTooltip = computed(() => {
  const envText = environment.value === 'prod' ? '生产环境' : '开发环境'
  const apiUrl = import.meta.env.VITE_API_BASE_URL || '自动检测'
  return `${envText} - API: ${apiUrl}`
})
</script>

<style scoped>
.environment-indicator {
  position: fixed;
  top: 0.65rem;
  right: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.2rem;
  padding: 0.2rem 0.4rem;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 0.5rem;
  z-index: 9999;
  cursor: pointer;
  transition: all 0.3s ease;
  backdrop-filter: blur(8px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(0, 0, 0, 0.05);
  height: 2rem;
  width: 4rem;
}

.environment-indicator:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.env-dot {
  width: 0.8rem;
  height: 0.8rem;
  border-radius: 50%;
  animation: pulse 2s infinite;
}

.env-text {
  font-size: 0.6rem;
  font-weight: 600;
  margin-left: 0.1rem;
  letter-spacing: 0.025em;
}

.env-prod .env-dot {
  background-color: #22c55e;
  /* 绿色 - 生产环境 */
  box-shadow: 0 0 0 2px rgba(34, 197, 94, 0.3);
}

.env-prod .env-text {
  color: #16a34a;
}

.env-dev .env-dot {
  background-color: #eab308;
  /* 黄色 - 开发环境 */
  box-shadow: 0 0 0 2px rgba(234, 179, 8, 0.3);
}

.env-dev .env-text {
  color: #ca8a04;
}

/* 脉冲动画 */
@keyframes pulse {
  0% {
    opacity: 1;
  }

  50% {
    opacity: 0.6;
  }

  100% {
    opacity: 1;
  }
}

/* 暗色主题适配 */
@media (prefers-color-scheme: dark) {
  .environment-indicator {
    background: rgba(0, 0, 0, 0.8);
    border-color: rgba(255, 255, 255, 0.1);
  }

  .env-prod .env-text {
    color: #4ade80;
  }

  .env-dev .env-text {
    color: #facc15;
  }
}
</style>