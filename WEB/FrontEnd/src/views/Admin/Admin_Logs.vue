<script>
import Loading from '@/components/Loading.vue'
import ConfirmDialog from '@/components/ConfirmDialog.vue'
import Header from '@/components/Header.vue'
import Admin_Sidebar from '@/components/Admin_Sidebar.vue'
import { useRouter } from 'vue-router'
import { validateToken } from '../../utils/token.js'
import axios from 'axios'
import '@/assets/api.js'
import { showToast, showLoadingToast, closeToast } from 'vant'

export default {
  name: 'Admin_Logs',
  components: {
    Loading,
    ConfirmDialog,
    Header,
    Admin_Sidebar,
  },
  data() {
    return {
      logs: [],
      loading: false,
      searchForm: {
        logType: '',
        userEmail: '',
        operation: '',
        module: '',
        ipAddress: '',
        status: '',
        startTime: '',
        endTime: ''
      },
      logTypeOptions: [
        { text: '全部', value: '' },
        { text: '登录日志', value: 'LOGIN' },
        { text: '操作日志', value: 'OPERATION' },
        { text: '错误日志', value: 'ERROR' },
        { text: '安全日志', value: 'SECURITY' }
      ],
      statusOptions: [
        { text: '全部', value: '' },
        { text: '成功', value: 'SUCCESS' },
        { text: '失败', value: 'FAILED' },
        { text: '错误', value: 'ERROR' }
      ],
      moduleOptions: [
        { text: '全部', value: '' },
        { text: '认证模块', value: '认证模块' },
        { text: '用户模块', value: '用户模块' },
        { text: '隧道模块', value: '隧道模块' },
        { text: '邮件模块', value: '邮件模块' },
        { text: '服务器模块', value: '服务器模块' },
        { text: '管理模块', value: '管理模块' }
      ],
      showSearchPanel: false,
      selectedLog: null,
      showLogDetail: false,
      router: useRouter()
    }
  },
  computed: {
  },
  methods: {
    checkTokenValidity() {
      const AdminToken = localStorage.getItem("AdminToken");
      if (!validateToken(this.router, AdminToken, true)) {
        return;
      }
    },

    async fetchLogs() {
      try {
        this.loading = true
        showLoadingToast({
          message: '加载中...',
          forbidClick: true,
          duration: 0
        })

        const params = {
          sortBy: 'createdTime',
          sortDir: 'desc',
          ...this.searchForm
        }

        // 过滤空值
        Object.keys(params).forEach(key => {
          if (params[key] === '' || params[key] === null || params[key] === undefined) {
            delete params[key]
          }
        })

        const token = localStorage.getItem('AdminToken') || localStorage.getItem('Token')
        const response = await axios.get('/api/logs/all', {
          params,
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })

        if (response.data.code == 200) {
          const data = response.data.data
          this.logs = data || []
        } else {
          showToast(response.data.message || '获取日志失败')
        }
      } catch (error) {
        showToast('获取日志失败，请检查网络连接')
      } finally {
        this.loading = false
        closeToast()
      }
    },

    searchLogs() {
      this.fetchLogs()
      this.showSearchPanel = false
    },

    resetSearch() {
      this.searchForm = {
        logType: '',
        userEmail: '',
        operation: '',
        module: '',
        ipAddress: '',
        status: '',
        startTime: '',
        endTime: ''
      }
      this.fetchLogs()
      this.showSearchPanel = false
    },

    formatTime(timeString) {
      if (!timeString) return '-'
      const date = new Date(timeString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    },

    getStatusClass(status) {
      switch (status) {
        case 'SUCCESS':
          return 'status-success'
        case 'FAILED':
          return 'status-failed'
        case 'ERROR':
          return 'status-error'
        default:
          return 'status-unknown'
      }
    },

    getStatusText(status) {
      switch (status) {
        case 'SUCCESS':
          return '成功'
        case 'FAILED':
          return '失败'
        case 'ERROR':
          return '错误'
        default:
          return '未知'
      }
    },

    getLogTypeText(logType) {
      switch (logType) {
        case 'LOGIN':
          return '登录日志'
        case 'OPERATION':
          return '操作日志'
        case 'ERROR':
          return '错误日志'
        case 'SECURITY':
          return '安全日志'
        default:
          return logType
      }
    },

    async viewLogDetail(log) {
      try {
        showLoadingToast({
          message: '加载详情...',
          forbidClick: true,
          duration: 0
        })

        const token = localStorage.getItem('AdminToken') || localStorage.getItem('Token')
        const response = await axios.get(`/api/logs/${log.id}`, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })

        if (response.data.code == 200) {
          this.selectedLog = response.data.data
          this.showLogDetail = true
        } else {
          showToast(response.data.message || '获取日志详情失败')
        }
      } catch (error) {
        showToast('获取日志详情失败')
      } finally {
        closeToast()
      }
    },

    async copyToClipboard(text) {
      try {
        await navigator.clipboard.writeText(text)
        showToast('已复制到剪贴板')
      } catch (error) {
        showToast('复制失败')
      }
    },

    async exportLogs(format = 'json') {
      try {
        showLoadingToast({
          message: '导出中...',
          forbidClick: true,
          duration: 0
        })

        const params = {
          format,
          ...this.searchForm
        }

        // 过滤空值
        Object.keys(params).forEach(key => {
          if (params[key] === '' || params[key] === null || params[key] === undefined) {
            delete params[key]
          }
        })

        const token = localStorage.getItem('AdminToken') || localStorage.getItem('Token')
        const response = await axios.get('/api/logs/export', {
          params,
          responseType: 'blob',
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })

        // 创建下载链接
        const url = window.URL.createObjectURL(new Blob([response.data]))
        const link = document.createElement('a')
        link.href = url
        link.setAttribute('download', `logs.${format}`)
        document.body.appendChild(link)
        link.click()
        link.remove()
        window.URL.revokeObjectURL(url)

        showToast('导出成功')
      } catch (error) {
        console.error('导出失败:', error)
        showToast('导出失败，请检查权限')
      } finally {
        closeToast()
      }
    }
  },
  mounted() {
    this.checkTokenValidity();
    this.fetchLogs();
  }
}
</script>

<template>
  <div class="Admin_Logs">
    <Loading />
    <Header />
    <Admin_Sidebar />

    <main class="main-content">
      <Loading />

      <section class="features">
        <div class="top">
          <button class="search_button" @click="showSearchPanel = true">查询</button>
        </div>

        <!-- 搜索面板 -->
        <div v-if="showSearchPanel" class="search_panel">
          <h3>搜索条件</h3>
          <div class="form-row">
            <div class="form-field">
              <label>用户邮箱</label>
              <input v-model="searchForm.userEmail" type="text" placeholder="请输入用户邮箱">
            </div>
            <div class="form-field">
              <label>操作类型</label>
              <input v-model="searchForm.operation" type="text" placeholder="请输入操作类型">
            </div>
          </div>
          <div class="form-row">
            <div class="form-field">
              <label>IP地址</label>
              <input v-model="searchForm.ipAddress" type="text" placeholder="请输入IP地址">
            </div>
            <div class="form-field">
              <label>日志类型</label>
              <select v-model="searchForm.logType">
                <option v-for="option in logTypeOptions" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-field">
              <label>状态</label>
              <select v-model="searchForm.status">
                <option v-for="option in statusOptions" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>
            <div class="form-field">
              <label>模块</label>
              <select v-model="searchForm.module">
                <option v-for="option in moduleOptions" :key="option.value" :value="option.value">
                  {{ option.text }}
                </option>
              </select>
            </div>
          </div>
          <div class="form-row">
            <div class="form-field">
              <label>开始时间</label>
              <input v-model="searchForm.startTime" type="datetime-local">
            </div>
            <div class="form-field">
              <label>结束时间</label>
              <input v-model="searchForm.endTime" type="datetime-local">
            </div>
          </div>
          <div class="form-actions">
            <button class="reset_button" @click="resetSearch">重置</button>
            <button class="confirm_button" @click="searchLogs">搜索</button>
            <button class="close_button" @click="showSearchPanel = false">关闭</button>
          </div>
        </div>

        <!-- 日志表格 -->
        <table>
          <thead>
            <tr>
              <th class="head log-id">ID</th>
              <th class="head log-type">日志类型</th>
              <th class="head user-email">用户邮箱</th>
              <th class="head operation">操作</th>
              <th class="head module">模块</th>
              <th class="head ip-address">IP地址</th>
              <th class="head status">状态</th>
              <th class="head execution-time">执行时间</th>
              <th class="head created-time">创建时间</th>
              <th class="head actions">操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="logs.length === 0 && !loading">
              <td colspan="10" style="text-align: center; padding: 40px; color: #999;">
                <div style="font-size: 16px;">暂无日志数据</div>
                <div style="font-size: 12px; margin-top: 8px;">请检查搜索条件或刷新页面</div>
              </td>
            </tr>
            <tr v-for="log in logs" :key="log.id">
              <td class="log-id clickable" @click="copyToClipboard(log.id)">{{ log.id }}</td>
              <td class="log-type clickable" @click="copyToClipboard(log.logType)">{{ getLogTypeText(log.logType) }}
              </td>
              <td class="user-email clickable" @click="copyToClipboard(log.userEmail)">{{ log.userEmail || '-' }}</td>
              <td class="operation clickable" @click="copyToClipboard(log.operation)">{{ log.operation }}</td>
              <td class="module clickable" @click="copyToClipboard(log.module)">{{ log.module }}</td>
              <td class="ip-address clickable" @click="copyToClipboard(log.ipAddress)">{{ log.ipAddress }}</td>
              <td class="status">
                <span :class="['status-tag', getStatusClass(log.status)]">
                  {{ getStatusText(log.status) }}
                </span>
              </td>
              <td class="execution-time">{{ log.executionTime || '-' }}ms</td>
              <td class="created-time clickable" @click="copyToClipboard(formatTime(log.createdTime))">{{
                formatTime(log.createdTime) }}</td>
              <td class="actions">
                <button class="detail_button" @click="viewLogDetail(log)">详情</button>
              </td>
            </tr>
          </tbody>
        </table>
      </section>
    </main>

    <!-- 日志详情弹窗 -->
    <div v-if="showLogDetail && selectedLog" class="overlay_1">
      <div class="detail_panel">
        <div class="detail_header">
          <h2>日志详情</h2>
          <button class="close_button" @click="showLogDetail = false">×</button>
        </div>

        <div class="detail_content">
          <div class="detail_row">
            <label>日志ID:</label>
            <span>{{ selectedLog.id }}</span>
          </div>
          <div class="detail_row">
            <label>日志类型:</label>
            <span>{{ getLogTypeText(selectedLog.logType) }}</span>
          </div>
          <div class="detail_row">
            <label>用户ID:</label>
            <span>{{ selectedLog.userId || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>用户邮箱:</label>
            <span>{{ selectedLog.userEmail || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>操作类型:</label>
            <span>{{ selectedLog.operation }}</span>
          </div>
          <div class="detail_row">
            <label>所属模块:</label>
            <span>{{ selectedLog.module }}</span>
          </div>
          <div class="detail_row">
            <label>请求方法:</label>
            <span>{{ selectedLog.requestMethod || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>请求URL:</label>
            <span>{{ selectedLog.requestUrl || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>IP地址:</label>
            <span>{{ selectedLog.ipAddress }}</span>
          </div>
          <div class="detail_row">
            <label>浏览器:</label>
            <span>{{ selectedLog.browser || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>操作系统:</label>
            <span>{{ selectedLog.os || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>地理位置:</label>
            <span>{{ selectedLog.location || '-' }}</span>
          </div>
          <div class="detail_row">
            <label>执行时间:</label>
            <span>{{ selectedLog.executionTime ? `${selectedLog.executionTime}ms` : '-' }}</span>
          </div>
          <div class="detail_row">
            <label>状态:</label>
            <span :class="['status-tag', getStatusClass(selectedLog.status)]">
              {{ getStatusText(selectedLog.status) }}
            </span>
          </div>
          <div class="detail_row">
            <label>创建时间:</label>
            <span>{{ formatTime(selectedLog.createdTime) }}</span>
          </div>
          <div class="detail_row">
            <label>描述:</label>
            <span>{{ selectedLog.description || '-' }}</span>
          </div>

          <!-- 错误信息 -->
          <div v-if="selectedLog.errorMessage" class="detail_section">
            <h4>错误信息</h4>
            <div class="error-message">{{ selectedLog.errorMessage }}</div>
          </div>

          <!-- 请求参数 -->
          <div v-if="selectedLog.requestParams" class="detail_section">
            <h4>请求参数</h4>
            <pre class="params-content">{{ selectedLog.requestParams }}</pre>
          </div>

          <!-- 响应数据 -->
          <div v-if="selectedLog.responseData" class="detail_section">
            <h4>响应数据</h4>
            <pre class="response-content">{{ selectedLog.responseData }}</pre>
          </div>

          <!-- 堆栈信息 -->
          <div v-if="selectedLog.stackTrace" class="detail_section">
            <h4>异常堆栈</h4>
            <pre class="stack-content">{{ selectedLog.stackTrace }}</pre>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.Admin_Logs {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.features {
  margin: 0 1.1rem;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.top {
  display: flex;
  justify-content: flex-end;
  padding: 0 20px;
}

.search_button {
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
  margin-bottom: 0.3rem;
  margin-left: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.search_button:hover {
  background-color: #3c9f3f;
}

.export_button {
  background-color: #4caf50;
  color: white;
}

.export_button:hover {
  background-color: #45a049;
}

.search_panel {
  background: white;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.search_panel h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 15px;
}

.form-field {
  flex: 1;
}

.form-field label {
  display: block;
  margin-bottom: 5px;
  font-weight: 500;
  color: #555;
}

.form-field input,
.form-field select {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.reset_button,
.confirm_button,
.close_button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.reset_button {
  background-color: #f5f5f5;
  color: #666;
}

.confirm_button {
  background-color: #2196f3;
  color: white;
}

.close_button {
  background-color: #f44336;
  color: white;
}

table {
  width: 100%;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-collapse: collapse;
  background: white;
}

.head {
  padding: 1rem 2rem;
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
  font-size: 14px;
}

td.clickable {
  cursor: pointer;
  transition: background-color 0.2s ease;
}

td.clickable:hover {
  background-color: #dcedc8;
}

.status-tag {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.status-success {
  background-color: #e8f5e8;
  color: #4caf50;
}

.status-failed {
  background-color: #fff3e0;
  color: #ff9800;
}

.status-error {
  background-color: #ffebee;
  color: #f44336;
}

.status-unknown {
  background-color: #f5f5f5;
  color: #9e9e9e;
}

.actions {
  padding: 8px;
}

.detail_button {
  height: 2rem;
  width: 5rem;
  cursor: pointer;
  border: none;
  border-radius: 6px;
  background-color: #66bb6a;
  color: white;
  font-weight: 500;
  transition: all 0.2s ease;
  font-size: 13px;
}

.detail_button:hover {
  background-color: #4caf50;
}

.overlay_1 {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.detail_panel {
  background: white;
  border-radius: 8px;
  width: 90%;
  max-width: 800px;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.detail_header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.detail_header h2 {
  margin: 0;
  color: #333;
}

.close_button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: #999;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.detail_content {
  padding: 20px;
}

.detail_row {
  display: flex;
  padding: 10px 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail_row label {
  width: 120px;
  font-weight: 500;
  color: #666;
  flex-shrink: 0;
}

.detail_row span {
  flex: 1;
  color: #333;
}

.detail_section {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #eee;
}

.detail_section h4 {
  margin: 0 0 10px 0;
  color: #333;
  font-size: 16px;
}

.error-message {
  padding: 10px;
  background-color: #ffebee;
  border: 1px solid #ffcdd2;
  border-radius: 4px;
  color: #c62828;
  font-family: monospace;
  font-size: 12px;
}

.params-content,
.response-content,
.stack-content {
  padding: 10px;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
  white-space: pre-wrap;
  word-break: break-all;
  max-height: 200px;
  overflow-y: auto;
}

@media (max-width: 768px) {
  .main-content {
    margin-left: 0;
    padding: 10px;
  }

  .top {
    flex-direction: column;
    gap: 10px;
  }

  .form-row {
    flex-direction: column;
    gap: 10px;
  }

  table {
    font-size: 12px;
  }

  td {
    padding: 8px;
  }

  .detail_row {
    flex-direction: column;
    gap: 5px;
  }

  .detail_row label {
    width: auto;
  }
}

/* 暗黑模式 */
@media (prefers-color-scheme: dark) {
  .top {
    background: #1e1e1e;
  }

  .search_panel {
    background: #1e1e1e;
    color: #e9ecef;
  }

  .search_panel h3 {
    color: #e9ecef;
  }

  .form-field label {
    color: #e9ecef;
  }

  .form-field input,
  .form-field select {
    background-color: #2d3748;
    border-color: #4a5568;
    color: #e9ecef;
  }

  table {
    background: #1e1e1e;
  }

  td {
    background-color: #2d3748;
    color: #e9ecef;
    border-bottom-color: #4a5568;
  }

  td.clickable:hover {
    background-color: #37474f;
  }

  .detail_panel {
    background: #1e1e1e;
    color: #e9ecef;
  }

  .detail_header {
    border-bottom-color: #4a5568;
  }

  .detail_header h2 {
    color: #e9ecef;
  }

  .detail_row {
    border-bottom-color: #4a5568;
  }

  .detail_row label {
    color: #e9ecef;
  }

  .detail_section {
    border-top-color: #4a5568;
  }

  .detail_section h4 {
    color: #e9ecef;
  }
}
</style>