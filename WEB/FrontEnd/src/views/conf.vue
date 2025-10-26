<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="conf">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以复制你创建的隧道所生成的配置文件，以供在客户端使用</p>
      </section>
      <section class="config-section">
        <h2 class="conf-h2">隧道配置</h2>
        <div class="tabs">
          <button class="tabs-btn active">按节点</button>
          <button class="tabs-btn">按隧道</button>
        </div>
        <select v-model="selectedServer" @change="fetchConfig" id="select-server">
          <option v-for="server in servers" :key="server.id" :value="server.name">
            {{ server.name }}
          </option>
        </select>
        <pre class="config-code" rows="20" cols="100">
{{ configFile }}
         </pre>
        <button @click="copy_button" class="copy">复制</button>
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
import axios from 'axios';
import { useLoadingStore } from '@/stores/loading'
import { commonMethods } from './shared/common.js'
import './shared/common.css'


export default {
  name: 'conf',
  components: {
    Header,
    Sidebar,
    Footer
  },
  data() {
    return {
      servers: [],
      selectedServer: '',
      configData: [],
      configFile: '',
      server_addr: '',
      server_port: '',
      userKey: '',
      server_ip: '',
      server_port: '',
      userInfo: []
    }
  },
  methods: {
    ...commonMethods,
    async getServers() {
      try {
        const Token = localStorage.getItem('Token');
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.get('/servers')
        loadingStore.hideLoading();
        this.servers = response.data.data

        if (this.servers.length > 0) {
          this.selectedServer = this.servers[0].name;
          this.fetchConfig();
        }

      } catch (error) {
        console.error(error)
      }
    },
    async getUserInfo() {
      try {
        const token = localStorage.getItem('Token')
        const response = await axios.post('/users/info', {
          token
        });
        this.userInfo = response.data.data;
        this.userKey = this.userInfo.user_key;
      } catch (error) {

      }
    },
    async fetchConfig() {
      try {
        const Token = localStorage.getItem('Token')
        const response = await axios.get(`/frp-tunnels/by-server?server_name=${this.selectedServer}`,
          {
            headers: {
              'Token': Token
            }
          })

        this.configData = response.data.data;

        this.generateConfigFile();

      } catch (error) {
        console.error('获取配置失败:', error);
      }
    },
    generateConfigFile() {
      this.servers.forEach(server => {
        if (server.name === this.selectedServer) {
          this.server_ip = server.ip_address;
          this.server_port = server.port;
          return;
        }
      });

      let configContent = `### 此 ZyroFrpc 为业务定制版本\n`;
      configContent += `### 此版本仅能与ZyroFrp内网穿透服务平台适配\n\n`;
      configContent += `# serverAddr为必填项-服务器ip/服务器域名\n`;
      configContent += `serverAddr = "${this.server_ip}"\n`;
      configContent += `# serverPort为必填项-服务器内网穿透专用端口\n`;
      configContent += `serverPort = ${this.server_port}\n`;
      configContent += `# userKey为必填项\n`;
      configContent += `userKey = "${this.userKey}"\n\n`;

      if (this.configData && this.configData.length > 0) {
        this.configData.forEach(tunnel => {
          configContent += `[[proxies]]\n`;
          configContent += `name = "${tunnel.tunnel_name}"\n`;
          configContent += `type = "${tunnel.tunnel_type.toLowerCase()}"\n`;
          configContent += `localIP = "${tunnel.local_ip}"\n`;
          configContent += `localPort = ${tunnel.local_port}\n`;

          if (tunnel.tunnel_type.toLowerCase() === 'tcp' || tunnel.tunnel_type.toLowerCase() === 'udp') {
            configContent += `remotePort = ${tunnel.remote_port}\n\n`;
          } else if (tunnel.tunnel_type.toLowerCase() === 'http' || tunnel.tunnel_type.toLowerCase() === 'https') {
            configContent += `customDomains = ["${tunnel.custom_domain}"]\n\n`;
          } else if (tunnel.tunnel_type.toLowerCase() === 'stcp' || tunnel.tunnel_type.toLowerCase() === 'xtcp') {
            configContent += `secretKey = "${tunnel.secret_key}"\n\n`;
          }
        });
      }

      this.configFile = configContent;
    },
    copy_button() {
      const copyTextarea = document.createElement('textarea');
      copyTextarea.value = this.configFile;
      document.body.appendChild(copyTextarea);

      copyTextarea.select();
      copyTextarea.setSelectionRange(0, 99999);

      navigator.clipboard.writeText(copyTextarea.value).then(() => {
        this.$message.success("复制成功！");
      }).catch((error) => {
        console.error('复制失败:', error);
        this.$message.error("复制失败，请重试");
      });

      document.body.removeChild(copyTextarea);
    },
  },
  mounted() {
    this.checkTokenValidity()
    this.getServers()
    this.getUserInfo()
  },
  watch: {
    selectedServer(newServer) {
      if (newServer) {
        this.fetchConfig();
      }
    }
  }
}
</script>


<style scoped>
.config-section {
  border: 1px solid #e0e0e0;
  padding: 30px;
  margin: 1rem;
  box-sizing: border-box;
  border-radius: 5px;
}

.conf-h2 {
  margin-left: 30px;
  margin-bottom: 20px;
  margin-top: 20px;
}

.tabs-btn {
  font-size: 15px;
  padding: 6px 3px;
  margin-right: 10px;
  border: none;
  border-bottom: 3px solid #e0e0e0;
  background-color: #fff;
  cursor: pointer;
  margin-bottom: 30px;
}

.tabs-btn:nth-child(1) {
  margin-right: 30px;
  margin-left: 30px;
}

.tabs-btn.active {
  border-bottom: 3px solid #42997c;
}

.copy {
  width: 5rem;
  height: 2.3rem;
  padding: 0.2rem 0.4rem;
  background-color: #42997c;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  border-radius: 5px;
  font-weight: 700;
  transition: all 0.3s;
  margin-left: 15px;
  font-size: 1.1rem;
}

.copy:hover {
  background-color: #5acaa5;
}

#select-server {
  width: 100rem;
  height: 2.3rem;
  padding: 0.2rem 0.4rem;
  margin-left: 20px;
  margin-bottom: 30px;
  color: #181818;
  border-color: #d8d8d8;
  border-radius: 5px;
  font-size: 1rem;
}

#select-server:focus {
  outline: none;
}

.config-code {
  min-height: 42rem;
  overflow-y: auto;
  margin-left: 20px;
}

/* 特定于conf页面的深色主题样式 */
@media (prefers-color-scheme: dark) {
  .tabs-btn {
    background-color: #18181c;
    color: #e0e0e0;
    border-bottom: 3px solid #18181c;
  }

  .tabs-btn.active {
    border-bottom: 3px solid #236659;
  }

  #select-server {
    background-color: #1a1a20;
    border: 1px solid #232323;
    color: #dddddd;
  }

  .config-section {
    border: 1px solid #232323;
    background-color: #18181c;
  }
}
</style>
