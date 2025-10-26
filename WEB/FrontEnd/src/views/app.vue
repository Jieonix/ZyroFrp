<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="app">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以下载 FRPC 客户端，以使用本服务</p>
      </section>
      <section class="features">
        <div class="feature-box fb1">
          <h2>MacOS</h2>
          <div class="version-description">
            <a class="version" :href="`${BASE_URL}frpc/macos/frpc-macos-amd64.tar.gz`">
              ZyroFrp_darwin_amd64
            </a>
            <p class="description">适用于 MacOS AMD64 版本</p>
          </div>
          <div class="version-description">
            <a class="version" :href="`${BASE_URL}frpc/macos/frpc-macos-arm64.tar.gz`">
              ZyroFrp_darwin_arm64
            </a>
            <p class="description">适用于 MacOS ARM64 版本</p>
          </div>
        </div>

        <div class="feature-box fb2">
          <h2>Linux</h2>
          <div class="version-description">
            <a class="version" :href="`${BASE_URL}frpc/linux/frpc-linux-amd64.tar.gz`">
              ZyroFrp_linux_amd64
            </a>
            <p class="description">适用于 Linux AMD64 版本</p>
          </div>
          <div class="version-description">
            <a class="version" :href="`${BASE_URL}frpc/linux/frpc-linux-arm64.tar.gz`">
              ZyroFrp_linux_arm64
            </a>
            <p class="description">适用于 Linux ARM64 版本</p>
          </div>
        </div>

        <div class="feature-box fb3">
          <h2>Windows</h2>
          <div class="version-description">
            <a class="version" :href="`${BASE_URL}frpc/windows/frpc-windows-amd64.tar.gz`">
              ZyroFrp_win_amd64
            </a>
            <p class="description">适用于 Windows AMD64 版本</p>
          </div>
          <div class="version-description">
            <a class="version" :href="`${BASE_URL}frpc/windows/frpc-windows-arm64.tar.gz`">
              ZyroFrp_win_arm64
            </a>
            <p class="description">适用于 Windows ARM64 位版本</p>
          </div>
        </div>
      </section>

      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '../utils/token.js';
import { useLoadingStore } from '@/stores/loading'
import { commonMethods } from './shared/common.js'
import './shared/common.css'


export default {
  name: 'app',
  components: {
    Header,
    Sidebar,
    Footer
  },
  data() {
    return {
      BASE_URL: 'https://zyroo.cn/backend/static/'
    }
  },
  methods: {
    ...commonMethods,
  },
  mounted() {
    this.checkTokenValidity();
  }
}
</script>

<style scoped>
.features {
  display: block;
  width: 84vw;
}

.fb1,
.fb2,
.fb3 {
  margin: 1rem;
  width: calc(100vw - 18rem - 2rem);
  display: flex;
  flex-direction: column;
}

.fb3 {
  margin-bottom: 1rem;
}

.version {
  text-decoration: none;
  color: #1eac71;
  font-weight: 700;
  margin: 5px 0;
  transition: color 0.3s ease;
}

.version:hover {
  color: #009525;
}

.discription {
  color: #666;
}

.version-description {
  margin-bottom: 20px;
}

h2 {
  margin-bottom: 10px;
}

@media (prefers-color-scheme: dark) {

  .fb1,
  .fb2,
  .fb3 {
    background-color: #18181c;
  }
}
</style>
