<template>
  <Header />
  <div class="flex-container">
    <div class="login-container">
      <h2>登录</h2>
      <input type="text" v-model="Email" placeholder="邮箱" />
      <input type="password" v-model="password" placeholder="密码" />
      <button @click="login">登录</button>
      <p>仅管理员帐号可登录</p>
    </div>
    <MessageBox v-if="showMessageBox" :message="messageBoxContent" :type="messageBoxType" @close="handleCloseMessageBox" />
  </div>
</template>

<script setup>
import { ref } from "vue"
import Header from "@/components/Header.vue"
import axios from "axios"
import qs from 'qs'
import { validateEmail } from '../../utils/validate.js'
import router from "@/router/index.js"
import MessageBox from '@/components/MessageBox.vue';


const Email = ref('')
const password = ref('')
const loginErrorCodes = [
  "LOGIN_4101",
  "LOGIN_4102",
  "LOGIN_4103",
  "LOGIN_4104"
];
const showMessageBox = ref(false)
const messageBoxContent = ref('')
const messageBoxType = ref('info')


const login = async () => {

  if (!validateEmail(Email)) {
    return
  }

  try {

    const response = await axios.post("/auth/admin_login", {
      email: Email.value,
      password: password.value
    })

    if (loginErrorCodes.includes(response.data.code)) {
      showMessageBox.value = true
      messageBoxContent.value = response.data.message
      messageBoxType.value = 'error'
      autoCloseMessageBox()
      return
    }

    localStorage.setItem("AdminToken", response.data.data)

    setTimeout(() => {
      router.push('/Admin_Home')
    }, 100)

  } catch (error) {}
}

const handleCloseMessageBox = () => {
  showMessageBox.value = false;
}

const autoCloseMessageBox = () => {
  setTimeout(() => {
    showMessageBox.value = false;
  }, 3000);
}

</script>

<style scoped>
</style>
