<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="auth">
    <Loading />
    <Header />
    <div class="container">
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
              <p>（ 鼠标悬停展示详细步骤 ）</p>
            </div>

            <p class="tooltip"><strong>1. 添加隧道</strong>
              <span class="tooltiptext">完成实名认证后，进入“添加隧道”页面。在此页面中，您可以创建新的 FRP
                隧道。填写隧道的名称、类型、端口等信息，确保填写正确的本地和远程端口。提交后，系统将生成一个专属的配置文件。</span>
            </p>
            <strong class="dowm">↓</strong>

            <p class="tooltip"><strong>2. 复制配置文件</strong>
              <span
                class="tooltiptext">在“配置文件”页面，您可以看到系统为您的隧道自动生成的配置文件。点击复制按钮，将配置文件中的内容复制到您的设备中。请注意，配置文件中的信息包含您的隧道参数，确保正确保存。</span>
            </p>
            <strong class="dowm">↓</strong>

            <p class="tooltip"><strong>3. 配置 FRPC 客户端</strong>
              <span class="tooltiptext">下载并解压 FRPC 客户端后，在客户端的目录下找到 <code>frpc.ini</code>
                配置文件。打开该文件，并将刚才复制的配置内容粘贴进去，确保配置项完整且无误。</span>
            </p>
            <strong class="dowm">↓</strong>

            <p class="tooltip"><strong>4. 启动 FRPC 客户端</strong>
              <span class="tooltiptext">完成配置后，打开终端（Windows 用 cmd，Linux/macOS 用终端），切到 FRPC 所在目录，执行
                <code>./frpc -c frpc.ini</code> 启动。启动后即可连接服务端，在平台查看流量。</span>
            </p>
            <br><br><br><br><br>

            <p class="tooltip"><strong>👋 有问题？反馈问题</strong>
              <span class="tooltiptext">如果遇到 BUG 或问题，欢迎加入我们的QQ群：738146595 反馈，我们会第一时间处理。</span>
            </p>
          </div>
        </section>
        <Footer />
      </main>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import Sidebar from '@/components/Sidebar.vue';
import Footer from '@/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '../utils/token.js';
import { useLoadingStore } from '@/stores/loading'


export default {
  name: 'auth',
  components: {
    Header,
    Sidebar,
    Footer
  },
  methods: {
    checkTokenValidity() {
      const router = useRouter();
      const token = localStorage.getItem("Token");
      if (!validateToken(router, token)) {
        return;
      }
    },
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
  margin-bottom: 16.5rem;
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
  display: block;
  text-align: left;
  font-size: 2em;
  margin: 20px 0;
}

@media (prefers-color-scheme: dark) {

  body {
    background-color: #1c1c1c;
    color: #e0e0e0;
  }

  .main-content {
    background-color: #101014;
  }

  h2 {
    color: #ededed;
  }

  .feature-box {
    background-color: #18181c;
    border: 1px solid #232323;
  }

  p {
    color: #a3a3a3;
  }

  pre {
    background-color: #555555;
    border: 1px solid #6a6a6a;
  }

  code {
    color: #5ccea8;
    font-size: 0.9rem;
  }

}
</style>
