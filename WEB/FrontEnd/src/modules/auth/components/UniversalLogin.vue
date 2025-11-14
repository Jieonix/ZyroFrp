<template>
  <Header />
  <div class="flex-container">
    <div class="login-container">
      <h2>{{ config.title }}</h2>
      <input
        type="text"
        v-model="Email"
        :placeholder="config.emailPlaceholder"
        @keyup.enter="handleLogin"
      />
      <input
        type="password"
        v-model="password"
        :placeholder="config.passwordPlaceholder"
        @keyup.enter="handleLogin"
      />
      <button
        class="btn btn-primary btn-full-width"
        @click="handleLogin"
        :disabled="isLoading"
      >
        {{ isLoading ? '登录中...' : config.loginButtonText }}
      </button>

      <!-- 动态内容区域 -->
      <div class="dynamic-content">
        <template v-if="config.type === 'user'">
          <p><router-link :to="{ name: 'ResetPassword' }">忘记密码?</router-link></p>
          <p>还没有账号? <router-link :to="{ name: 'Register' }">注册</router-link></p>
        </template>
        <template v-else-if="config.type === 'admin'">
          <p>仅管理员帐号可登录</p>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance, computed, onUnmounted } from "vue"
import Header from "@/modules/common/components/Header.vue"
import { post } from '@/modules/common/utils/http.js'
import { validateEmail } from '@/modules/common/utils/validation.js'
import router from "@/router/index.js"
import activityTracker from '@/modules/common/utils/activityTracker.js'

// 定义 props
const props = defineProps({
  type: {
    type: String,
    required: true,
    validator: (value) => ['user', 'admin'].includes(value)
  }
})

const { proxy } = getCurrentInstance()

// 响应式数据
const Email = ref('')
const password = ref('')
const isLoading = ref(false)

// 配置对象
const config = computed(() => {
  if (props.type === 'admin') {
    return {
      title: '管理员登录',
      emailPlaceholder: '管理员邮箱',
      passwordPlaceholder: '管理员密码',
      loginButtonText: '管理员登录',
      apiEndpoint: '/auth/admin_login',
      tokenKey: 'AdminToken',
      errorCodes: ["LOGIN_4101", "LOGIN_4102", "LOGIN_4103", "LOGIN_4104"],
      successRoute: 'Dashboard'
    }
  } else {
    return {
      title: '用户登录',
      emailPlaceholder: '邮箱',
      passwordPlaceholder: '密码',
      loginButtonText: '登录',
      apiEndpoint: '/auth/login',
      tokenKey: 'Token',
      errorCodes: ["LOGIN_4101", "LOGIN_4102", "LOGIN_4103"],
      successRoute: 'UserDashboard'
    }
  }
})


// 登录处理函数
const handleLogin = async () => {
  // 前端验证
  if (!validateEmail(Email)) {
    return
  }

  if (!password.value.trim()) {
    proxy.$message.error('请输入密码')
    return
  }

  isLoading.value = true

  try {
    const response = await post(config.value.apiEndpoint, {
      email: Email.value,
      password: password.value
    }, {}, {
      showMessage: (msg) => proxy.$message.error(msg),
      onAuthError: () => {
        localStorage.removeItem(config.value.tokenKey)
        if (router.currentRoute.value.name !== 'Login' && router.currentRoute.value.name !== 'Admin_Login') {
          router.push({ name: props.type === 'admin' ? 'Admin_Login' : 'Login' })
        }
      }
    })

    // 检查业务错误码
    if (config.value.errorCodes.includes(response.data.code)) {
      proxy.$message.error(response.data.message)
      return
    }

    // 登录成功
    localStorage.setItem(config.value.tokenKey, response.data.data)

    // 启动活跃时间跟踪器
    activityTracker.start()

    // 显示成功消息
    const loginType = props.type === 'admin' ? '管理员' : '用户'
    proxy.$message.success(`${loginType}登录成功`)

    // 延迟跳转，让用户看到成功消息
    setTimeout(() => {
      router.push({ name: config.value.successRoute })
    }, 100)

  } finally {
    isLoading.value = false
  }
}

// 清理函数 - 组件卸载时重置状态
const resetForm = () => {
  Email.value = ''
  password.value = ''
  isLoading.value = false
}

// 组件卸载时清理
onUnmounted(() => {
  resetForm()
})

// 暴露方法给父组件
defineExpose({
  resetForm,
  handleLogin
})
</script>

<style scoped>
/* 使用统一的样式库，无需额外样式 */
.dynamic-content {
  margin-top: 0.9375rem;
  font-size: 0.875rem;
}

.dynamic-content p {
  margin: 0.5rem 0;
}

.dynamic-content a {
  color: #5a9a6d;
  text-decoration: none;
}

.dynamic-content a:hover {
  text-decoration: underline;
}

/* 深色主题适配 */
@media (prefers-color-scheme: dark) {
  .dynamic-content a {
    color: #727272;
  }
}

/* 加载状态样式 */
button:disabled {
  cursor: not-allowed;
  opacity: 0.7;
}
</style>