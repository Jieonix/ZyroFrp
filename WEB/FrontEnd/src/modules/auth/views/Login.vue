<template>
  <Header />
  <div class="flex-container">
    <div class="login-container">
      <h2>登录</h2>
      <input type="text" v-model="Email" placeholder="邮箱" />
      <input type="password" v-model="password" placeholder="密码" />
      <button @click="login">登录</button>
      <p><router-link :to="{ name: 'ResetPassword' }">忘记密码?</router-link></p>
      <p>还没有账号? <router-link :to="{ name: 'Register' }">注册</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref, getCurrentInstance } from "vue"
import Header from "@/modules/common/components/Header.vue"
import axios from "axios"
import qs from 'qs'
import { validateEmail } from '@/modules/auth/utils/validate.js'
import router from "@/router/index.js"
import activityTracker from '@/modules/common/utils/activityTracker.js'

const { proxy } = getCurrentInstance()

const Email = ref('')
const password = ref('')
const loginErrorCodes = [
  "LOGIN_4101",
  "LOGIN_4102",
  "LOGIN_4103"
];

const login = async () => {

  if (!validateEmail(Email)) {
    return
  }

  try {

    const response = await axios.post("/auth/login", {
      email: Email.value,
      password: password.value
    })

    if (loginErrorCodes.includes(response.data.code)) {
      proxy.$message.error(response.data.message)
      return
    }

    localStorage.setItem("Token", response.data.data)

    // 登录成功后启动活跃时间跟踪器
    activityTracker.start()

    setTimeout(() => {
      router.push({ name: 'UserDashboard' })
    }, 100)

  } catch (error) {}
}

</script>

<style scoped>
</style>
