<template>
  <Header />
  <div class="flex-container">
    <div class="login-container">
      <h2>注册</h2>
      <input type="text" v-model="Email" placeholder="邮箱" />
      <div class="code-container">
        <input type="text" v-model="code" placeholder="验证码" />
        <button class="code-button" @click="sendCode" :disabled="isCounting || isSending">
          {{ isSending ? "发送中..." : isCounting ? `${countdown}s` : "获取验证码" }}
        </button>
      </div>
      <input type="password" v-model="password" placeholder="密码" />
      <button class="btn btn-primary btn-full-width" @click="register">注册</button>
      <p>已有账号? <router-link :to="{ name: 'Login' }">登录</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue"
import Header from "@/modules/common/components/Header.vue"
import { post } from '@/modules/common/utils/http.js'
import { validateEmail, validatePassword, validateCode } from '@/modules/common/utils/validation.js'
import router from "@/router/index.js"

const { proxy } = getCurrentInstance()

const Email = ref('')
const code = ref('')
const password = ref('')
const isCounting = ref(false)
const countdown = ref(60)
const isSending = ref(false)
const errorCodes = [
  "REGISTER_4001",
  "REGISTER_4002",
  "REGISTER_4003",
  "REGISTER_4004",
  "REGISTER_4005",
  "REGISTER_4006",
  "EMAIL_SEND_4301"
];

const sendCode = async () => {

  if (!validateEmail(Email)) {
    return
  }

  if (isCounting.value || isSending.value) return;

  isSending.value = true

  try {
    const response = await post('/emails/send-verification', {
      toEmail: Email.value,
      type: "REGISTER"
    }, {}, {
      showMessage: (msg) => proxy.$message.error(msg),
      defaultMessage: '验证码发送失败'
    })

    if (errorCodes.includes(response.data.code)) {
      proxy.$message.error(response.data.message)
      return;
    }

    isCounting.value = true
    let timeLeft = countdown.value
    const timer = setInterval(() => {
      timeLeft--
      countdown.value = timeLeft
      if (timeLeft <= 0) {
        clearInterval(timer)
        isCounting.value = false
        countdown.value = 60
      }
    }, 1000);

  } finally {
    isSending.value = false
  }
}

const register = async () => {

  if (!validateEmail(Email)) {
    return
  }

  if (!validatePassword(password)) {
    return
  }

  if (!validateCode(code)) {
    return
  }

  try {

    const response = await post('/auth/register', {
      email: Email.value,
      password: password.value,
      emailCode: code.value
    }, {}, {
      showMessage: (msg) => proxy.$message.error(msg),
      defaultMessage: '注册失败'
    })

    if (errorCodes.includes(response.data.code)) {
      proxy.$message.error(response.data.message)
      return
    }

    proxy.$message.success('注册成功,请登录...')

    setTimeout(() => {
      router.push({ name: 'Login' })
    }, 100)

  }
}

</script>


<style scoped>
.code-container {
  display: flex;
  justify-content: space-between;
}

.code-container input {
  width: 200px;
}

/* 使用统一的按钮样式库 */
</style>
