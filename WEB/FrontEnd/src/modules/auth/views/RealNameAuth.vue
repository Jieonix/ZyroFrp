<script setup>
import Loading from '@/modules/common/components/Loading.vue'
</script>

<template>
  <div class="auth">
    <Loading />
    <Header />
    <Sidebar />
    <main class="main-content">
      <section class="welcome">
        <h2>欢迎来到 FRP 管理平台</h2>
        <p>在这里你可以实名认证，确保服务的正常使用和运行</p>
      </section>
      <section class="features">
        <div class="feature-box fb1" v-if="!realNameVerified">
          <h3 class="fb1-h3">实名认证</h3>
          <div class="fb1_div">
            <p class="top_p">身份信息将调用第三方API验证,后期如发现利用本站Frp服务从事违法违纪活动,本站有权将身份信息交由警方处理！</p>
            <p class="top_p_2">本站允许未成年用户使用，请勿使用他人身份信息，以避免不必要的麻烦</p>
            <label for="name">姓名</label>
            <input autocomplete="off" class="form-label" type="text" id="name" v-model="name" placeholder="请输入姓名" />
            <label class="cardid" for="idCard">身份证号</label>
            <input autocomplete="off" class="form-label" type="text" id="idCard" v-model="idCard"
              placeholder="请输入身份证号" />
            <button class="submit-btn" @click="handleSubmit">提交</button>
          </div>
        </div>
        <div class="feature-box fb2" v-else>
          <p>实名认证已完成，可正常使用所有功能！</p>
        </div>
      </section>
      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import Sidebar from '@/modules/common/components/Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import axios from 'axios';
import { useLoadingStore } from '@/modules/common/stores/loading'
import { commonMethods } from '@/modules/common/utils/common.js'
import '@/modules/common/assets/styles/common.css'


export default {
  name: 'auth',
  components: {
    Header,
    Sidebar,
    Footer
  },
  data() {
    return {
      name: '',
      idCard: '',
      realNameVerified: false
    };
  },
  methods: {
    ...commonMethods,
    handleSubmit() {
      const appcode = '783aa29e972e4e409046fb80696048da';
      const url = 'https://idcard.market.alicloudapi.com/lianzhuo/idcard';
      const headers = {
        'Authorization': 'APPCODE ' + appcode
      };
      const params = {
        cardno: this.idCard,
        name: this.name
      };

      axios.get(url, { headers, params })
        .then(response => {
          const data = response.data.resp.code
          console.log(data);

          if (data === 0) {

            const token = localStorage.getItem('Token');

            axios.post('/idcard', {
              token,
              real_name: this.name,
              idCard: this.idCard
            })
              .then(response => {
                window.location.reload()
              })
              .catch(error => {
                console.error(error);
              });
          } else {
            if (data === 5) {
              this.$message.error("身份信息不匹配，请重试～");
            } else if (data === 14) {
              this.$message.error("无此身份证号码，请检查并重试～");
            } else if (data === 96) {
              this.$message.error("交易失败，请稍后重试～");
            } else {
              this.$message.error("身份验证失败，未知错误～");
            }
          }
        })
        .catch(error => {
          console.error(error);
          this.$message.error("身份验证失败，请重试～" + error);
        });
    },
    real_name_status() {
      const token = localStorage.getItem('Token');

      const loadingStore = useLoadingStore();
      loadingStore.showLoading();
      axios.get('/idcard', {
        params: {
          token
        }
      })
        .then(response => {

          loadingStore.hideLoading();

          if (response.data.data === 1) {
            this.realNameVerified = true;
          } else {
            this.realNameVerified = false;
          }

        })
        .catch(error => { })
    }
  },
  mounted() {
    this.checkTokenValidity();
    this.real_name_status();
  }
}
</script>


<style scoped>
.fb1 {
  margin: 15px;
  height: 3%;
  width: calc(100vw - 18rem - 2rem);
  margin: 0 1rem;
  padding: 2rem;
}

.fb1-h3 {
  font-weight: 700;
  margin-bottom: 10px;
}

.fb1_div {
  display: flex;
  flex-direction: column;
}

.submit-btn {
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
  font-size: 1.1rem;
}

.submit-btn:hover {
  background-color: #5acaa5;
}

.form-label {
  width: 100rem;
  height: 2.5rem;
  margin-bottom: 0.8rem;
  padding: 0.5rem 0.8rem;
  background-color: #ffffff;
  border: 1px solid #cecece;
  outline: none;
  border-radius: 5px;
  margin-top: 0.8rem;
  color: #1a1a1a;
}

.cardid {
  margin-top: 20px;
}

.top_p {
  color: rgb(145, 145, 145);
  margin-top: 5px;
}

.top_p_2 {
  color: rgb(145, 145, 145);
  margin-bottom: 20px;
  margin-top: 5px;
}

.fb2 {
  width: calc(100vw - 18rem - 2rem);
  margin: 1rem;
  box-sizing: border-box;
  height: 100px;
  display: flex;
  align-items: center;
  font-size: 17px;
  padding-left: 15px;
}

.fb2 p {
  margin-left: 20px;
}

.features {
  flex-direction: column;
}

.fb1-h3 {
  margin-left: 30px;
  font-weight: 900;
  font-size: 25px;
}

input::placeholder {
  color: #cbcbcb7f;
  font-size: 1rem;
  font-style: italic;
}

input:focus {
  border-color: #cecece;
  outline: none;
}

@media (prefers-color-scheme: dark) {
  .form-label {
    background-color: #1a1a20;
    border: 1px solid #282832;
    color: #d4d4d4;
  }

  .form-label:focus {
    border: 1px solid #282832;
    outline: none;
  }

  .form-label::placeholder {
    color: #7f7f7f37;
  }

  .fb1,
  .fb2 {
    background-color: #18181c;
  }

  .top_p,
  .top_p_2 {
    color: #b0b0b0;
  }
}
</style>
