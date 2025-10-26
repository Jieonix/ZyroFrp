<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="auth">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以查看 FRPC 客户端使用教程</p>
      </section>
      <section class="features">
        <div class="feature-box fb1">
          <div class="h3">
            <h3>使用帮助</h3>
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm" class="icon" />
            </strong>
            <p class="tooltip"><strong>创建隧道</strong></p>
          </div>

          <div class="arrow">
            |
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm" class="icon" />
            </strong>
            <p class="tooltip"><strong>软件下载</strong></p>
          </div>

          <div class="arrow">
            |
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm" class="icon" />
            </strong>
            <p class="tooltip"><strong>启动隧道</strong></p>
          </div>

          <div class="arrow">
            |
          </div>

          <div class="line">
            <strong class="dowm">
              <SvgIcon name="Confirm2" class="icon2" />
            </strong>
            <p class="tooltip"><strong>开始使用</strong></p>
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
  name: 'auth',
  components: {
    Header,
    Sidebar,
    Footer
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
.fb1 {
  width: calc(100vw - 18rem - 2rem);
  padding: 3rem;
  margin: 1rem;
}

h3 {
  font-size: 2rem;
  font-weight: 500;
  margin-bottom: 10px;
}

pre {
  background-color: #f4f4f4;
  padding: 10px;
  border-radius: 5px;
  border: 1px solid #ddd;
  font-family: Consolas, monospace;
  margin: 20px 0;
  white-space: pre-wrap;
  word-wrap: break-word;
  width: 10vw;
  padding-left: 20px;
}

code {
  color: #e83e8c;
  font-size: 0.9rem;
}

strong {
  font-size: 1.5em;
  font-weight: 350;
  cursor: pointer;
}

section.features {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.h3 {
  display: flex;
  align-items: end;
  margin-bottom: 40px;
}

.h3 p {
  margin-bottom: 16px;
}

.tooltip {
  position: relative;
  cursor: pointer;
  display: inline-block;
}

.tooltip .tooltiptext {
  visibility: hidden;
  min-width: 500px;
  background-color: #ebebeb;
  color: #1c1c1c;
  text-align: left;
  padding: 12px;
  border-radius: 6px;
  font-size: 14px;
  line-height: 1.5;
  position: absolute;
  z-index: 99;
  bottom: 120%;
  left: 0;
  opacity: 0;
  transition: opacity 0.3s;
}

.tooltip:hover .tooltiptext {
  visibility: visible;
  opacity: 1;
}

strong.dowm {
  display: flex;
}

strong {
  font-size: 1rem;
}

.icon {
  color: #63e2b7;
  width: 3rem;
  height: 3rem;
  margin-right: 1rem;
}

.icon2 {
  color: #63e2b7;
  width: 2.74rem;
  height: 2.74rem;
  margin-right: 1rem;
  margin-left: 0.2rem;
}

.line {
  display: flex;
  align-items: center;
}

.arrow {
  font-size: 2rem;
  margin-left: 1.3rem;
  color: #63e2b7;
}

@media (prefers-color-scheme: dark) {
  p {
    color: #a3a3a3;
  }

  .fb1 {
    background-color: #18181c;
  }
}
</style>
