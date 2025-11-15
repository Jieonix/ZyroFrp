<script setup>
import Loading from '@/modules/common/components/Loading.vue'
import ConfirmDialog from '@/modules/common/components/ConfirmDialog.vue'
</script>

<template>
  <div class="Admin_Users">
    <Loading />
    <Header />
    <Admin_Sidebar />
    <main class="main-content">
      <Loading />
      <section class="features">
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
              <td class="user-id clickable" @click="copyToClipboard(user.user_id)">{{ user.user_id }}</td>
              <td class="email clickable" @click="copyToClipboard(user.email)">{{ user.email }}</td>
              <td class="email clickable" @click="copyToClipboard(user.user_key)">{{ user.user_key }}</td>
              <td class="role clickable" @click="copyToClipboard(user.role)">{{ user.role }}</td>
              <td class="remaining-traffic clickable" @click="copyToClipboard(user.remaining_traffic)">{{
                user.remaining_traffic }}</td>
              <td class="upload-limit clickable" @click="copyToClipboard(user.upload_limit)">{{ user.upload_limit }}
              </td>
              <td class="download-limit clickable" @click="copyToClipboard(user.download_limit)">{{ user.download_limit
                }}</td>
              <td class="is-trial-user clickable" @click="copyToClipboard(user.is_trial_user ? '是' : '否')">{{
                user.is_trial_user ? '是' : '否' }}</td>
              <td class="vip-start-time clickable" @click="copyToClipboard(formatDate(user.vip_start_time))">{{
                formatDate(user.vip_start_time) }}</td>
              <td class="vip-end-time clickable" @click="copyToClipboard(formatDate(user.vip_end_time))">{{
                formatDate(user.vip_end_time) }}</td>
              <td class="vip-status clickable" @click="copyToClipboard(user.vip_status ? 'VIP' : '非VIP')">{{
                user.vip_status ? 'VIP' : '非VIP' }}</td>
              <td class="real-name clickable" @click="copyToClipboard(user.real_name)">{{ user.real_name }}</td>
              <td class="real-name-status clickable" @click="copyToClipboard(user.real_name_status ? '已实名' : '未实名')">{{
                user.real_name_status ? '已实名' : '未实名' }}</td>
              <td class="created-at clickable" @click="copyToClipboard(formatDate(user.created_at))">{{
                formatDate(user.created_at) }}</td>
              <td class="updated-at clickable" @click="copyToClipboard(formatDate(user.updated_at))">{{
                formatDate(user.updated_at) }}</td>
              <td class="td_edit"><button class="edit_button" @click="edit_style_function(user.user_id)">编辑</button>
              </td>
              <td class="td_delete"><button class="edit_delete" @click="deleteUser(user.user_id)">删除</button></td>
            </tr>
          </tbody>
        </table>
        <div v-if="add_style" class="overlay_1">
          <div class="add">
            <h1>新增用户</h1>
            <div class="form-row">
              <div class="form-field">
                <label for="">账号*</label>
                <input id="email" v-model="email" type="text">
              </div>
              <div class="form-field">
                <label for="">密码*</label>
                <input id="password" v-model="password" type="password">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">权限</label>
                <select v-model="role" id="role">
                  <option value="User">User</option>
                  <option value="Admin">Admin</option>
                  <option value="SuperAdmin">SuperAdmin</option>
                </select>
              </div>
              <div class="form-field">
                <label for="">总流量 (GB)</label>
                <input v-model="remaining_traffic" id="remaining_traffic" type="number" value="4000">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">上行限速 (Mb)</label>
                <input v-model="upload_limit" id="upload_limit" type="number" value="51888">
              </div>
              <div class="form-field">
                <label for="">下行限速 (Mb)</label>
                <input v-model="download_limit" id="download_limit" type="number" value="51888">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">先锋体验用户</label>
                <select v-model.number="is_trial_user" id="is_trial_user">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </div>
              <div class="form-field">
                <label>真实姓名</label>
                <input v-model="real_name" id="real_name" type="text" placeholder="张xx">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>身份证号（加密）</label>
                <input v-model="id_card" id="id_card" type="text" placeholder="652xxxxxxxxxxxxxxx">
              </div>
              <div class="form-field">
                <label>是否实名</label>
                <select v-model.number="real_name_status" id="real_name_status">
                  <option value="1">已实名</option>
                  <option value="0">未实名</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>会员开始时间</label>
                <input v-model="vip_start_time" id="vip_start_time" type="date">
              </div>
              <div class="form-field">
                <label>会员结束时间</label>
                <input v-model="vip_end_time" id="vip_end_time" type="date">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>会员状态</label>
                <select v-model.number="vip_status" id="vip_status">
                  <option value="0">非VIP</option>
                  <option value="1">VIP</option>
                </select>
              </div>
            </div>
            <div>
              <button type="button" @click="confirmTheAddition" class="confirm">确认新增</button>
              <button type="button" @click="add_style = false" class="cancel">取消</button>
            </div>
          </div>
        </div>
        <div v-if="edit_style" class="overlay_1">
          <div class="edit">
            <h1>修改用户</h1>
            <div class="form-row">
              <div class="form-field">
                <label for="">账号*</label>
                <input id="email" v-model="email" type="text">
              </div>
              <div class="form-field">
                <label for="">密码*（默认不显示）</label>
                <input id="password" v-model="password" type="text">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">权限</label>
                <select v-model="role" id="role">
                  <option value="User">User</option>
                  <option value="Admin">Admin</option>
                  <option value="SuperAdmin">SuperAdmin</option>
                </select>
              </div>
              <div class="form-field">
                <label for="">总流量 (GB)</label>
                <input v-model="remaining_traffic" id="remaining_traffic" type="number" value="4000">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">上行限速 (Mb)</label>
                <input v-model="upload_limit" id="upload_limit" type="number" value="51888">
              </div>
              <div class="form-field">
                <label for="">下行限速 (Mb)</label>
                <input v-model="download_limit" id="download_limit" type="number" value="51888">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">先锋体验用户</label>
                <select v-model.number="is_trial_user" id="is_trial_user">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </div>
              <div class="form-field">
                <label>真实姓名</label>
                <input v-model="real_name" id="real_name" type="text" placeholder="">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>身份证号（默认不显示）</label>
                <input v-model="id_card" id="id_card" type="text" placeholder="">
              </div>
              <div class="form-field">
                <label>是否实名</label>
                <select v-model.number="real_name_status" id="real_name_status">
                  <option value="1">已实名</option>
                  <option value="0">未实名</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>会员开始时间</label>
                <input v-model="vip_start_time" id="vip_start_time" type="date">
              </div>
              <div class="form-field">
                <label>会员结束时间</label>
                <input v-model="vip_end_time" id="vip_end_time" type="date">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>会员状态</label>
                <select v-model.number="vip_status" id="vip_status">
                  <option value="0">非VIP</option>
                  <option value="1">VIP</option>
                </select>
              </div>
            </div>
            <div>
              <button type="button" @click="confirmTheEdit(this.user_id)" class="confirm">确认修改</button>
              <button type="button" @click="edit_style = false" class="cancel">取消</button>
            </div>
          </div>
        </div>
        <div v-if="search_style" class="overlay_1">
          <div class="add">
            <h1>查询用户</h1>
            <div class="form-row">
              <div class="form-field">
                <label for="">账号*</label>
                <input id="email" v-model="email" type="text">
              </div>
              <div class="form-field">
                <label for="">权限</label>
                <select v-model="role" id="role">
                  <option value="User">User</option>
                  <option value="Admin">Admin</option>
                  <option value="SuperAdmin">SuperAdmin</option>
                </select>
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label for="">先锋体验用户</label>
                <select v-model.number="is_trial_user" id="is_trial_user">
                  <option value="1">是</option>
                  <option value="0">否</option>
                </select>
              </div>
              <div class="form-field">
                <label>真实姓名</label>
                <input v-model="real_name" id="real_name" type="text" placeholder="张xx">
              </div>
            </div>
            <div class="form-row">
              <div class="form-field">
                <label>是否实名</label>
                <select v-model.number="real_name_status" id="real_name_status">
                  <option value="1">已实名</option>
                  <option value="0">未实名</option>
                </select>
              </div>
              <div class="form-field">
                <label>会员状态</label>
                <select v-model.number="vip_status" id="vip_status">
                  <option value="0">非VIP</option>
                  <option value="1">VIP</option>
                </select>
              </div>
            </div>
            <div>
              <button type="button" @click="confirmTheSearch" class="confirm">开始查询</button>
              <button type="button" @click="search_style = false" class="cancel">取消</button>
            </div>
          </div>
        </div>
      </section>

      <ConfirmDialog
        :visible="confirmDialog.show"
        :title="confirmDialog.title"
        :message="confirmDialog.message"
        :confirmText="confirmDialog.confirmText"
        :cancelText="confirmDialog.cancelText"
        :loading="confirmDialog.loading"
        :loadingText="confirmDialog.loadingText"
        @confirm="handleConfirm"
        @cancel="handleCancel"
        @close="handleCancel"
      />

      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import axios, { Axios } from 'axios';
import Admin_Sidebar from '@/modules/admin/components/Admin_Sidebar.vue';
import Footer from '@/modules/common/components/Footer.vue';
import { useLoadingStore } from '@/modules/common/stores/loading';
import { emailRegex, weakPasswords } from '@/modules/common/utils/validation.js'


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
      errorCodes: [
        "REGISTER_4001",
        "REGISTER_4002",
        "REGISTER_4003",
        "REGISTER_4004",
        "REGISTER_4005",
        "REGISTER_4006",
        "EMAIL_SEND_4301"
      ],
      confirmDialog: {
        show: false,
        title: '',
        message: '',
        confirmText: '确认',
        cancelText: '取消',
        loading: false,
        loadingText: '处理中...',
        callback: null,
        callbackArgs: []
      },
    };
  },
  methods: {
    showConfirm(options) {
      this.confirmDialog = {
        show: true,
        title: options.title || '确认操作',
        message: options.message || '确定要执行此操作吗？',
        confirmText: options.confirmText || '确认',
        cancelText: options.cancelText || '取消',
        loading: false,
        loadingText: options.loadingText || '处理中...',
        callback: options.callback || null,
        callbackArgs: options.callbackArgs || []
      };
    },

    async handleConfirm() {
      if (this.confirmDialog.callback) {
        this.confirmDialog.loading = true;
        try {
          await this.confirmDialog.callback(...this.confirmDialog.callbackArgs);
        } catch (error) {
        } finally {
          this.confirmDialog.loading = false;
          this.confirmDialog.show = false;
        }
      }
    },

    handleCancel() {
      this.confirmDialog.show = false;
      this.confirmDialog.loading = false;
    },

    copyToClipboard(text) {
      if (!text) return;

      navigator.clipboard.writeText(text.toString()).then(() => {
        this.$message.success('已复制到剪贴板: ' + text);
      }).catch(err => {
        const textArea = document.createElement('textarea');
        textArea.value = text;
        document.body.appendChild(textArea);
        textArea.select();
        document.execCommand('copy');
        document.body.removeChild(textArea);
        this.$message.success('已复制到剪贴板: ' + text);
      });
    },
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
      if (!emailRegex.test(this.email)) {
        this.$message.error("您的邮箱格式不符合条件，请更换后重试...");
        return;
      }

      if (!this.password || this.password.length < 6) {
        this.$message.error("密码不能为空，且长度不能少于6位！");
        return;
      }

      if (weakPasswords.test(this.password)) {
        this.$message.warning("您的密码太弱了，请更换一个复杂点的密码...");
        return;
      }

      this.showConfirm({
        title: '新增用户',
        message: `确定要新增用户 "${this.email}" 吗？\n\n请检查填写的信息是否正确。`,
        confirmText: '确认新增',
        loadingText: '正在新增...',
        callback: this.doAddUser
      });
    },

    async doAddUser() {
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

      try {
        const response = await axios.post('/auth/register_backstage', userData)
        if (this.errorCodes.includes(response.data.code)) {
          this.$message.error(response.data.message)
          return
        }
        this.$message.success(response.data.message);

        this.add_style = false;

        setTimeout(() => {
          window.location.reload();
        }, 1000)

      } catch (error) {
        this.$message.error('新增用户失败');
        throw error;
      }
    },
    async deleteUser(id) {
      const user = this.users.find(u => u.user_id === id);

      this.showConfirm({
        title: '删除用户',
        message: `确定要删除用户 "${user.email}" 吗？\n\n此操作不可撤销！`,
        confirmText: '确认删除',
        loadingText: '正在删除...',
        callback: this.doDeleteUser,
        callbackArgs: [id]
      });
    },

    async doDeleteUser(id) {
      try {
        const AdminToken = localStorage.getItem("AdminToken")
        const response = await axios.delete(`/auth/delete/${id}`, {
          headers: {
            'Authorization': AdminToken
          }
        })
        this.$message.success(response.data.message)

        setTimeout(() => {
          window.location.reload();
        }, 1000)
      }
      catch (error) {
        this.$message.error('删除用户失败');
        throw error;
      }
    },
    async confirmTheEdit() {
      this.showConfirm({
        title: '修改用户',
        message: `确定要修改用户 "${this.email}" 的信息吗？\n\n请检查填写的信息是否正确。`,
        confirmText: '确认修改',
        loadingText: '正在修改...',
        callback: this.doEditUser
      });
    },

    async doEditUser() {
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
        this.messageBoxType = "success";
        this.autoCloseMessageBox();

        setTimeout(() => {
          window.location.reload();
        }, 1000)

      } catch (error) {
        this.$message.error('修改用户信息失败');
        throw error;
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

        const response = await axios.get('/users/search', {
          params: params,
        });

      } catch (error) {
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
  font-weight: 600;
  text-align: center;
  margin-bottom: 30px;
  color: #2c3e50;
  font-size: 2rem;
}

.head {
  padding: 12px 16px;
  background-color: #4caf50;
  color: white;
  font-weight: 600;
  font-size: 14px;
  white-space: nowrap;
}

td {
  padding: 1rem;
  text-align: center;
  background-color: #f1f8e9;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-bottom: 1px solid #c8e6c9;
  max-width: 120px;
}

td.clickable {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

td.clickable:hover {
  background-color: #dcedc8;
}

.td_delete,
.td_edit {
  padding: 8px;
  background: transparent;
}

.edit_delete,
.edit_button {
  height: 2.5rem;
  width: 5rem;
  cursor: pointer;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  transition: all 0.2s ease;
  font-size: 13px;
}

.edit_button {
  background-color: #66bb6a;
}

.edit_delete {
  background-color: #f44336;
}

.edit_button:hover {
  background-color: #4caf50;
}

.edit_delete:hover {
  background-color: #d32f2f;
}

table {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-collapse: separate;
  border-spacing: 0;
  margin-bottom: 30px;
}

table thead {
  border-radius: 12px 12px 0 0;
}



table th,
table td {
  border: none;
}

.overlay_1 {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  backdrop-filter: blur(5px);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.edit,
.add {
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  background-color: #ffffff;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  padding: 30px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  animation: slideIn 0.3s ease;
  overflow-y: auto;
}

@keyframes slideIn {
  from {
    transform: translateY(-50px);
    opacity: 0;
  }

  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.edit h1,
.add h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 1.5rem;
  position: sticky;
  top: 0;
  background-color: #ffffff;
  padding: 10px 0;
  z-index: 10;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;
}

.form-field {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.edit select,
.edit input,
.add select,
.add input {
  width: 100%;
  height: 46px;
  border: 2px solid #e1e8ed;
  padding: 0 12px;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s ease;
  box-sizing: border-box;
}

.edit select:focus,
.edit input:focus,
.add select:focus,
.add input:focus {
  outline: none;
}

.edit input::placeholder,
.add input::placeholder {
  color: #aab8c2;
}

.edit label,
.add label {
  margin-bottom: 8px;
  color: #2c3e50;
  font-weight: 500;
  font-size: 14px;
  text-align: left;
  order: -1;
}

.edit>div:last-child,
.add>div:last-child {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
  position: sticky;
  bottom: 0;
  background-color: #ffffff;
  padding-bottom: 10px;
}

@media (prefers-color-scheme: dark) {

  .edit>div:last-child,
  .add>div:last-child {
    border-top-color: #4a5568;
    background-color: #1e1e1e;
  }
}

.confirm {
  margin-right: 16px;
  background-color: #4caf50;
}

.confirm,
.cancel {
  height: 3rem;
  width: 5rem;
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
}

.cancel {
  background-color: #f44336;
  color: white;
}

.confirm:hover {
  background-color: #45a049;
}

.cancel:hover {
  background-color: #d32f2f;
}

.search_button,
.add_button {
  height: 2.5rem;
  width: 5rem;
  background-color: #4caf50;
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
  margin-bottom: 24px;
  margin-left: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search_button,
.add_button {
  background-color: #4caf50;
}

.search_button:hover,
.add_button:hover {
  background-color: #3c9f3f;
}

.top {
  display: flex;
  justify-content: flex-end;
  padding: 0 20px;
}

.features {
  display: block;
  padding: 0 20px;
}

@media (max-width: 1200px) {
  td {
    max-width: 100px;
    font-size: 12px;
    padding: 8px 12px;
  }

  .edit_delete,
  .edit_button {
    width: 60px;
    height: 30px;
    font-size: 11px;
  }
}

@media (max-width: 768px) {
  .features {
    padding: 10px;
    overflow-x: auto;
  }

  table {
    font-size: 12px;
    min-width: 800px;
  }

  .head {
    padding: 8px 12px;
    font-size: 12px;
  }

  td {
    max-width: 80px;
    padding: 6px 8px;
  }

  .edit,
  .add {
    width: 95%;
    max-width: none;
    padding: 20px;
    max-height: 90vh;
  }

  .edit h1,
  .add h1 {
    font-size: 1.2rem;
  }

  .form-row {
    flex-direction: column;
    gap: 15px;
  }

  .form-field {
    width: 100%;
  }

  .edit select,
  .edit input,
  .add select,
  .add input {
    height: 44px;
  }
}

@media (prefers-color-scheme: dark) {
  h1 {
    color: #e9ecef;
  }

  .features-box {
    background: #1e1e1e;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  }

  .head {
    background-color: #2e7d32;
  }

  td {
    background-color: #2d3748;
    color: #e9ecef;
    border-bottom-color: #1e1e1e;
  }

  td.clickable:hover {
    background-color: #37474f;
  }

  .edit,
  .add {
    background-color: #1e1e1e;
    color: #e9ecef;
    box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
  }

  .edit h1,
  .add h1 {
    color: #e9ecef;
    background-color: #1e1e1e;
  }

  .edit select,
  .edit input,
  .add select,
  .add input {
    background-color: #2d3748;
    border-color: #4a5568;
    color: #e9ecef;
  }

  
  .edit input::placeholder,
  .add input::placeholder {
    color: #a0aec0;
  }

  .edit label,
  .add label {
    color: #e9ecef;
  }

  .cancel {
    background-color: #424242;
    color: #e9ecef;
  }

  .cancel:hover {
    background-color: #616161;
  }

  .search_button,
  .add_button {
    background-color: #2e7d32;
  }

  .add_button {
    background-color: #388e3c;
  }

  .edit_button {
    background-color: #388e3c;
  }
}
</style>