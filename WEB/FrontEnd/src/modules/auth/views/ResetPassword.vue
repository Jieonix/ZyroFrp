<template>
  <Header />
  <div class="flex-container">
    <div class="login-container">
      <h2>忘记密码</h2>
      <input type="text" v-model="Email" placeholder="邮箱" />
      <div class="code-container">
        <input type="text" v-model="code" placeholder="验证码" />
        <button @click="sendCode" :disabled="isCounting || isSending">
          {{ isSending ? "发送中..." : isCounting ? `${countdown}s` : "获取验证码" }}
        </button>
      </div>
      <input type="password" v-model="password" placeholder="新密码" />
      <button @click="register">重置密码</button>
      <p>重置密码失败？<router-link :to="{ name: 'Register' }">注册</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue"
import Header from "@/modules/common/components/Header.vue"
import axios from "axios"
import { validateEmail, validatePassword, validateCode } from '@/modules/auth/utils/validate.js'
import router from "@/router/index.js"



const Email = ref('')
const code = ref('')
const password = ref('')
const isCounting = ref(false)
const countdown = ref(60)
const isSending = ref(false)
const passwordResetErrorCodes = [
  "PASSWORD_RESET_4201",
  "PASSWORD_RESET_4202",
  "PASSWORD_RESET_4203",
  "PASSWORD_RESET_4204",
  "PASSWORD_RESET_4205",
  "PASSWORD_RESET_4206",
  "PASSWORD_RESET_4207",
  "EMAIL_SEND_4301"
];
import { getCurrentInstance } from "vue"
const { proxy } = getCurrentInstance()


const sendCode = async () => {

  if (!validateEmail(Email)) {
    return
  }

  if (isCounting.value || isSending.value) return;

  isSending.value = true

  try {

    const response = await axios.post('/emails/send-verification', {
      toEmail: Email.value,
      type: "RESET_PASSWORD"
    })

    if (passwordResetErrorCodes.includes(response.data.code)) {
      proxy.$message.error(response.data.message)
      return
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

  } catch (error) {

    console.error("验证码发送失败：", error)

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

    const response = await axios.put('/auth/password', {
      email: Email.value,
      newPassword: password.value,
      emailCode: code.value
    })

    if (passwordResetErrorCodes.includes(response.data.code)) {
      proxy.$message.error(response.data.message)
      return
    }

    proxy.$message.success('密码修改成功，请登录...')

    setTimeout(() => {
      router.push({ name: 'Login' })
    }, 100)


  } catch (error) {

    console.error("密码修改失败：", error)

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

.code-container button {
  height: 42px;
  margin-left: 10px;
  padding: 12px;
  background-color: #444;
  color: white;
  border: none;
  cursor: pointer;
  box-sizing: border-box;
}

.code-container button:hover {
  background-color: #555;
  cursor: not-allowed;
  cursor: pointer;
  color: #999;
}


@media (prefers-color-scheme: dark) {

  .code-container button {
    color: #777777;
    background-color: #323232;
  }

}
</style>
