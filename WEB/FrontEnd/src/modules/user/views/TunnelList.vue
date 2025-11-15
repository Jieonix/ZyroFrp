<script setup>
import Loading from '@/modules/common/components/Loading.vue'
</script>

<template>
  <div class="tunnels">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以管理 FRP 隧道，实现编辑、删除、停用等操作</p>
      </section>
      <ul class="tunnels2">
        <li v-for="tunnel in tunnels" :key="tunnel.id" class="tunnels-li">
          <div class="feature-box">
            <h2 class="feature-title">{{ tunnel.tunnel_name }}</h2>
            <div class="feature-tags">
              <p class="tag-green">{{ tunnel.tunnel_type }}</p>
              <p class="tag-red">{{ tunnel.server_name }}</p>
            </div>
            <div class="feature-middle">
              <div class="left">
                <p v-if="tunnel.tunnel_type === 'tcp' || tunnel.tunnel_type === 'udp'" class="port">远程端口：</p>
                <p v-if="tunnel.tunnel_type === 'http' || tunnel.tunnel_type === 'https'" class="domain">域名：</p>
                <p v-if="tunnel.tunnel_type === 'xtcp' || tunnel.tunnel_type === 'stcp'">密钥：</p>
              </div>
              <div class="right">
                <p v-if="tunnel.tunnel_type === 'tcp' || tunnel.tunnel_type === 'udp'" class="port">{{
                  tunnel.remote_port }}</p>
                <p v-if="tunnel.tunnel_type === 'http' || tunnel.tunnel_type === 'https'" class="domain">{{
                  tunnel.custom_domain }}</p>
                <p v-if="tunnel.tunnel_type === 'xtcp' || tunnel.tunnel_type === 'stcp'">{{ tunnel.secret_key }}</p>
              </div>
            </div>
            <p class="feature-ip">节点IP：{{ tunnel.server_ip }}</p>
            <div class="feature-footer">
              <button @click="openEditBox(tunnel)">编辑</button>
              <button @click="deleteTunnel(tunnel)">删除</button>
            </div>
          </div>
        </li>
      </ul>
      <Footer />
    </main>
  </div>
  <div v-if="isEditBoxVisible" class="edit-box-overlay" @click="closeEditBox">
    <div class="edit-box" @click.stop>
      <h3>编辑隧道</h3>
      <form>
        <label for="Servers">服务器</label>
        <select name="Servers" id="Servers" v-model="selectedServer">
          <option v-for="server in servers" :key="server.id" :value="server.name">
            {{ server.name }}
          </option>
        </select>
        <label for="tunnel-name">隧道名称</label>
        <input autocomplete="off" type="text" id="tunnel_name" v-model="tunnel_name">
        <label for="tunnel_setting">隧道设置</label>
        <select name="Protocol" id="Protocol" v-model="selectedProtocol">
          <option v-for="protocol in protocols" :key="protocol" :value="protocol">
            {{ protocol }}
          </option>
        </select>
        <label for="local_ip">内网IP</label>
        <input autocomplete="off" type="text" id="local_ip" v-model="local_ip">
        <div class="port_div">
          <div class="port">
            <label for="local_port">内网端口</label>
            <input autocomplete="off" type="text" id="local_port" v-model="local_port">
          </div>
          <div class="port" v-if="selectedProtocol === 'TCP' || selectedProtocol === 'UDP'">
            <label for="remote-port" class="form-label">远程端口</label>
            <input autocomplete="off" class="border-color" type="text" id="remote_port" name="remote_port"
              v-model="remote_port" placeholder="">
          </div>

          <div class="port" v-if="selectedProtocol === 'HTTP' || selectedProtocol === 'HTTPS'">
            <label for="custom-domains" class="form-label">自定义域名</label>
            <input autocomplete="off" class="border-color" type="text" id="custom_domains" name="custom_domains"
              v-model="custom_domain" placeholder="">
          </div>

          <div class="port" v-if="selectedProtocol === 'XTCP' || selectedProtocol === 'STCP'">
            <label for="secret-key" class="form-label">密钥</label>
            <input autocomplete="off" class="border-color" type="text" id="secret_key" name="secret_key"
              v-model="secret_key" placeholder="">
          </div>
        </div>
        <div class="button_div">
          <button type="button" @click="save">保存</button>
          <button type="button" @click="closeEditBox">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import Sidebar from '@/modules/user/components/Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import axios from 'axios';
import { useLoadingStore } from '@/modules/common/stores/loading'
import { commonMethods } from '@/modules/common/utils/common.js'
import '@/modules/common/assets/styles/common.css'


export default {
  name: 'tunnels',
  components: {
    Header,
    Sidebar,
    Footer
  },
  data() {
    return {
      tunnels: [],
      tunnel_id: '',
      isEditBoxVisible: false,
      selectedTunnel: {},
      selectedServer: '',
      servers: [],
      protocols: ['TCP', 'UDP', 'HTTP', 'HTTPS', 'XTCP', 'STCP'],
      selectedProtocol: '',
      remote_port: '',
      custom_domain: '',
      secret_key: '',
      tunnel_name: '',
      local_ip: '',
      local_port: '',
    }
  },
  methods: {
    ...commonMethods,
    async getTunnels() {
      try {
        const Token = localStorage.getItem('Token');
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.get('/frp-tunnels', {
          headers: {
            'Token': Token
          }
        })

        loadingStore.hideLoading();

        this.tunnels = response.data.data


      } catch (error) {
        console.error(error)
      }
    },
    async openEditBox(tunnel) {
      this.isEditBoxVisible = true;

      try {
        const response = await axios.get('/servers');
        this.servers = response.data.data;
      } catch (error) {
        console.error("获取服务器列表失败", error);
      }

      this.selectedServer = tunnel.server_name;
      this.selectedProtocol = tunnel.tunnel_type;
      this.tunnel_name = tunnel.tunnel_name;
      this.local_ip = tunnel.local_ip;
      this.local_port = tunnel.local_port;
      this.tunnel_id = tunnel.id;

      if (this.selectedProtocol === 'HTTP' || this.selectedProtocol === 'HTTPS') {
        this.custom_domain = tunnel.custom_domain;
      } else if (this.selectedProtocol === 'XTCP' || this.selectedProtocol === 'STCP') {
        this.secret_key = tunnel.secret_key;
      } else {
        this.remote_port = tunnel.remote_port;
      }
    },
    closeEditBox() {
      this.isEditBoxVisible = false;
    },
    save(tunnel) {
      const id = this.tunnel_id
      const token = localStorage.getItem('Token')

      let tunnelData = {
        server_name: this.selectedServer,
        tunnel_name: this.tunnel_name,
        tunnel_type: this.selectedProtocol.toLowerCase(),
        local_ip: this.local_ip,
        local_port: this.local_port
      };

      if (this.selectedProtocol === 'HTTP' || this.selectedProtocol === 'HTTPS') {
        tunnelData.custom_domain = this.custom_domain;
      } else if (this.selectedProtocol === 'XTCP' || this.selectedProtocol === 'STCP') {
        tunnelData.secret_key = this.secret_key;
      } else {
        tunnelData.remote_port = this.remote_port;
      }

      const response = axios.put(`/frp-tunnels/${id}`, {
        token,
        frpTunnel: tunnelData
      })
        .then(response => {
          this.$message.success(response.data.message);

          setTimeout(() => {
            this.isEditBoxVisible = false;
          }, 1000)

          setTimeout(() => {
            this.getTunnels();
          }, 3000);
        })
        .catch(error => {
          console.error('更新隧道失败', error);
        });
    },
    deleteTunnel(tunnel) {
      const token = localStorage.getItem('Token');

      axios.delete(`/frp-tunnels/${tunnel.id}`, {
        data: { token }
      })
        .then(response => {
          this.$message.success(response.data.message);

          setTimeout(() => {
            this.getTunnels();
          }, 3000);
        })
        .catch(error => {
          console.error("删除失败:", error.response?.data || error.message);
        });
    },
  },
  mounted() {
    this.checkTokenValidity();
    this.getTunnels();
  }
}
</script>

<style scoped>
.tunnels2 {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 30px;
  width: 100%;
  height: 100%;
  justify-items: center;
  margin: 0 auto;
  padding: 0 2rem;
  box-sizing: border-box;
  list-style: none;
  padding-bottom: 3.5rem;
}

.tunnels-li {
  height: 300px;
  box-sizing: border-box;
  list-style: none;
  margin: 0;
  padding: 0;
  width: 100%;
}

.qqq {
  padding: 0 2rem;
  padding-bottom: 10rem;
}

.feature-box {
  position: relative;
  width: 16rem;
  height: 19rem;
  border: 1px solid #e6e6e6;
  border-radius: 5px;
  background-color: #fff;
  padding: 0;
  margin: 0 auto;
  box-sizing: border-box;
}

.feature-title {
  font-weight: 700;
  font-size: 25px;
  margin: 20px 20px;
}

.feature-tags {
  display: flex;
  margin: 20px 20px;
}

.tag-green {
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e3f3eb;
  border-radius: 3px 0 0 3px;
  padding: 2px 6px;
  color: #27a663;
  font-size: 13px;
  font-weight: 600;
}

.tag-red {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 28px;
  background-color: #e4f0fd;
  border-radius: 0 3px 3px 0;
  padding: 2px 6px;
  font-size: 13px;
  font-weight: 600;
  color: #2080f0;
}

.feature-ip {
  margin: 0 1rem;
  font-weight: 500;
}

.feature-footer {
  width: 100%;
  height: 3rem;
  background-color: #f5f5f584;
  border-radius: 0 0 5px 5px;
  position: absolute;
  bottom: 0;
  display: flex;
  align-items: center;
  gap: 10px;
  padding-left: 18px;
}

.feature-footer button {
  background-color: #d6ece1;
  width: 4.5rem;
  height: 2rem;
  border: none;
  border-radius: 3px;
  cursor: pointer;
  color: #27a663;
  transition: all 0.3s;
  font-size: 0.8rem;
}

.feature-footer button:hover {
  background-color: #cbeadb;
}

.edit-box-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(234, 234, 234, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.edit-box {
  background-color: white;
  border-radius: 10px;
  padding: 20px;
  width: 400px;
  max-width: 90vw;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  text-align: center;
  margin: 0 auto;
}

.edit-box h3 {
  font-size: 20px;
  color: #333;
}

.edit-box form {
  display: flex;
  flex-direction: column;
}

.edit-box label {
  margin: 10px 0 5px;
  text-align: left;
  font-weight: bold;
  font-size: 14px;
  color: #555;
}

.edit-box input {
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.edit-box button {
  background-color: #42997c;
  color: white;
  padding: 0.2px;
  border: none;
  cursor: pointer;
  border-radius: 5px;
  margin-top: 10px;
}

.edit-box button:hover {
  background-color: #5acaa5;
}

.edit-box button[type="button"] {
  background-color: #42997c;
  transition: all 0.3s;
}

.edit-box button[type="button"]:hover {
  background-color: #5acaa5;
}

#tunnel_name,
#tunnel_setting,
#local_ip,
#local_port,
#remote_port,
#Protocol,
#Servers {
  height: 37px;
  border-radius: 5px;
  outline: none;
  border: 1px solid #cccccc;
  padding: 0 10px;
  margin: 0;
}

#local_port,
#remote_port {
  width: 100%;
}

#tunnel-name {
  margin-bottom: 0;
  padding: 0 10px;
  height: 37px;
}

.port_div {
  display: flex;
  justify-content: space-between;
}

.port {
  display: flex;
  flex-direction: column;
}

.button_div {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  margin-bottom: 1rem;
  height: 3rem;
}

.button_div button {
  width: 48%;
}

.feature-middle {
  display: flex;
  margin: 4.5rem 1rem 0.5rem 1rem;
}

.feature-middle .left p,
.feature-middle .right p {
  font-weight: 500;
}

@media (prefers-color-scheme: dark) {
  .tag-green {
    background-color: #0a5831;
    color: #9dd7b8;
  }

  .tag-red {
    background-color: #0a2b58;
    color: #b0b6dd;
  }

  .feature-footer {
    background-color: #262629;
  }

  .feature-footer button {
    background-color: #236659;
    color: #c9e9d8;
  }

  .feature-footer button:hover {
    background-color: #195549;
  }

  .edit-box-overlay {
    background: #18181c94;
  }

  .edit-box {
    background-color: #24242a;
  }

  .edit-box h3 {
    color: #d4d4d4;
  }

  .edit-box label {
    color: #e0e0e0;
  }

  .edit-box input {
    background-color: #1a1a20;
    border: 1px solid #353543 !important;
    color: #d4d4d4;
  }

  .edit-box input:focus {
    border: 1px solid #353543;
    outline: none;
  }

  .edit-box input::placeholder {
    color: #7f7f7f37;
  }

  .edit-box select {
    background-color: #1a1a20;
    border: 1px solid #353543 !important;
    color: #d4d4d4;
  }

  .edit-box select:focus {
    border: 1px solid #353543;
    outline: none;
  }

  .edit-box button[type="button"] {
    background-color: #2f8360;
  }

  .edit-box button[type="button"]:hover {
    background-color: #28684f;
  }

  .feature-box {
    background-color: #18181c;
    border-color: #232323;
  }

  .feature-ip {
    color: #b0b0b0;
  }
}
</style>
