
<template>
  <div class="Admin_Email">
    <Header />
    <Admin_Sidebar />
    <main class="main-content">
      <div class="tab-header">
        <button class="tab-button" :class="{ active: activeTab === 'send' }" @click="activeTab = 'send'">
          邮件发送
        </button>
        <button class="tab-button" :class="{ active: activeTab === 'history' }" @click="activeTab = 'history'">
          历史记录
        </button>
      </div>

      <div v-show="activeTab === 'send'" class="content-section">
        <div class="form-container">
          <div class="form-row">
            <div class="form-field full-width">
              <label>邮件主题 *</label>
              <input v-model="emailForm.subject" type="text" placeholder="请输入邮件主题" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-field full-width">
              <label>邮件内容 *</label>
              <textarea v-model="emailForm.content" placeholder="请输入邮件内容" rows="8"></textarea>
            </div>
          </div>

          <div class="info-section">
            <div class="info-row">
              <label>收件人数量</label>
              <span class="info-value">{{ recipientCount }} 位用户</span>
            </div>
            <div class="info-row clickable" @click="showUserList = true">
              <label>查看用户列表</label>
              <span class="info-value">{{ users.length }} 位用户 →</span>
            </div>
          </div>

          <div class="form-buttons">
            <button class="send-btn" @click="sendBulkEmail" :disabled="sending">
              {{ sending ? '正在发送...' : '发送邮件' }}
            </button>
            <button class="refresh-btn" @click="loadEmailData" :disabled="loading">
              {{ loading ? '刷新中...' : '刷新数据' }}
            </button>
          </div>
        </div>
      </div>

      <div v-show="activeTab === 'history'" class="content-section">
        <div v-if="historyLoading" class="loading-container">
          <div class="loading-text">加载中...</div>
        </div>

        <div v-else-if="emailHistory.length === 0" class="empty-container">
          <div class="empty-text">暂无发送记录</div>
        </div>

        <div v-else class="history-table">
          <table>
            <thead>
              <tr>
                <th class="head">邮件主题</th>
                <th class="head">收件人数量</th>
                <th class="head">发送状态</th>
                <th class="head">发送时间</th>
                <th class="head">查看详情</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="email in emailHistory" :key="email.id" @click="showEmailDetail(email)">
                <td class="subject clickable">{{ formatContent(email.subject) }}</td>
                <td class="recipient-count">{{ email.recipientCount }} 位</td>
                <td class="status">
                  <span class="status-tag" :class="getStatusClass(email.sendStatus)">
                    {{ email.sendStatus }}
                  </span>
                </td>
                <td class="time">{{ formatTime(email.createdAt) }}</td>
                <td class="action">
                  <button class="detail-button" @click.stop="showEmailDetail(email)">查看</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-if="showUserList" class="overlay_1">
        <div class="popup">
          <h1>用户邮箱列表 ({{ users.length }})</h1>
          <div class="search-section">
            <input v-model="userSearchKeyword" type="text" placeholder="搜索用户邮箱" />
          </div>
          <div class="user-list">
            <div v-for="user in filteredUsers" :key="user" class="user-item">
              {{ user }}
            </div>
          </div>
          <div class="popup-buttons">
            <button class="close-btn" @click="showUserList = false">关闭</button>
          </div>
        </div>
      </div>

      <div v-if="showEmailDetailPopup" class="overlay_1">
        <div class="popup">
          <h1>邮件详情</h1>
          <div class="email-info">
            <div class="info-row">
              <label>主题</label>
              <span>{{ selectedEmail?.subject }}</span>
            </div>
            <div class="info-row">
              <label>收件人数量</label>
              <span>{{ selectedEmail?.recipientCount }} 位用户</span>
            </div>
            <div class="info-row">
              <label>发送状态</label>
              <span class="status-tag" :class="getStatusClass(selectedEmail?.sendStatus)">
                {{ selectedEmail?.sendStatus }}
              </span>
            </div>
            <div class="info-row">
              <label>发送时间</label>
              <span>{{ formatTime(selectedEmail?.createdAt) }}</span>
            </div>
            <div class="info-row">
              <label>发送邮箱</label>
              <span>{{ selectedEmail?.senderEmail }}</span>
            </div>
          </div>
          <div class="email-content">
            <label>邮件内容</label>
            <div class="content-text">{{ selectedEmail?.content }}</div>
          </div>
          <div class="popup-buttons">
            <button class="close-btn" @click="showEmailDetailPopup = false">关闭</button>
          </div>
        </div>
      </div>

      </main>
  </div>
</template>

<script>
import Header from '@/modules/common/components/Header.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '@/modules/auth/utils/token.js';
import axios from 'axios';
import Admin_Sidebar from '@/modules/admin/components/Admin_Sidebar.vue';

export default {
  name: 'Admin_Email',
  components: {
    Header,
    Admin_Sidebar,
  },
  data() {
    return {
      activeTab: 'send',
      emailForm: {
        subject: '',
        content: ''
      },
      recipientCount: 0,
      loading: false,
      sending: false,
      users: [],
      userSearchKeyword: '',
      showUserList: false,
      emailHistory: [],
      historyLoading: false,
      showEmailDetailPopup: false,
      selectedEmail: null,
      };
  },
  computed: {
    filteredUsers() {
      if (!this.userSearchKeyword) {
        return this.users;
      }
      return this.users.filter(user =>
        user.toLowerCase().includes(this.userSearchKeyword.toLowerCase())
      );
    }
  },
  methods: {
  
    checkTokenValidity() {
      const router = useRouter();
      const AdminToken = localStorage.getItem("AdminToken");
      if (!validateToken(router, AdminToken, true)) {
        this.$router.push({ name: 'Admin_Login' });
      }
    },

    validateEmailForm() {
      if (this.recipientCount === 0) {
        return {
          valid: false,
          message: '没有可发送的收件人，请先确保有用户可接收邮件'
        };
      }

      if (!this.emailForm.subject.trim()) {
        return {
          valid: false,
          message: '邮件主题不能为空，请输入邮件主题'
        };
      }

      if (this.emailForm.subject.trim().length < 2) {
        return {
          valid: false,
          message: '邮件主题太短，至少需要2个字符'
        };
      }

      if (!this.emailForm.content.trim()) {
        return {
          valid: false,
          message: '邮件内容不能为空，请输入邮件内容'
        };
      }

      if (this.emailForm.content.trim().length < 5) {
        return {
          valid: false,
          message: '邮件内容太短，至少需要5个字符'
        };
      }

      return {
        valid: true,
        message: ''
      };
    },

    async loadEmailData() {
      this.loading = true;
      try {
        await Promise.all([
          this.loadEmailCount(),
          this.loadEmailHistory()
        ]);
      } catch (error) {
      } finally {
        this.loading = false;
      }
    },

    async loadEmailCount() {
      try {
        const AdminToken = localStorage.getItem("AdminToken");
        const response = await axios.get('/emails/all-emails', {
          headers: {
            'Authorization': `Bearer ${AdminToken}`
          }
        });


        if (response.data.code === '200' || response.data.code === 200) {
          this.users = response.data.data || [];
          this.recipientCount = this.users.length;
        } else {
          this.$message.error(response.data.message || response.data.msg || '获取邮箱列表失败');
        }
      } catch (error) {
        this.$message.error('获取邮箱列表失败，请检查网络连接');
      }
    },

    async loadEmailHistory() {
      this.historyLoading = true;
      try {
        const AdminToken = localStorage.getItem("AdminToken");
        const response = await axios.get('/emails/history?limit=50', {
          headers: {
            'Authorization': `Bearer ${AdminToken}`
          }
        });

        if (response.data.code === '200' || response.data.code === 200) {
          this.emailHistory = response.data.data || [];
        } else {
          this.$message.error(response.data.message || response.data.msg || '获取历史记录失败');
        }
      } catch (error) {
        this.$message.error('获取历史记录失败，请检查网络连接');
      } finally {
        this.historyLoading = false;
      }
    },

    async sendBulkEmail() {
      const validation = this.validateEmailForm();

      if (!validation.valid) {
        this.$message.error(validation.message);
        return;
      }

      if (confirm(`确定要发送邮件给 ${this.recipientCount} 位用户吗？\n\n主题：${this.emailForm.subject}\n\n此操作不可撤销！`)) {
        this.sending = true;
        try {
          const AdminToken = localStorage.getItem("AdminToken");
          const response = await axios.post('/emails/send-bulk', this.emailForm, {
            headers: {
              'Authorization': `Bearer ${AdminToken}`,
              'Content-Type': 'application/json'
            }
          });

          if (response.data.code === 200) {
            this.$message.success(`邮件发送成功！已发送给 ${response.data.data.recipientCount} 位用户`);

            this.emailForm.subject = '';
            this.emailForm.content = '';

            await this.loadEmailHistory();
          } else {
            this.$message.error(response.data.msg || '邮件发送失败');
          }
        } catch (error) {
          this.$message.error('邮件发送失败，请检查网络连接或邮件配置');
        } finally {
          this.sending = false;
        }
      }
    },
    async showEmailDetail(email) {
      this.selectedEmail = email;
      this.showEmailDetailPopup = true;
    },

    formatContent(content) {
      if (!content) return '';
      return content.length > 100 ? content.substring(0, 100) + '...' : content;
    },

    formatTime(time) {
      if (!time) return '';
      const date = new Date(time);
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      });
    },

  
    getStatusClass(status) {
      switch (status) {
        case 'SUCCESS':
          return 'status-success';
        case 'FAILED':
          return 'status-failed';
        case 'PARTIAL':
          return 'status-partial';
        default:
          return 'status-default';
      }
    }
  },
  mounted() {
    this.checkTokenValidity();
    this.loadEmailData();
  }
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

.tab-header {
  display: flex;
  justify-content: center;
  gap: 0;
  margin-bottom: 30px;
  padding: 0 20px;
}

.tab-button {
  height: 50px;
  width: 150px;
  background: none;
  border: none;
  border-bottom: 2px solid transparent;
  color: #666;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  font-weight: 500;
  outline: none;
}

.tab-button:hover {
  color: #333;
}

.tab-button:focus {
  border-bottom-color: #4caf50;
  color: #4caf50;
}

.tab-button.active {
  border-bottom-color: #4caf50;
  color: #4caf50;
  font-weight: 600;
}

.content-section {
  background: #ffffff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin: 0 40px 40px 40px;
}

.form-container {
  width: 100%;
  max-width: none;
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

.form-field.full-width {
  width: 100%;
}

.form-field label {
  margin-bottom: 8px;
  color: #2c3e50;
  font-weight: 500;
  font-size: 14px;
  text-align: left;
  order: -1;
}

.form-field input,
.form-field textarea {
  width: 100%;
  height: 46px;
  border: 2px solid #e1e8ed;
  padding: 0 12px;
  border-radius: 8px;
  font-size: 14px;
  transition: all 0.2s ease;
  box-sizing: border-box;
  background-color: #ffffff;
}

.form-field textarea {
  min-height: 120px;
  padding: 12px;
  resize: vertical;
}

.form-field input:focus,
.form-field textarea:focus {
  outline: none;
}

.info-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 20px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  border-bottom: 1px solid #e9ecef;
}

.info-row:last-child {
  border-bottom: none;
}

.info-row.clickable {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.info-row.clickable:hover {
  background-color: #f1f3f4;
  border-radius: 4px;
}

.info-row label {
  font-weight: 600;
  color: #2c3e50;
}

.info-value {
  color: #666;
  font-weight: 500;
}

.form-buttons {
  display: flex;
  gap: 16px;
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
}

.send-btn {
  flex: 2;
  height: 50px;
  background-color: #4caf50;
  border: none;
  border-radius: 8px;
  color: white;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.2s ease;
}

.send-btn:hover:not(:disabled) {
  background-color: #45a049;
}

.send-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.refresh-btn {
  flex: 1;
  height: 50px;
  background-color: #66bb6a;
  border: none;
  border-radius: 8px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
}

.refresh-btn:hover:not(:disabled) {
  background-color: #4caf50;
}

.refresh-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.history-table {
  overflow-x: auto;
}

.history-table table {
  width: 100%;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-collapse: separate;
  border-spacing: 0;
}

.head {
  padding: 12px 16px;
  background-color: #4caf50;
  color: white;
  font-weight: 600;
  font-size: 14px;
  white-space: nowrap;
}

.history-table td {
  padding: 12px 16px;
  background-color: #f1f8e9;
  border-bottom: 1px solid #c8e6c9;
}

.history-table td.clickable {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.history-table td.clickable:hover {
  background-color: #dcedc8;
}

.status-tag {
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 600;
  text-transform: uppercase;
}

.status-success {
  background-color: #4caf50;
  color: white;
}

.status-failed {
  background-color: #f44336;
  color: white;
}

.status-partial {
  background-color: #ff9800;
  color: white;
}

.status-default {
  background-color: #9e9e9e;
  color: white;
}

.detail-button {
  height: 36px;
  width: 60px;
  background-color: #66bb6a;
  border: none;
  border-radius: 6px;
  color: white;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 13px;
}

.detail-button:hover {
  background-color: #4caf50;
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

.popup {
  width: 90%;
  max-width: 600px;
  max-height: 80vh;
  background-color: #ffffff;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  padding: 30px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.15);
  overflow-y: auto;
}

.popup h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
  font-weight: 600;
  font-size: 1.5rem;
}

.search-section {
  margin-bottom: 20px;
}

.search-section input {
  width: 100%;
  height: 46px;
  border: 2px solid #e1e8ed;
  padding: 0 12px;
  border-radius: 8px;
  font-size: 14px;
  box-sizing: border-box;
}

.search-section input:focus {
  outline: none;
}

.user-list {
  max-height: 400px;
  overflow-y: auto;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  background-color: #f8f9fa;
}

.user-item {
  padding: 12px 16px;
  border-bottom: 1px solid #e9ecef;
  font-size: 14px;
  color: #333;
}

.user-item:last-child {
  border-bottom: none;
}

.user-item:hover {
  background-color: #f1f3f4;
}

.email-info,
.email-content {
  margin-bottom: 20px;
}

.email-info .info-row {
  background-color: #f8f9fa;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 8px;
  border: none;
}

.content-text {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 16px;
  min-height: 120px;
  max-height: 300px;
  overflow-y: auto;
  white-space: pre-wrap;
  line-height: 1.5;
  color: #333;
  font-family: inherit;
}

.popup-buttons {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #e9ecef;
}

.close-btn {
  height: 42px;
  width: 100px;
  background-color: #9e9e9e;
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 14px;
  font-weight: 500;
}

.close-btn:hover {
  background-color: #757575;
}

.loading-container,
.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 200px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.loading-text,
.empty-text {
  color: #666;
  font-size: 16px;
}

@media (max-width: 768px) {
  .tab-header {
    padding: 0 10px;
  }

  .tab-button {
    width: 120px;
    font-size: 14px;
    height: 45px;
  }

  .content-section {
    padding: 20px;
    margin: 0 20px 20px 20px;
  }

  .form-row {
    flex-direction: column;
    gap: 15px;
  }

  .form-field {
    width: 100%;
  }

  .form-buttons {
    flex-direction: column;
    gap: 12px;
  }

  .send-btn,
  .refresh-btn {
    width: 100%;
  }

  .popup {
    width: 95%;
    padding: 20px;
    max-height: 90vh;
  }
}

@media (prefers-color-scheme: dark) {
  h1 {
    color: #e9ecef;
  }

  .tab-button {
    color: #b0b0b0;
  }

  .tab-button:hover {
    color: #e9ecef;
  }

  .tab-button:focus,
  .tab-button.active {
    color: #4caf50;
  }

  .content-section,
  .popup {
    background-color: #1e1e1e;
    color: #e9ecef;
  }

  .form-field input,
  .form-field textarea,
  .search-section input {
    background-color: #2d3748;
    border-color: #4a5568;
    color: #e9ecef;
  }

  .form-field label {
    color: #e9ecef;
  }

  .info-section,
  .email-info .info-row,
  .content-text {
    background-color: #2d3748;
    color: #e9ecef;
  }

  .info-row label {
    color: #e9ecef;
  }

  .info-value {
    color: #b0b0b0;
  }

  .user-list {
    background-color: #2d3748;
    border-color: #4a5568;
  }

  .user-item {
    color: #e9ecef;
    border-bottom-color: #4a5568;
  }

  .user-item:hover {
    background-color: #37474f;
  }

  .history-table td {
    background-color: #2d3748;
    color: #e9ecef;
    border-bottom-color: #1e1e1e;
  }

  .history-table td.clickable:hover {
    background-color: #37474f;
  }

  .loading-container,
  .empty-container {
    background-color: #2d3748;
  }

  .loading-text,
  .empty-text {
    color: #b0b0b0;
  }
}
</style>