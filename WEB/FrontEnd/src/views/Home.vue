<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="home">
    <Loading />
    <Header />
    <div class="container">
      <Sidebar />
      <main class="main-content">
        <Loading />
        <section class="welcome">
          <h2>欢迎来到 FRP 管理平台</h2>
          <p>在这里你可以管理和查看个人信息并查看官方公告，监控流量和状态</p>
        </section>
        <section class="features">
          <div class="f_left">
            <div class="feature-box fb1">
              <h3 class="fb-h3">个人资料</h3>
              <p class="fb1_p1">
                尊敬的{{ displayVipStatus() }}，您好！
                <b :style="displayStyle()" class="vip">{{ displayVipStatus() }}</b>
                <b v-if="is_trial_user === 1" class="trial"><img src="/icons/king.png">先锋测试用户</b>
              </p>
              <p class="fb1_p2">您的邮箱为：<b class="fb1_p2_b">{{ email }}</b></p>
              <p class="fb1_p3">您已经陪我们度过了 <b class="fb1_p3_b">{{ timeSinceCreation }}</b> 天</p>
              <p class="fb1_p4">您的 userKey 为：<b class="fb1_p4_b" @click="copy_userKey">{{ userKey }}</b></p>
            </div>
            <div class="feature-box fb2">
              <h3 class="fb-h3">数据报告</h3>
              <div class="fb2-div">
                <table class="fb2-table">
                  <thead>
                    <tr class="fb2-tr">
                      <td class="fb2-td-top">剩余流量</td>
                      <td class="fb2-td-top">上行速率</td>
                      <td class="fb2-td-top">下行速率</td>
                    </tr>
                  </thead>
                  <tbody>
                    <tr class="fb2-tr">
                      <td class="fb2-td-bottom">{{ remaining_traffic }}GB</td>
                      <td class="fb2-td-bottom">{{ upload_limit }}Mb/s</td>
                      <td class="fb2-td-bottom">{{ download_limit }}Mb/s</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div class="feature-box fb3">
              <h3 class="fb-h3">广告位招商</h3>
              <p></p>
              <br>
              <br>
              <br>
              <br>
              <br>
            </div>
            <hr class="line">
            <div class="feature-box fb4">
              <h3 class="fb-h3">注意事项</h3>
              <p class="fb4-p">
                1. 账号安全：请确保您的账号和密码安全，不要与他人共享，避免使用弱密码。<br>
                2. 服务费用与结算：所有费用需提前支付，支持多种支付方式，选择服务套餐时请注意付款方式。<br>
                3. 服务稳定性：我们致力于提供稳定的网络连接，但不可控因素可能影响服务。遇到问题时请及时联系客服。<br>
                4. 使用范围：FRP 服务仅限合法使用，禁止用于违法活动，任何不当行为会被立即停止服务。<br>
                5. 数据隐私：我们严格保护您的数据隐私，定期备份您的数据，以防止丢失。<br>
                6. 技术支持：提供7x24小时技术支持，遇到问题请通过客服工具联系。<br>
                7. 服务期限与续费：服务到期后自动停止，续费时请提前处理，避免服务中断。<br>
                8. 不可抗力条款：如遇不可抗力因素，我们将尽力恢复服务，并承担由此造成的损失，给予您相应补偿。<br>
                9. 退款政策：请您慎重考虑后再下单，付款后不可退款，可先体验免费服务再决定是否下单vip服务。<br>
                10. 用户责任：请妥善管理设备和网络，任何因用户原因导致的问题，我们不承担责任。<br>
              </p>
            </div>
          </div>
          <div class="f_right">
            <div class="feature-box fb5">
              <h3 class="fb5-h3">公告板</h3>
              <hr class="fb5-line">
              <div class="announcement" v-for="announcement in announcements" :key="announcement.id">
                <h3 class="fb5-h3-2">{{ announcement.title }}</h3>
                <p>{{ announcement.content }}</p>
                <p><strong>发布日期：</strong> {{ formatDate(announcement.created_at) }}</p>
                <p><strong>更新日期：</strong> {{ formatDate(announcement.updated_at) }}</p>
              </div>
            </div>
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
import { ref } from 'vue';
import axios from 'axios';
import { useLoadingStore } from '@/stores/loading'


export default {
  name: 'Home',
  components: {
    Header,
    Sidebar,
    Footer,
  },
  data() {
    const isDarkMode = window.matchMedia("(prefers-color-scheme: dark)").matches;

    return {
      token: localStorage.getItem("Token"),
      userInfo: null,
      email: "",
      vipStatus: "",
      creatTime: "",
      userToken: "",
      timeSinceCreation: 0,
      remaining_traffic: "",
      upload_limit: "",
      download_limit: "",
      announcements: [],
      is_trial_user: false,
      normalUserStyle: isDarkMode
        ? {
          backgroundColor: "#606060",
          color: "#dfdfdf",
          fontWeight: "900",
          fontSize: "13px",
          padding: "3px 5px",
          margin: "0 3px 0 5px",
          borderRadius: "3px",
        }
        : {
          backgroundColor: "#606060",
          color: "#dfdfdf",
          fontWeight: "900",
          fontSize: "13px",
          padding: "3px 5px",
          margin: "0 3px 0 5px",
          borderRadius: "3px",
        },

      vipStyle: isDarkMode
        ? {
          backgroundColor: "#ffd700",
          color: "#383838",
          fontWeight: "bold",
          fontSize: "0.7rem",
          padding: "2px 5px",
          margin: "0 3px 0 5px",
          borderRadius: "3px",
        }
        : {
          backgroundColor: "#ffd700",
          color: "#383838",
          fontWeight: "bold",
          fontSize: "0.7rem",
          padding: "2px 5px",
          margin: "0 3px 0 5px",
          borderRadius: "3px",
        },
    };
  },
  methods: {
    checkTokenValidity() {
      const router = useRouter();
      const token = localStorage.getItem("Token");
      if (!validateToken(router, token)) {
        return;
      }
    },

    async getUserInfo() {
      try {
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.post('/users/info', {
          token: this.token,
        });
        loadingStore.hideLoading();
        this.userInfo = response.data.data;
        this.vipStatus = String(this.userInfo.vip_status);
        this.email = this.userInfo.email;
        this.creatTime = this.userInfo.created_at;

        const creationDate = new Date(this.creatTime);
        const currentDate = new Date();
        this.timeSinceCreation = ((currentDate - creationDate) / 1000 / 60 / 60 / 24).toFixed(3);
        this.userKey = this.userInfo.user_key;
        this.remaining_traffic = this.userInfo.remaining_traffic
        this.upload_limit = this.userInfo.upload_limit
        this.download_limit = this.userInfo.download_limit;
        this.is_trial_user = this.userInfo.is_trial_user;
      } catch (error) {
        console.error('获取用户信息失败:', error);
      }
    },

    async fetchAnnouncements() {
      try {
        const response = await axios.get('/announcements');
        this.announcements = response.data.data;

        this.sortAnnouncements();
      } catch (error) {
        console.error("获取公告数据失败：", error);
      }
    },

    sortAnnouncements() {
      const pinned = this.announcements.filter(a => a.is_pinned === 1);
      const nonPinned = this.announcements.filter(a => a.is_pinned === 0);

      pinned.sort((a, b) => b.id - a.id);
      nonPinned.sort((a, b) => b.id - a.id);

      this.announcements = [...pinned, ...nonPinned];
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },

    displayVipStatus() {
      if (this.vipStatus === '0') {
        return '普通用户';
      } else if (this.vipStatus === '1') {
        return 'VIP';
      }
      return this.vipStatus;
    },

    displayStyle() {
      if (this.vipStatus === "1") {
        return this.vipStyle;
      }
      return this.normalUserStyle;
    },

    copy_userKey() {
      navigator.clipboard.writeText(this.userKey)

        .then(() => {
          this.$message.success("userKey已复制成功");
        })
        .catch(() => {
          this.$message.error("userKey未复制成功，请重试");
        })
    },

  },

  mounted() {
    this.checkTokenValidity();
    this.getUserInfo();
    this.fetchAnnouncements();
  }
}
</script>


<style scoped>
.fb1,
.fb2,
.fb3 {
  margin: 15px 15px 15px 1rem;
  width: 34.5rem;
  height: 14.5rem;
}

.fb1 {
  margin-top: 0;
  background-color: #fff;
}

.fb4 {
  border: 1px solid rgb(255, 158, 79);
  margin: 15px;
  background-color: #ffd1bc;
  margin: 15px 15px 15px 1rem;
  width: 34.5rem;
  height: 28rem;
}

.fb5 {
  width: 81.6rem;
  height: 76rem;
  overflow: auto;
  margin-right: 1rem;
}

.line {
  border: 0;
  height: 1px;
  background-color: #dbdbdb;
  width: 95%;
  margin: 0 auto;
  margin-top: 22px;
  margin-bottom: 20px;
}

.fb-h3 {
  font-weight: 700;
  margin-bottom: 10px;
  margin-left: 12px;
}

.fb5-h3 {
  font-weight: 700;
  font-size: 23px;
  margin-bottom: 10px;
}

.fb1 p,
.fb3 p,
.fb4 p {
  margin-bottom: 10px;
  margin-left: 12px;
}

.fb4 p {
  font-weight: 400;
  font-size: 13px;
  margin-left: 12px;
  line-height: 1.8;
}

.fb2-td-top,
.fb2-td-bottom {
  padding: 0 10px;
  text-align: left;
  width: 5vw;
  height: 4vh;
  background-color: #ffffff;
}

.fb2-td-bottom {
  font-weight: 700;
  font-size: 20px;
}

.fb5-line {
  border: 1px solid #e6e6e6;
  margin-bottom: 20px;
}

.fb5-h3-2 {
  font-weight: 700;
}

.fb1_p1 {
  font-size: 15px;
}

.fb1_p4_b,
.fb1_p2_b {
  font-weight: 900;
  cursor: pointer;
  font-size: 14px;
}

.fb1_p4_b {
  transition: all 0.3s;
}

.fb1_p4_b:hover {
  color: #616161;
}

.fb1_p3 {
  font-size: 16px;
  font-weight: 400;
}

.fb1_p3_b {
  font-weight: 900;
  font-size: 22px;
}

.announcement {
  margin-bottom: 20px;
  background-color: #ffffff;
  padding: 10px 20px;
  border-bottom: #e2e2e2 1px solid;
}

.fb5-h3-2 {
  margin-bottom: 5px;
}

.announcement p {
  font-weight: 400;
}

.announcement p:nth-child(3),
.announcement p:nth-child(4) {
  font-size: 11px;
  margin-bottom: 0;
}

.announcement p:nth-child(3) {
  margin-top: 7px;
}

.trial {
  background-color: #6600ff;
  color: #ffffff;
  font-weight: bold;
  font-size: 12px;
  padding: 3px 5px;
  margin: 0 3px 0 5px;
  border-radius: 3px;
}

.trial img {
  height: 13px;
  margin-bottom: 4px;
  vertical-align: middle;
  padding-right: 5px;
}

.vip img {
  height: 12px;
  margin-bottom: 2px;
  vertical-align: middle;
  padding-right: 3px;
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

  .line {
    background-color: #282828;
  }

  .fb4 {
    border: 1px solid #834f0a;
    background-color: #482f0f;
  }

  .fb2-td-top,
  .fb2-td-bottom {
    background-color: #18181c;
  }

  .fb5-line {
    border: 1px solid #282828;
  }

  .announcement {
    background-color: #18181c;
    border-color: #292929;
  }

  .fb1 {
    background-color: #18181c;
  }

}
</style>
