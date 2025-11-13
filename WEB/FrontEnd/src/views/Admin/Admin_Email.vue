<script setup>
import Loading from '@/components/Loading.vue'
</script>

<template>
  <div class="Admin_Email">
    <Loading />
    <Header />
    <Admin_Sidebar />
    <main class="main-content">
      <Loading />

      <h1>邮件群发页</h1>

      <!-- 标签页切换 -->
      <van-tabs v-model:active="activeTab" sticky>
        <!-- 邮件发送标签页 -->
        <van-tab title="邮件发送" name="send">
          <section class="email-bulk-section">
            <div class="email-bulk-container">
              <h1 class="page-title">邮件群发</h1>

              <van-form @submit="sendBulkEmail">
                <!-- 邮件主题输入 -->
                <van-field v-model="emailForm.subject" label="邮件主题" placeholder="请输入邮件主题"
                  :rules="[{ required: true, message: '请填写邮件主题' }]" required />

                <!-- 邮件内容输入 -->
                <van-field v-model="emailForm.content" label="邮件内容" type="textarea" placeholder="请输入邮件内容"
                  :rules="[{ required: true, message: '请填写邮件内容' }]" required rows="8" autosize />

                <!-- 收件人信息 -->
                <van-cell-group title="收件人信息">
                  <van-cell title="收件人数量" :value="recipientCount + ' 位用户'" />
                  <van-cell title="查看用户列表" is-link @click="showUserList = true" :value="users.length + ' 位用户'" />
                </van-cell-group>

                <!-- 操作按钮 -->
                <div class="action-buttons">
                  <van-button class="send-btn" round block type="primary" native-type="submit" :loading="sending"
                    loading-text="正在发送..." icon="envelop-o" size="large">
                    {{ sending ? '正在发送...' : '发送邮件' }}
                  </van-button>

                  <van-button class="refresh-btn" round block type="success" @click="loadEmailData" :loading="loading"
                    icon="replay" size="normal">
                    {{ loading ? '刷新中...' : '刷新数据' }}
                  </van-button>
                </div>
              </van-form>
            </div>
          </section>
        </van-tab>

        <!-- 历史记录标签页 -->
        <van-tab title="历史记录" name="history">
          <section class="email-history-section">
            <div class="email-history-container">
              <h1 class="page-title">发送历史</h1>

              <div v-if="historyLoading" class="loading-container">
                <van-loading size="24px">加载中...</van-loading>
              </div>

              <div v-else-if="emailHistory.length === 0" class="empty-container">
                <van-empty description="暂无发送记录" />
              </div>

              <div v-else class="history-list">
                <van-card v-for="email in emailHistory" :key="email.id" :title="email.subject"
                  :desc="formatContent(email.content)" :thumb="null" class="history-card"
                  @click="showEmailDetail(email)">
                  <template #tags>
                    <van-tag type="primary">{{ email.sendStatus }}</van-tag>
                  </template>
                  <template #footer>
                    <div class="card-footer">
                      <span class="recipient-info">{{ email.recipientCount }} 位收件人</span>
                      <span class="time-info">{{ formatTime(email.createdAt) }}</span>
                    </div>
                  </template>
                </van-card>
              </div>
            </div>
          </section>
        </van-tab>
      </van-tabs>

      <!-- 用户列表弹窗 -->
      <van-popup v-model:show="showUserList" position="bottom" :style="{ height: '70%' }">
        <div class="user-list-popup">
          <div class="popup-header">
            <h3>用户邮箱列表 ({{ users.length }})</h3>
            <van-button size="small" @click="showUserList = false">关闭</van-button>
          </div>
          <div class="user-list-content">
            <van-search v-model="userSearchKeyword" placeholder="搜索用户邮箱" />
            <div class="user-email-list">
              <van-cell v-for="user in filteredUsers" :key="user" :title="user" size="small" />
            </div>
          </div>
        </div>
      </van-popup>

      <!-- 邮件详情弹窗 -->
      <van-popup v-model:show="showEmailDetailPopup" position="bottom" :style="{ height: '80%' }">
        <div class="email-detail-popup">
          <div class="popup-header">
            <h3>邮件详情</h3>
            <van-button size="small" @click="showEmailDetailPopup = false">关闭</van-button>
          </div>
          <div class="email-detail-content" v-if="selectedEmail">
            <van-cell-group title="邮件信息">
              <van-cell title="主题" :value="selectedEmail.subject" />
              <van-cell title="收件人数量" :value="selectedEmail.recipientCount + ' 位用户'" />
              <van-cell title="发送状态">
                <template #value>
                  <van-tag :type="getStatusType(selectedEmail.sendStatus)">
                    {{ selectedEmail.sendStatus }}
                  </van-tag>
                </template>
              </van-cell>
              <van-cell title="发送时间" :value="formatTime(selectedEmail.createdAt)" />
              <van-cell title="发送邮箱" :value="selectedEmail.senderEmail" />
            </van-cell-group>

            <van-cell-group title="邮件内容">
              <div class="email-content-text">{{ selectedEmail.content }}</div>
            </van-cell-group>
          </div>
        </div>
      </van-popup>

      <Footer />
    </main>
  </div>
</template>

<script>
import Header from '@/components/Header.vue';
import { useRouter } from 'vue-router';
import { validateToken } from '../../utils/token.js';
import { ref } from 'vue';
import axios from 'axios';
import Admin_Sidebar from '@/components/Admin_Sidebar.vue';
import Footer from '@/components/Footer.vue';
import { useLoadingStore } from '@/stores/loading'
import { showNotify } from 'vant';

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
      selectedEmail: null
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
        this.$router.push('/admin/login');
      }
    },

    // 加载邮件相关数据（用户列表和历史记录）
    async loadEmailData() {
      this.loading = true;
      try {
        await Promise.all([
          this.loadEmailCount(),
          this.loadEmailHistory()
        ]);
      } catch (error) {
        console.error('加载数据失败:', error);
      } finally {
        this.loading = false;
      }
    },

    // 加载收件人数量和用户列表
    async loadEmailCount() {
      try {
        const AdminToken = localStorage.getItem("AdminToken");
        const response = await axios.get('/emails/all-emails', {
          headers: {
            'Authorization': `Bearer ${AdminToken}`
          }
        });

        // 添加调试信息
        console.log('API响应:', response.data);

        if (response.data.code === '200' || response.data.code === 200) {
          this.users = response.data.data || [];
          this.recipientCount = this.users.length;
          console.log('设置用户数据:', this.users);
          console.log('收件人数量:', this.recipientCount);
        } else {
          console.error('API返回错误:', response.data);
          showNotify({
            type: 'danger',
            message: response.data.message || response.data.msg || '获取邮箱列表失败'
          });
        }
      } catch (error) {
        console.error('获取邮箱列表失败:', error);
        showNotify({
          type: 'danger',
          message: '获取邮箱列表失败，请检查网络连接'
        });
      }
    },

    // 加载历史邮件记录
    async loadEmailHistory() {
      this.historyLoading = true;
      try {
        const AdminToken = localStorage.getItem("AdminToken");
        const response = await axios.get('/emails/history?limit=50', {
          headers: {
            'Authorization': `Bearer ${AdminToken}`
          }
        });

        console.log('历史记录API响应:', response.data);

        if (response.data.code === '200' || response.data.code === 200) {
          this.emailHistory = response.data.data || [];
          console.log('设置历史记录:', this.emailHistory);
        } else {
          console.error('历史记录API返回错误:', response.data);
          showNotify({
            type: 'danger',
            message: response.data.message || response.data.msg || '获取历史记录失败'
          });
        }
      } catch (error) {
        console.error('获取历史记录失败:', error);
        showNotify({
          type: 'danger',
          message: '获取历史记录失败，请检查网络连接'
        });
      } finally {
        this.historyLoading = false;
      }
    },

    // 发送群发邮件
    async sendBulkEmail() {
      // 表单验证
      if (!this.emailForm.subject.trim()) {
        showNotify({
          type: 'danger',
          message: '请填写邮件主题'
        });
        return;
      }

      if (!this.emailForm.content.trim()) {
        showNotify({
          type: 'danger',
          message: '请填写邮件内容'
        });
        return;
      }

      // 确认发送
      const confirmed = confirm(`确定要发送邮件给 ${this.recipientCount} 位用户吗？\n\n主题：${this.emailForm.subject}\n\n此操作不可撤销！`);
      if (!confirmed) {
        return;
      }

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
          showNotify({
            type: 'success',
            message: `邮件发送成功！已发送给 ${response.data.data.recipientCount} 位用户`
          });

          // 清空表单
          this.emailForm.subject = '';
          this.emailForm.content = '';

          // 刷新历史记录
          await this.loadEmailHistory();
        } else {
          showNotify({
            type: 'danger',
            message: response.data.msg || '邮件发送失败'
          });
        }
      } catch (error) {
        console.error('邮件发送失败:', error);
        showNotify({
          type: 'danger',
          message: '邮件发送失败，请检查网络连接或邮件配置'
        });
      } finally {
        this.sending = false;
      }
    },

    // 显示邮件详情
    async showEmailDetail(email) {
      this.selectedEmail = email;
      this.showEmailDetailPopup = true;
    },

    // 格式化邮件内容
    formatContent(content) {
      if (!content) return '';
      return content.length > 100 ? content.substring(0, 100) + '...' : content;
    },

    // 格式化时间
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

    getStatusType(status) {
      switch (status) {
        case 'SUCCESS':
          return 'primary';
        case 'FAILED':
          return 'danger';
        case 'PARTIAL':
          return 'warning';
        default:
          return 'default';
      }
    }
  },
  mounted() {
    this.checkTokenValidity();
    this.loadEmailData();
  }
}
</script>