<script setup>
import Loading from '@/modules/common/components/Loading.vue'
  ;
</script>

<template>
  <div class="add_tunnels">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以创建 FRP 隧道，各种隧道皆可创建</p>
      </section>
      <section class="add-tunnel">
        <h1>添加隧道</h1>
        <form @submit.prevent="handleSubmit">
          <div class="form-group">
            <label for="server" class="form-label">选择服务器</label>
            <select class="border-color" id="server" name="server" v-model="selectedServer"
              @change="handleServerChange">
              <option v-for="server in lists" :key="server.id" :value="server.name">{{ server.name }}</option>
            </select>
            <div class="big">
              <div class="vip" :class="vipClass" v-if="vipOnly">{{ vip }}</div>
              <div class="vip" :class="vipClass" v-else>{{ no_vip }}</div>
              <div class="udp" :class="udpClass">{{ udp }}</div>
              <div class="website" :class="websiteClass">{{ web }}</div>
              <div class="traffic" :class="trafficClass">{{ traffic }}</div>
              <div class="status" :class="statusClass">{{ s_status }}</div>
            </div>
            <div class="tips">
              <h1>状态颜色含义：</h1>
              <div class="tip_1">
                <div class="one"></div>
                <p class="tips_p">vip用户专用节点</p>
              </div>
              <div class="tip_2">
                <div class="two"></div>
                <p class="tips_p">通用节点、服务器在线、允许的服务</p>
              </div>
              <div class="tip_3">
                <div class="three"></div>
                <p class="tips_p">服务器离线、不允许的服务</p>
              </div>
            </div>
            <div class="ServerInfo">
              <p class="title">服务器信息：</p>
              <p class="content"><b class="left">服务器名：</b>{{ server_name }}</p>
              <p class="content"><b class="left">服务器简介：</b>{{ server_Info }}</p>
              <p class="content"><b class="left">服务器IP：</b>{{ server_ip }}</p>
              <p class="content"><b class="left">服务器域名：</b>{{ server_domain }}</p>
              <p class="content"><b class="left">服务器用户数量：</b>{{ server_current_clients }} / {{ server_max_clients }} 人
              </p>
              <p class="content"><b class="left">当前隧道数量：</b>{{ server_current_tunnels }} 条</p>
              <p class="content"><b class="left">可用端口范围：</b>{{ server_portRange }}</p>
            </div>
          </div>

          <div class="form-group fg1">
            <div class="form-group-div-01">
              <label for="tunnel-name" class="form-label">隧道名</label>
              <input autocomplete="off" class="border-color" type="text" id="tunnel-name" name="tunnel-name"
                v-model="tunnelName" placeholder="隧道名">
            </div>
            <div class="form-group-div-02">
              <label class="form-label">隧道设置</label>
              <div class="settings">
                <button v-for="protocol in protocols" :key="protocol" class="select-button"
                  :class="{ 'active': selectedProtocol === protocol }" @click="selectProtocol(protocol)">
                  {{ protocol }}
                </button>
              </div>
            </div>
          </div>

          <div class="form-group">
            <label for="local-ip" class="form-label">内网IP</label>
            <input autocomplete="off" class="border-color" type="text" id="local-ip" name="local-ip" v-model="localIP"
              value="127.0.0.1" placeholder="127.0.0.1">
          </div>

          <div class="form-group">
            <label for="local-ports" class="form-label">内网端口</label>
            <input autocomplete="off" class="border-color" type="text" id="local-ports" name="local-ports"
              v-model="localPorts" placeholder="TCP:21  SSH:22  MySql:3306  MC:25565/19132  Terraria:7777">
          </div>

          <div class="form-group" v-if="selectedProtocol === 'TCP' || selectedProtocol === 'UDP'">
            <label for="remote-port" class="form-label">远程端口</label>
            <input autocomplete="off" class="border-color" type="text" id="remote-port" name="remote-port"
              v-model="remotePort" placeholder="映射到远程服务器上的端口">
          </div>

          <div class="form-group" v-if="selectedProtocol === 'HTTP' || selectedProtocol === 'HTTPS'">
            <label for="custom-domains" class="form-label">自定义域名</label>
            <input autocomplete="off" class="border-color" type="text" id="custom-domains" name="custom-domains"
              v-model="custom_domain" placeholder="输入自定义域名">
          </div>

          <div class="form-group" v-if="selectedProtocol === 'XTCP' || selectedProtocol === 'STCP'">
            <label for="secret-key" class="form-label">密钥</label>
            <input autocomplete="off" class="border-color" type="text" id="secret-key" name="secret-key"
              v-model="secret_key" placeholder="输入密钥">
          </div>

          <button class="button" type="submit" @click="SubmitTunnel">创建</button>
        </form>
      </section>
      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import Sidebar from '@/modules/user/components/Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import axios from 'axios';
;
import { useLoadingStore } from '@/modules/common/stores/loading'
import { commonMethods } from '@/modules/common/utils/common.js'
import '@/modules/common/assets/styles/common.css'


export default {
  name: 'add_tunnels',
  components: {
    Header,
    Sidebar,
    Footer,
  },
  data() {
    return {
      protocols: ['TCP', 'UDP', 'HTTP', 'HTTPS', 'XTCP', 'STCP'],
      selectedProtocol: 'TCP',
      lists: [],
      selectedServer: '',
      tunnelName: '',
      localIP: '127.0.0.1',
      localPorts: '',
      remotePort: '',
      custom_domain: '',
      secret_key: '',
      vipOnly: true,
      allowWebsite: true,
      allowHighTraffic: true,
      allowUdp: true,
      status: true,
      vip: 'vip',
      no_vip: '通用节点',
      web: '网站部署',
      traffic: '超大流量',
      udp: 'UDP',
      s_status: 'ONLINE',
      server_name: '',
      server_Info: '',
      server_ip: '',
      server_domain: '',
      server_portRange: '',
      server_current_clients: '',
      server_max_clients: '',
      server_current_tunnels: '',
    }
  },
  methods: {
    ...commonMethods,
    selectProtocol(protocol) {
      this.selectedProtocol = protocol;
    },
    async ServersList() {
      try {
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.get('/servers')
        loadingStore.hideLoading();
        this.lists = response.data.data;

        this.selectedServer = this.lists[0].name

        this.lists.forEach(list => {
          if (list.name === this.selectedServer) {
            this.vipOnly = list.vipOnly;
            this.allowWebsite = list.allowWebsite;
            this.allowHighTraffic = list.allowHighTraffic;
            this.allowUdp = list.allowUdp
            this.server_name = list.name;
            this.server_Info = list.info;
            this.server_ip = list.ip_address;
            this.server_domain = list.domain;
            this.server_portRange = list.port_range;
            this.status = list.status === 'ONLINE' ? true : false;
            this.s_status = list.status;
            this.server_current_clients = list.current_clients;
            this.server_max_clients = list.max_clients;
            this.server_current_tunnels = list.current_tunnels;
          }
        });

      } catch (error) {
        console.error(error);
      }
    },
    async SubmitTunnel() {
      try {
        const Token = localStorage.getItem('Token');
        let tunnelData = {
          server_name: this.selectedServer,
          tunnel_name: this.tunnelName,
          tunnel_type: this.selectedProtocol.toLowerCase(),
          local_ip: this.localIP,
          local_port: this.localPorts
        };

        if (this.selectedProtocol === 'HTTP' || this.selectedProtocol === 'HTTPS') {
          tunnelData.custom_domain = this.custom_domain;
        } else if (this.selectedProtocol === 'XTCP' || this.selectedProtocol === 'STCP') {
          tunnelData.secret_key = this.secret_key;
        } else {
          tunnelData.remote_port = this.remotePort;
        }

        const resp = await axios.post('/users/info', {
          token: Token
        });

        if (resp.data.data.real_name_status !== 1) {
          this.$message.error("请实名认证后再进行隧道创建！");
          return;
        }

        if (resp.data.data.status === "OFFLINE") {
          this.$message.error("服务器离线！");
          return;
        }

        if ((resp.data.data.vip_status === 0 || resp.data.data.vip_status === 2) && this.selectedServer) {
          const selectedServer = this.selectedServer;
          const serverInList = this.lists.find(server => server.name === selectedServer);

          if (serverInList && serverInList.vipOnly) {
            this.$message.warning('该节点为VIP专属节点，为了VIP用户的体验，请您更换为免费通用节点，谢谢您的理解与配合！');
            return;
          }
        }

        if (this.selectedServer && !this.allowWebsite && (this.selectedProtocol === 'HTTP' || this.selectedProtocol === 'HTTPS')) {
          this.$message.error('该节点不允许建站，请更换其他节点');
          return;
        }

        if (this.selectedServer && !this.allowUdp && this.selectedProtocol === 'UDP') {
          this.$message.error('该节点不允许UDP，请更换其他节点');
          return;
        }

        const response = await axios.post('/frp-tunnels', tunnelData, {
          headers: { 'Token': Token }
        });

        this.$message.success(response.data.message);

      } catch (error) {
        console.error(error);

        this.$message.error('提交隧道失败，请稍后重试');
      }
    },
    handleServerChange() {
      this.lists.forEach(list => {
        if (list.name === this.selectedServer) {
          this.vipOnly = list.vipOnly;
          this.allowWebsite = list.allowWebsite;
          this.allowHighTraffic = list.allowHighTraffic;
          this.allowUdp = list.allowUdp;
          this.server_name = list.name;
          this.server_Info = list.info;
          this.server_ip = list.ip_address;
          this.server_domain = list.domain;
          this.server_portRange = list.port_range;
          this.status = list.status === 'ONLINE' ? true : false;
          this.s_status = list.status;
          this.server_current_clients = list.current_clients;
          this.server_max_clients = list.max_clients;
          this.server_current_tunnels = list.current_tunnels;
        }
      });
    },
    handleCloseMessageBox() {
      this.showMessageBox = false;
    },
    autoCloseMessageBox() {
      setTimeout(() => {
        this.showMessageBox = false;
      }, 3000);
    }
  },
  computed: {
    vipClass() {
      return this.vipOnly ? "golden" : "green";
    },
    websiteClass() {
      return this.allowWebsite ? "green" : "red";
    },
    trafficClass() {
      return this.allowHighTraffic ? "green" : "red";
    },
    udpClass() {
      return this.allowUdp ? "green" : "red";
    },
    statusClass() {
      return this.status ? "green" : "red";
    }
  },
  mounted() {
    this.checkTokenValidity()
    this.ServersList()
  }
};
</script>


<style scoped>
.add-tunnel {
  margin-top: 20px;
  margin-left: 80px;
  margin-bottom: 70px;
}

.form-group {
  margin-bottom: 15px;
}

.fg1 {
  display: flex;
  gap: 100px;
  flex-wrap: wrap;
}

label {
  display: block;
  margin-bottom: 5px;
}

input[type="text"],
select {
  width: 100rem;
  padding: 0.5rem 0.8rem;
  box-sizing: border-box;
  font-size: 1.1rem;
}

#tunnel-name {
  width: 40rem;
}

.settings {
  display: flex;
}

.button {
  width: 5rem;
  height: 2.3rem;
  padding: 0.2rem 0.4rem;
  background-color: #42997c;
  color: white;
  border: none;
  cursor: pointer;
  margin-top: 20px;
  border-radius: 5px;
  transition: all 0.3s;
  box-sizing: border-box;
  font-size: 1.1rem;
}

.button:hover {
  background-color: #5acaa5;
}

h1 {
  margin-bottom: 20px;
}

.form-label {
  margin-bottom: 10px;
}

.form-group-div-01 {
  width: 40%;
}

.button-group {
  display: flex;
}

.select-button {
  width: 5rem;
  height: 2.3rem;
  font-size: 0.9rem;
  cursor: pointer;
  text-align: center;
  background-color: #fff;
  border: 1px solid #cecece;
  transition: all 0.3s;
}

.select-button:nth-child(1) {
  border-top-left-radius: 5px;
  border-bottom-left-radius: 5px;
}

.select-button:nth-child(6) {
  border-top-right-radius: 5px;
  border-bottom-right-radius: 5px;
}

.select-button:nth-child(2),
.select-button:nth-child(3),
.select-button:nth-child(4),
.select-button:nth-child(5),
.select-button:nth-child(6) {
  border-left: none;
}

.select-button.active {
  background-color: #42997c;
  color: rgb(255, 255, 255);
  font-weight: 700;
  border-color: #378068;
  height: 37px;
}

.border-color {
  border: 1px solid #cecece;
  border-radius: 5px;
}

.border-color:focus {
  border: 1px solid #cecece;
  outline: none;
}

input::placeholder {
  color: #cbcbcb7f;
  font-size: 1rem;
  font-style: italic;
}

.red,
.green,
.golden {
  width: 70%;
  height: 30px;
  background-color: #ddf7ea;
  border: 1px solid #7de6b1;
  color: #00994f;
  border-radius: 5px;
  font-weight: 900;
  text-align: center;
  font-size: 12px;
  line-height: 20px;
  margin-top: 5px;
  margin-right: 10px;
}

.red {
  background-color: #f7e6dd;
  border: 1px solid #e6a77d;
  color: #e14f00;
}

.golden {
  width: 70%;
  height: 30px;
  background-color: #fff1aa;
  border: 1px solid #e6c200;
  color: #e2a900;
  border-radius: 5px;
  font-weight: 900;
  text-align: center;
  font-size: 12px;
  line-height: 20px;
  margin-top: 5px;
  margin-right: 10px;
}

.tips h1 {
  font-size: 0.8rem;
  margin: 0;
  color: #000000;
  margin-right: 1rem;
  margin-left: 0.5rem;
}

.big {
  width: 35rem;
  display: flex;
  margin-top: 20px;
}

.udp,
.vip,
.website,
.traffic,
.status {
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 0 10px;
}

.udp {
  width: 20%;
}

.vip {
  width: 20%;
}

.website {
  width: 20%;
}

.traffic {
  width: 20%;
}

.status {
  width: 20%;
}

.title {
  font-size: 18px;
  margin-bottom: 5px;
  font-weight: 900;
  color: #000000;
}

.ServerInfo {
  width: 30rem;
  margin-top: 20px;
  padding: 15px;
  padding-left: 20px;
  background-color: #e2f6eb90;
  border-radius: 5px;
}

.content {
  color: #0e0e0e;
  margin: 3px 0;
}

.tips {
  display: flex;
  width: 37rem;
  margin: 0.9rem 0;
}

.tip_1,
.tip_2,
.tip_3 {
  display: flex;
  align-items: center;
  margin-right: 1.3rem;
  font-weight: 1900;
}

.one,
.two,
.three {
  margin: 0;
  margin-right: 0.7rem;
  width: 0.7rem;
  height: 0.7rem;
  border: none;
  border-radius: 0.2rem;
}

.one {
  background-color: #fce782;
}

.two {
  background-color: #8bfec4;
}

.three {
  background-color: #feaf84;
}

.tips_p {
  font-size: 0.6rem;
}

.tip_1 {
  color: #eec700;
}

.tip_2 {
  color: #00ff80;
}

.tip_3 {
  color: #ff5900;
}

@media (prefers-color-scheme: dark) {
  .select-button {
    background-color: #28282c;
    border: 1px solid #212121;
    color: #9e9e9e;
  }

  .select-button.active {
    background-color: #236659;
    color: rgb(255, 255, 255);
    font-weight: 700;
    border-color: #09360b;
  }

  .border-color {
    background-color: #1a1a20;
    border: 1px solid #282832;
    color: #d4d4d4;
  }

  .border-color:focus {
    border: 1px solid #282832;
    outline: none;
  }

  input::placeholder {
    color: #7f7f7f37;
  }

  .ServerInfo {
    background-color: #2d7a6a;
  }

  .ServerInfo .title,
  .ServerInfo .content,
  .ServerInfo p {
    color: #d1d1d1;
  }

  .title {
    color: #e0e0e0;
  }

  .content {
    color: #b0b0b0;
  }

}
</style>
