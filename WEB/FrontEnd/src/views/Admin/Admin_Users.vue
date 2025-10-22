<script setup>
import MessageBox from '@/components/MessageBox.vue'
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="Admin_Users">
    <Loading />
    <Header />
    <div class="container">
      <Admin_Sidebar />
      <main class="main-content">
        <Loading />
        <h1>用户管理页</h1>
        <section class="features">
          <div class="features-box">
            <div class="top">
              <button class="search_button" @click="search_style_function">查询</button>
              <button class="add_button" @click="add_style_function">新增</button>
            </div>
            <table>
              <thead>
                <tr>
                  <th class="head user-id">用户ID</th>
                  <th class="head email">邮箱</th>
                  <th class="head user-key">用户密钥</th>
                  <th class="head role">用户权限</th>
                  <th class="head remaining-traffic">剩余流量</th>
                  <th class="head upload-limit">上行限速</th>
                  <th class="head download-limit">下行限速</th>
                  <th class="head is-trial-user">先锋测试用户</th>
                  <th class="head vip-start-time">会员开始时间</th>
                  <th class="head vip-end-time">会员结束时间</th>
                  <th class="head vip-status">会员状态</th>
                  <th class="head real-name">真实姓名</th>
                  <th class="head real-name-status">实名状态</th>
                  <th class="head created-at">创建日期</th>
                  <th class="head updated-at">更新日期</th>
                  <th class="head edit-t">编辑</th>
                  <th class="head delete">删除</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="user in users" :key="user.user_id">
                  <td class="user-id">{{ user.user_id }}</td>
                  <td class="email">{{ user.email }}</td>
                  <td class="email">{{ user.user_key }}</td>
                  <td class="role">{{ user.role }}</td>
                  <td class="remaining-traffic">{{ user.remaining_traffic }}</td>
                  <td class="upload-limit">{{ user.upload_limit }}</td>
                  <td class="download-limit">{{ user.download_limit }}</td>
                  <td class="is-trial-user">{{ user.is_trial_user ? '是' : '否' }}</td>
                  <td class="vip-start-time">{{ formatDate(user.vip_start_time) }}</td>
                  <td class="vip-end-time">{{ formatDate(user.vip_end_time) }}</td>
                  <td class="vip-status">{{ user.vip_status ? 'VIP' : '非VIP' }}</td>
                  <td class="real-name">{{ user.real_name }}</td>
                  <td class="real-name-status">{{ user.real_name_status ? '已实名' : '未实名' }}</td>
                  <td class="created-at">{{ formatDate(user.created_at) }}</td>
                  <td class="updated-at">{{ formatDate(user.updated_at) }}</td>
                  <td class="td_edit"><button class="edit_button" @click="edit_style_function(user.user_id)">编辑</button>
                  </td>
                  <td class="td_delete"><button class="edit_delete" @click="deleteUser(user.user_id)">删除</button></td>
                </tr>
              </tbody>
            </table>
          </div>
          <div v-if="add_style" class="overlay_1">
            <div class="add">
              <h1>新增用户</h1>
              <label for="">账号*</label><input id="email" v-model="email" type="text">
              <label for="">密码*</label><input id="password" v-model="password" type="password">
              <label for="">权限</label>
              <select v-model="role" id="role">
                <option value="User">User</option>
                <option value="Admin">Admin</option>
                <option value="SuperAdmin">SuperAdmin</option>
              </select>
              <label for="">总流量 (GB)</label><input v-model="remaining_traffic" id="remaining_traffic" type="number"
                value="4000">
              <label for="">上行限速 (Mb)</label><input v-model="upload_limit" id="upload_limit" type="number"
                value="51888">
              <label for="">下行限速 (Mb)</label><input v-model="download_limit" id="download_limit" type="number"
                value="51888">
              <label for="">先锋体验用户</label>
              <select v-model.number="is_trial_user" id="is_trial_user">
                <option value="1">是</option>
                <option value="0">否</option>
              </select>

              <label>真实姓名</label>
              <input v-model="real_name" id="real_name" type="text" placeholder="张xx">

              <label>身份证号（加密）</label>
              <input v-model="id_card" id="id_card" type="text" placeholder="652xxxxxxxxxxxxxxx">

              <label>是否实名</label>
              <select v-model.number="real_name_status" id="real_name_status">
                <option value="1">已实名</option>
                <option value="0">未实名</option>
              </select>

              <label>会员开始时间</label>
              <input v-model="vip_start_time" id="vip_start_time" type="date">

              <label>会员结束时间</label>
              <input v-model="vip_end_time" id="vip_end_time" type="date">

              <label>会员状态</label>
              <select v-model.number="vip_status" id="vip_status">
                <option value="0">非VIP</option>
                <option value="1">VIP</option>
              </select>

              <div>
                <button type="button" @click="confirmTheAddition" class="confirm">确认新增</button>
                <button type="button" @click="add_style = false" class="cancel">取消</button>
              </div>
            </div>
          </div>
          <div v-if="edit_style" class="overlay_1">
            <div class="edit">
              <h1>修改用户</h1>
              <label for="">账号*</label><input id="email" v-model="email" type="text">
              <label for="">密码*（默认不显示）</label><input id="password" v-model="password" type="text">
              <label for="">权限</label>
              <select v-model="role" id="role">
                <option value="User">User</option>
                <option value="Admin">Admin</option>
                <option value="SuperAdmin">SuperAdmin</option>
              </select>
              <label for="">总流量 (GB)</label><input v-model="remaining_traffic" id="remaining_traffic" type="number"
                value="4000">
              <label for="">上行限速 (Mb)</label><input v-model="upload_limit" id="upload_limit" type="number"
                value="51888">
              <label for="">下行限速 (Mb)</label><input v-model="download_limit" id="download_limit" type="number"
                value="51888">
              <label for="">先锋体验用户</label>
              <select v-model.number="is_trial_user" id="is_trial_user">
                <option value="1">是</option>
                <option value="0">否</option>
              </select>

              <label>真实姓名</label>
              <input v-model="real_name" id="real_name" type="text" placeholder="">

              <label>身份证号（默认不显示）</label>
              <input v-model="id_card" id="id_card" type="text" placeholder="">

              <label>是否实名</label>
              <select v-model.number="real_name_status" id="real_name_status">
                <option value="1">已实名</option>
                <option value="0">未实名</option>
              </select>

              <label>会员开始时间</label>
              <input v-model="vip_start_time" id="vip_start_time" type="date">

              <label>会员结束时间</label>
              <input v-model="vip_end_time" id="vip_end_time" type="date">

              <label>会员状态</label>
              <select v-model.number="vip_status" id="vip_status">
                <option value="0">非VIP</option>
                <option value="1">VIP</option>
              </select>

              <div>
                <button type="button" @click="confirmTheEdit(this.user_id)" class="confirm">确认修改</button>
                <button type="button" @click="edit_style = false" class="cancel">取消</button>
              </div>
            </div>
          </div>
          <div v-if="search_style" class="overlay_1">
            <div class="add">
              <h1>查询用户</h1>
              <label for="">账号*</label><input id="email" v-model="email" type="text">
              <label for="">权限</label>
              <select v-model="role" id="role">
                <option value="User">User</option>
                <option value="Admin">Admin</option>
                <option value="SuperAdmin">SuperAdmin</option>
              </select>
              <label for="">先锋体验用户</label>
              <select v-model.number="is_trial_user" id="is_trial_user">
                <option value="1">是</option>
                <option value="0">否</option>
              </select>

              <label>真实姓名</label>
              <input v-model="real_name" id="real_name" type="text" placeholder="张xx">

              <label>是否实名</label>
              <select v-model.number="real_name_status" id="real_name_status">
                <option value="1">已实名</option>
                <option value="0">未实名</option>
              </select>

              <label>会员状态</label>
              <select v-model.number="vip_status" id="vip_status">
                <option value="0">非VIP</option>
                <option value="1">VIP</option>
              </select>

              <div>
                <button type="button" @click="confirmTheSearch" class="confirm">开始查询</button>
                <button type="button" @click="search_style = false" class="cancel">取消</button>
              </div>
            </div>
          </div>
        </section>
        <Footer />
      </main>
    </div>
    <MessageBox v-if="showMessageBox" :message="messageBoxContent" @close="handleCloseMessageBox" />
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '../../utils/token.js';
import axios, { Axios } from 'axios';
import Admin_Sidebar from '@/components/Admin_Sidebar.vue';
import Footer from '@/components/Footer.vue';
import { useLoadingStore } from '@/stores/loading';
import { emailRegex, weakPasswords } from '@/assets/vue_main.js'


export default {
  name: 'Home',
  components: {
    Header,
    Admin_Sidebar,
  },
  data() {
    const isDarkMode = window.matchMedia("(prefers-color-scheme: dark)").matches;

    return {
      user_id: null,
      users: [],
      add_style: false,
      edit_style: false,
      search_style: false,
      email: '',
      password: '',
      role: 'User',
      remaining_traffic: 4000,
      upload_limit: 51888,
      download_limit: 51888,
      is_trial_user: 1,
      real_name: null,
      id_card: null,
      real_name_status: 0,
      vip_start_time: this.getCurrentDate(),
      vip_end_time: this.getCurrentDate(),
      vip_status: 0,
      showMessageBox: false,
      messageBoxContent: '',
      errorCodes: [
        "REGISTER_4001",
        "REGISTER_4002",
        "REGISTER_4003",
        "REGISTER_4004",
        "REGISTER_4005",
        "REGISTER_4006",
        "EMAIL_SEND_4301"
      ],
    };
  },
  methods: {
    checkTokenValidity() {
      const router = useRouter();
      const AdminToken = localStorage.getItem("AdminToken");
      if (!validateToken(router, AdminToken, true)) {
        return;
      }
    },
    async getAllUsers() {
      try {
        const AdminToken = localStorage.getItem("AdminToken")
        const loadingStore = useLoadingStore();
        loadingStore.showLoading();
        const response = await axios.get('/users', {
          headers: {
            Authorization: `Bearer ${AdminToken}`
          }
        });
        loadingStore.hideLoading();
        this.users = response.data.data;
      } catch (error) {

      }
    },
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },
    add_style_function() {
      this.add_style = true;
    },
    async edit_style_function(id) {
      this.user_id = id;

      const AdminToken = localStorage.getItem("AdminToken");

      try {
        const response = await axios.get(`/users/${id}`, {
          headers: {
            Authorization: `Bearer ${AdminToken}`
          }
        });

        const userData = response.data.data;

        this.email = userData.email;
        this.password = userData.password;
        this.role = userData.role;
        this.remaining_traffic = userData.remaining_traffic;
        this.upload_limit = userData.upload_limit;
        this.download_limit = userData.download_limit;
        this.is_trial_user = userData.is_trial_user;
        this.real_name = userData.real_name;
        this.id_card = userData.id_card;
        this.real_name_status = userData.real_name_status;
        this.vip_start_time = userData.vip_start_time?.slice(0, 10);
        this.vip_end_time = userData.vip_end_time?.slice(0, 10);
        this.vip_status = userData.vip_status;

        this.edit_style = true;

      } catch (err) {
        console.error("获取用户信息失败：", err);
      }
    },
    search_style_function() {
      this.search_style = true;
    },
    getCurrentDate() {
      const date = new Date();
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    async confirmTheAddition() {
      const userData = {
        email: this.email,
        password: this.password,
        role: this.role,
        remaining_traffic: this.remaining_traffic,
        upload_limit: this.upload_limit,
        download_limit: this.download_limit,
        is_trial_user: this.is_trial_user,
        real_name: this.real_name,
        id_card: this.id_card,
        real_name_status: this.real_name_status,
        vip_start_time: this.vip_start_time,
        vip_end_time: this.vip_end_time,
        vip_status: this.vip_status,
      };

      // 邮箱校验
      if (!emailRegex.test(this.email)) {
        this.showMessageBox = true;
        this.messageBoxContent = "您的邮箱格式不符合条件，请更换后重试...";
        this.autoCloseMessageBox();
        return;
      }

      // 密码不能为空或太短
      if (!this.password || this.password.length < 6) {
        this.showMessageBox = true;
        this.messageBoxContent = "密码不能为空，且长度不能少于6位！";
        this.autoCloseMessageBox();
        return;
      }

      // 弱密码判断
      if (weakPasswords.test(this.password)) {
        this.showMessageBox = true;
        this.messageBoxContent = "您的密码太弱了，请更换一个复杂点的密码...";
        this.autoCloseMessageBox();
        return;
      }


      try {
        const response = await axios.post('/auth/register_backstage', userData)
        if (this.errorCodes.includes(response.data.code)) {
          this.showMessageBox = true
          this.messageBoxContent = response.data.message
          this.autoCloseMessageBox()
          return
        }
        this.showMessageBox = true;
        this.messageBoxContent = response.data.message;

        this.add_style = false;

        setTimeout(() => {
          window.location.reload();
        }, 1000)

      } catch (error) {
        console.error("失败：", error)
      }
    },
    async deleteUser(id) {
      try {
        const AdminToken = localStorage.getItem("AdminToken")
        const response = await axios.delete(`/auth/delete/${id}`, {
          headers: {
            'Authorization': AdminToken
          }
        })
        this.showMessageBox = true
        this.messageBoxContent = response.data.message
        this.autoCloseMessageBox()

        setTimeout(() => {
          window.location.reload();
        }, 1000)

      }
      catch (error) {
        console.error(error);
      }
    },
    async confirmTheEdit() {
      try {
        const AdminToken = localStorage.getItem("AdminToken");

        const payload = {
          userId: this.user_id,
          email: this.email,
          role: this.role,
          remainingTraffic: this.remaining_traffic,
          uploadLimit: this.upload_limit,
          downloadLimit: this.download_limit,
          isTrialUser: this.is_trial_user,
          realName: this.real_name,
          realNameStatus: this.real_name_status,
          vipStartTime: this.vip_start_time,
          vipEndTime: this.vip_end_time,
          vipStatus: this.vip_status
        };

        if (this.password && this.password.trim() !== "") {
          payload.password = this.password;
        }

        if (this.id_card && this.id_card.trim() !== "") {
          payload.idCard = this.id_card;
        }

        const response = await axios.post(
          `/auth/editUser`,
          payload,
          {
            headers: {
              'Authorization': AdminToken
            }
          }
        );

        this.showMessageBox = true;
        this.messageBoxContent = response.data.message;
        this.autoCloseMessageBox();

        setTimeout(() => {
          window.location.reload();
        }, 1000)

      } catch (error) {
        console.error(error);
      }
    },
    async confirmTheSearch() {
      try {
        const params = {
          email: this.email,
          role: this.role,
          isTrialUser: this.is_trial_user,
          realName: this.real_name,
          realNameStatus: this.real_name_status,
          vipStatus: this.vip_status
        };

        // 发起请求，传递查询参数
        const response = await axios.get('/users/search', {
          params: params, // 这里将查询条件作为 params 传递
        });

        console.log(response.data); // 输出查询结果
      } catch (error) {
        console.error("Error during search:", error);
      }
    },
    autoCloseMessageBox() {
      setTimeout(() => {
        this.showMessageBox = false;
      }, 3000);
    },
    handleCloseMessageBox() {
      this.showMessageBox = false;
    },
  },
  mounted() {
    this.checkTokenValidity();
    this.getAllUsers();
  },
}
</script>



<style scoped>
h1 {
  font-weight: 500;
  text-align: center;
  margin-bottom: 20px;
}

.features-box {
  margin: 0 auto;
  text-align: center;
  min-height: 86vh;
}

.head {
  padding: 10px 20px;
  background-color: #e1e1e1;
}

td {
  max-width: 180px;
  padding: 10px 20px;
  background-color: #f5f5f5;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}


.td_delete,
.td_edit {
  padding: 0;
}

.edit_delete,
.edit_button {
  height: 45px;
  width: 100px;
  cursor: pointer;
  background-color: #e4e4e4;
  border: none;
  color: #2c3e50;
  transition: all 0.1s;
}

.edit_delete:hover,
.edit_button:hover {
  background-color: #a8a8a8;
}

table {
  border-radius: 30px;
  margin-bottom: 30px;
}

table,
tr,
td,
th {
  border-collapse: collapse;
  border: 2px solid rgb(194, 194, 194);
}

.overlay_1 {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(78, 78, 78, 0.5);
  z-index: 999;
}

.edit,
.add {
  width: 22vw;
  background-color: #ffffff;
  position: fixed;
  top: 50%;
  left: 56%;
  transform: translate(-50%, -50%);
  z-index: 1000;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  padding: 30px 60px;
}

.edit select,
.edit input,
.add select,
.add input {
  width: 100%;
  height: 30px;
  border: 1px solid rgb(178, 178, 178);
  padding: 0 10px;
}

#email,
#password,
#role,
#remaining_traffic,
#upload_limit,
#download_limit,
#is_trial_user,
#real_name,
#id_card,
#real_name_status,
#vip_start_time,
#vip_end_time,
#vip_status {
  margin-bottom: 10px;
}

#vip_status {
  margin-bottom: 30px;
}

.edit label,
.add label {
  margin-bottom: 10px;
}

.confirm {
  margin-right: 20px;
}

.confirm,
.cancel {
  height: 35px;
  width: 80px;
  background-color: #4caf50;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 13px;
}

.confirm:hover,
.cancel:hover {
  background-color: #45a049;
}

.add_button {
  height: 35px;
  width: 50px;
  background-color: #767676;
  border: none;
  border-radius: 5px;
  color: white;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 13px;
  margin-bottom: 20px;
}

.add_button:hover {
  background-color: #686868;
}

.top {
  display: flex;
  justify-content: flex-end;
}


@media (prefers-color-scheme: dark) {}
</style>