<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import { showToast, showLoadingToast, closeToast } from 'vant'
import axios from 'axios'
import Loading from '@/modules/common/components/Loading.vue'
import Header from '@/modules/common/components/Header.vue'
import Admin_Sidebar from '@/modules/admin/components/Admin_Sidebar.vue'
import '@/modules/common/utils/api-config.js'

const statistics = ref({})
const loading = ref(false)
const timeRange = ref('7d')

const timeRangeOptions = [
  { text: '最近24小时', value: '1d' },
  { text: '最近7天', value: '7d' },
  { text: '最近30天', value: '30d' },
  { text: '最近90天', value: '90d' }
]

const activeUsers = ref([])
const frequentOperations = ref([])


const getTimeRange = (range) => {
  const now = new Date()
  let startTime

  switch (range) {
    case '1d':
      startTime = new Date(now.getTime() - 24 * 60 * 60 * 1000)
      break
    case '7d':
      startTime = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
      break
    case '30d':
      startTime = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
      break
    case '90d':
      startTime = new Date(now.getTime() - 90 * 24 * 60 * 60 * 1000)
      break
    default:
      startTime = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
  }

  return {
    startTime: startTime.toISOString().slice(0, 19),
    endTime: now.toISOString().slice(0, 19)
  }
}

const fetchStatistics = async () => {
  try {
    loading.value = true
    showLoadingToast({
      message: '加载统计中...',
      forbidClick: true,
      duration: 0
    })

    const { startTime, endTime } = getTimeRange(timeRange.value)

    const token = localStorage.getItem('AdminToken') || localStorage.getItem('Token')
    const response = await axios.get('/api/logs/statistics', {
      params: {
        startTime,
        endTime
      },
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })


    if (response.data.code == 200) {
      statistics.value = response.data.data
    } else {
      showToast(response.data.message || '获取统计数据失败')
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
    showToast('获取统计数据失败')
  } finally {
    loading.value = false
    closeToast()
  }
}

const fetchActiveUsers = async () => {
  try {
    const { startTime, endTime } = getTimeRange(timeRange.value)

    const token = localStorage.getItem('AdminToken') || localStorage.getItem('Token')
    const response = await axios.get('/api/logs/analytics/active-users', {
      params: {
        limit: 10
      },
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (response.data.code == 200) {
      activeUsers.value = response.data.data || []
      console.log('活跃用户数据:', activeUsers.value)
      console.log('活跃用户数组长度:', activeUsers.value.length)
    } else {
      console.error('活跃用户API错误:', response.data.message)
    }
  } catch (error) {
    console.error('获取活跃用户失败:', error)
  }
}



const fetchFrequentOperations = async () => {
  try {
    const { startTime, endTime } = getTimeRange(timeRange.value)

    const token = localStorage.getItem('AdminToken') || localStorage.getItem('Token')
    const response = await axios.get('/api/logs/analytics/frequent-operations', {
      params: {
        limit: 10
      },
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (response.data.code == 200) {
      frequentOperations.value = response.data.data || []
      console.log('频繁操作数据:', frequentOperations.value)
      console.log('频繁操作数组长度:', frequentOperations.value.length)
    } else {
      console.error('频繁操作API错误:', response.data.message)
    }
  } catch (error) {
    console.error('获取频繁操作失败:', error)
  }
}

const refreshData = async () => {

  try {
    await Promise.all([
      fetchStatistics(),
      fetchActiveUsers(),
      fetchFrequentOperations()
    ])
    console.log('数据刷新完成')
  } catch (error) {
    console.error('数据刷新失败:', error)
  }
}

const handleTimeRangeChange = () => {
  refreshData()
}

const successRate = computed(() => {
  const total = statistics.value.totalCount || 0
  const successCount = statistics.value.successCount || 0
  console.log(statistics.value);

  return total > 0 ? ((successCount / total) * 100).toFixed(2) : 0
})

const errorRate = computed(() => {
  const total = statistics.value.totalCount || 0
  const errorCount = statistics.value.errorCount || 0
  return total > 0 ? ((errorCount / total) * 100).toFixed(2) : 0
})

const formatNumber = (num) => {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + 'M'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + 'K'
  }
  return num.toString()
}

const getLogTypeColor = (type) => {
  const colors = {
    'LOGIN': '#1989fa',
    'OPERATION': '#07c160',
    'ERROR': '#ee0a24',
    'SECURITY': '#ff976a'
  }
  return colors[type] || '#969799'
}

const getLogTypeName = (type) => {
  const names = {
    'LOGIN': '登录日志',
    'OPERATION': '操作日志',
    'ERROR': '错误日志',
    'SECURITY': '安全日志'
  }
  return names[type] || type
}

const getPieSliceStyle = (type, count) => {
  const total = Object.values(statistics.value.logTypeStats || {}).reduce((sum, val) => sum + val, 0)
  const percentage = total > 0 ? (count / total) * 100 : 0
  return {
    backgroundColor: getLogTypeColor(type),
    width: percentage + '%'
  }
}

const getPieChartBackground = () => {
  if (!statistics.value.logTypeStats) return '#f5f5f5'

  const colors = Object.keys(statistics.value.logTypeStats).map(type => getLogTypeColor(type))
  const counts = Object.values(statistics.value.logTypeStats)
  const total = counts.reduce((sum, val) => sum + val, 0)

  if (total === 0) return '#f5f5f5'

  let currentAngle = 0
  const gradientStops = []

  counts.forEach((count, index) => {
    const percentage = (count / total) * 100
    const angle = (percentage / 100) * 360

    gradientStops.push(`${colors[index]} ${currentAngle}deg ${currentAngle + angle}deg`)
    currentAngle += angle
  })

  return `conic-gradient(from 0deg, ${gradientStops.join(', ')})`
}

const getTopOperations = (operations, limit) => {
  const sorted = Object.entries(operations).sort((a, b) => b[1] - a[1])
  return Object.fromEntries(sorted.slice(0, limit))
}

const getOperationPercentage = (count) => {
  const total = Object.values(statistics.value.operationStats || {}).reduce((sum, val) => sum + val, 0)
  return total > 0 ? Math.min((count / total) * 100, 100) : 0
}

const getOperationColor = (operation) => {
  const colors = {
    '用户登录': '#1989fa',
    '用户注册': '#07c160',
    '查询操作': '#ff976a',
    '更新操作': '#ff6034',
    '删除操作': '#ee0a24',
    '其他操作': '#969799'
  }
  return colors[operation] || '#1989fa'
}

const getHealthClass = (value) => {
  if (value >= 95) return 'excellent'
  if (value >= 85) return 'good'
  if (value >= 70) return 'warning'
  return 'danger'
}

onMounted(() => {
  refreshData()
})
</script>

<template>
  <div class="Admin_LogStatistics">
    <Loading />
    <Header />
    <Admin_Sidebar />

    <main class="main-content">
      <Loading />

      <section class="statistics-page">
        <div class="page-header">
          <h1>日志统计</h1>
          <div class="header-actions">
            <select v-model="timeRange" @change="handleTimeRangeChange" class="time-range-select">
              <option v-for="option in timeRangeOptions" :key="option.value" :value="option.value">
                {{ option.text }}
              </option>
            </select>
            <button class="refresh-button" @click="refreshData">
              <van-icon name="replay" />
              刷新
            </button>
          </div>
        </div>

        <div class="stats-cards">
          <div class="stat-card">
            <div class="stat-icon total">
              <van-icon name="chart-trending-o" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatNumber(statistics.totalCount || 0) }}</div>
              <div class="stat-label">总日志数</div>
            </div>
          </div>

          <div class="stat-card">
            <div class="stat-icon success">
              <van-icon name="success" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ successRate }}%</div>
              <div class="stat-label">成功率</div>
            </div>
          </div>

          <div class="stat-card">
            <div class="stat-icon error">
              <van-icon name="fail" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ errorRate }}%</div>
              <div class="stat-label">错误率</div>
            </div>
          </div>

          <div class="stat-card">
            <div class="stat-icon users">
              <van-icon name="friends-o" />
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ formatNumber(statistics.uniqueUsers || 0) }}</div>
              <div class="stat-label">活跃用户</div>
            </div>
          </div>
        </div>

        <div class="charts-container">
          <div class="chart-card">
            <h3>日志类型分布</h3>
            <div class="chart-content">
              <div v-if="statistics.logTypeStats" class="log-type-chart">
                <div class="pie-chart-container">
                  <div class="pie-chart" :style="{ background: getPieChartBackground() }">
                  </div>
                </div>
                <div class="chart-legend">
                  <div v-for="(count, type) in statistics.logTypeStats" :key="type" class="legend-item">
                    <div class="legend-color" :style="{ backgroundColor: getLogTypeColor(type) }"></div>
                    <div class="legend-text">
                      <div class="legend-name">{{ getLogTypeName(type) }}</div>
                      <div class="legend-value">{{ formatNumber(count) }} ({{ ((count / statistics.totalCount) *
                        100).toFixed(1) }}%)</div>
                    </div>
                  </div>
                </div>
              </div>
              <div v-else class="no-data">暂无数据</div>
            </div>
          </div>

          <div class="chart-card">
            <h3>操作类型分布</h3>
            <div class="chart-content">
              <div v-if="statistics.operationStats" class="operation-chart">
                <div v-for="(count, operation) in getTopOperations(statistics.operationStats, 6)" :key="operation"
                  class="operation-bar">
                  <div class="operation-info">
                    <div class="operation-name">{{ operation }}</div>
                    <div class="operation-count">{{ formatNumber(count) }}</div>
                  </div>
                  <div class="progress-bar">
                    <div class="progress-fill"
                      :style="{ width: getOperationPercentage(count) + '%', backgroundColor: getOperationColor(operation) }">
                    </div>
                  </div>
                </div>
                <div v-if="Object.keys(statistics.operationStats).length > 6" class="more-operations">
                  还有 {{ Object.keys(statistics.operationStats).length - 6 }} 个操作类型...
                </div>
              </div>
              <div v-else class="no-data">暂无数据</div>
            </div>
          </div>
          <div class="chart-card">
            <h3>用户活跃度</h3>
            <div class="chart-content">
              <div class="user-activity">
                <div class="activity-item">
                  <div class="activity-icon users">
                    <van-icon name="friends-o" />
                  </div>
                  <div class="activity-content">
                    <div class="activity-value">{{ formatNumber(statistics.uniqueUsers || 0) }}</div>
                    <div class="activity-label">活跃用户</div>
                  </div>
                  <div class="activity-trend">
                    <van-icon name="arrow-up" color="#07c160" />
                    <span class="trend-value">+12%</span>
                  </div>
                </div>
                <div class="activity-item">
                  <div class="activity-icon ips">
                    <van-icon name="location-o" />
                  </div>
                  <div class="activity-content">
                    <div class="activity-value">{{ formatNumber(statistics.uniqueIps || 0) }}</div>
                    <div class="activity-label">独立IP</div>
                  </div>
                  <div class="activity-trend">
                    <van-icon name="arrow-up" color="#07c160" />
                    <span class="trend-value">+8%</span>
                  </div>
                </div>
                <div class="activity-item">
                  <div class="activity-icon requests">
                    <van-icon name="description" />
                  </div>
                  <div class="activity-content">
                    <div class="activity-value">{{ formatNumber(statistics.totalCount || 0) }}</div>
                    <div class="activity-label">总请求数</div>
                  </div>
                  <div class="activity-trend">
                    <van-icon name="arrow-up" color="#07c160" />
                    <span class="trend-value">+25%</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="detailed-stats">
          <div class="stats-table">
            <h3>最活跃用户</h3>
            <div class="table-container">
              <table>
                <thead>
                  <tr>
                    <th>排名</th>
                    <th>用户邮箱</th>
                    <th>操作次数</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(user, index) in activeUsers" :key="user[0] || index">
                    <td class="rank">{{ index + 1 }}</td>
                    <td class="user-email">{{ user[0] || '未知用户' }}</td>
                    <td class="count">{{ formatNumber(user[1] || 0) }}</td>
                  </tr>
                </tbody>
              </table>
              <div v-if="activeUsers.length == 0" class="no-data">暂无数据</div>
            </div>
          </div>

          <div class="stats-table">
            <h3>最频繁操作</h3>
            <div class="table-container">
              <table>
                <thead>
                  <tr>
                    <th>排名</th>
                    <th>操作类型</th>
                    <th>执行次数</th>
                    <th>占比</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(operation, index) in frequentOperations" :key="operation[0] || index">
                    <td class="rank">{{ index + 1 }}</td>
                    <td class="operation-name">{{ operation[0] || '未知操作' }}</td>
                    <td class="count">{{ formatNumber(operation[1] || 0) }}</td>
                    <td class="percentage">
                      {{ statistics.totalCount > 0 ? ((operation[1] / statistics.totalCount) * 100).toFixed(1) : 0 }}%
                    </td>
                  </tr>
                </tbody>
              </table>
              <div v-if="frequentOperations.length == 0" class="no-data">
                暂无数据
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<style scoped>
.Admin_LogStatistics {
  min-height: 100vh;
  background-color: #f7f8fa;
}

.statistics-page {
  max-width: 1400px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.page-header h1 {
  margin: 0;
  color: #323233;
  font-size: 24px;
}

.header-actions {
  display: flex;
  gap: 15px;
  align-items: center;
}

.refresh-button {
  display: flex;
  align-items: center;
  gap: 5px;
  width: 5rem;
  padding: 0.4rem 0.6rem;
  border: none;
  border-radius: 6px;
  background-color: #1989fa;
  color: white;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.refresh-button:hover {
  background-color: #1a7fe8;
}

.debug-button {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  background-color: #ff976a;
  color: white;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.debug-button:hover {
  background-color: #ff8445;
}

.test-button {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  background-color: #ee0a24;
  color: white;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.test-button:hover {
  background-color: #d6081a;
}

.time-range-select {
  padding: 8px 16px;
  border: 1px solid #ebedf0;
  border-radius: 6px;
  background-color: white;
  font-size: 14px;
  color: #323233;
  cursor: pointer;
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  font-size: 24px;
  color: white;
}

.stat-icon.total {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.stat-icon.success {
  background: linear-gradient(135deg, #07c160 0%, #06ad56 100%);
}

.stat-icon.error {
  background: linear-gradient(135deg, #ee0a24 0%, #d6081a 100%);
}

.stat-icon.users {
  background: linear-gradient(135deg, #1989fa 0%, #1a7fe8 100%);
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #323233;
  line-height: 1;
}

.stat-label {
  font-size: 14px;
  color: #969799;
  margin-top: 5px;
}

.charts-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.chart-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.chart-card h3 {
  margin: 0;
  padding: 20px;
  color: #323233;
  font-size: 18px;
  border-bottom: 1px solid #ebedf0;
}

.chart-content {
  padding: 20px;
  min-height: 200px;
}

.log-type-stats,
.operation-stats {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-color {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  flex-shrink: 0;
}

.stat-info {
  flex: 1;
}

.stat-name {
  font-size: 14px;
  color: #323233;
  margin-bottom: 2px;
}

.stat-count {
  font-size: 16px;
  font-weight: 600;
  color: #323233;
}

.stat-percentage {
  font-size: 14px;
  color: #969799;
}

.no-data {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 150px;
  color: #969799;
  font-size: 14px;
}

.detailed-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stats-table {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.stats-table h3 {
  margin: 0;
  padding: 20px;
  color: #323233;
  font-size: 18px;
  border-bottom: 1px solid #ebedf0;
}

.table-container {
  overflow-x: auto;
}

.stats-table table {
  width: 100%;
  border-collapse: collapse;
}

.stats-table th,
.stats-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ebedf0;
  font-size: 14px;
}

.stats-table th {
  background-color: #fafafa;
  font-weight: 600;
  color: #323233;
}

.rank {
  font-weight: 600;
  color: #1989fa;
}

.user-id,
.user-email,
.operation-name {
  color: #646566;
}

.count {
  font-weight: 600;
  color: #323233;
}

.percentage {
  color: #07c160;
}

.additional-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.stat-item-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 20px;
  text-align: center;
}

.stat-item-card h4 {
  margin: 0 0 10px 0;
  color: #646566;
  font-size: 14px;
  font-weight: normal;
}

.stat-item-card .value {
  font-size: 24px;
  font-weight: bold;
  color: #323233;
}

.log-type-chart {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.pie-chart-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.pie-chart {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  background: conic-gradient(from 0deg,
      #1989fa 0deg,
      #07c160 90deg,
      #ff976a 180deg,
      #ee0a24 270deg,
      #1989fa 360deg);
  position: relative;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.pie-chart::before {
  content: '';
  position: absolute;
  inset: 25%;
  background: white;
  border-radius: 50%;
}

.chart-legend {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.legend-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  flex-shrink: 0;
}

.legend-text {
  flex: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.legend-name {
  font-size: 14px;
  color: #323233;
}

.legend-value {
  font-size: 13px;
  color: #969799;
  font-weight: 500;
}

.operation-chart {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.operation-bar {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.operation-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.operation-name {
  font-size: 14px;
  color: #323233;
  font-weight: 500;
}

.operation-count {
  font-size: 13px;
  color: #646566;
  font-weight: 600;
}

.progress-bar {
  height: 8px;
  background-color: #f2f3f5;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.6s ease;
  min-width: 2px;
}

.more-operations {
  text-align: center;
  font-size: 13px;
  color: #969799;
  padding: 8px;
  background-color: #f7f8fa;
  border-radius: 4px;
}

.health-metrics {
  display: flex;
  justify-content: space-around;
  align-items: center;
  padding: 20px 0;
}

.metric-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.metric-circle {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border: 4px solid #f0f0f0;
  transition: all 0.3s ease;
  position: relative;
}

.metric-circle.excellent {
  border-color: #07c160;
  background: linear-gradient(135deg, rgba(7, 193, 96, 0.1) 0%, rgba(7, 193, 96, 0.05) 100%);
}

.metric-circle.good {
  border-color: #1989fa;
  background: linear-gradient(135deg, rgba(25, 137, 250, 0.1) 0%, rgba(25, 137, 250, 0.05) 100%);
}

.metric-circle.warning {
  border-color: #ff976a;
  background: linear-gradient(135deg, rgba(255, 151, 106, 0.1) 0%, rgba(255, 151, 106, 0.05) 100%);
}

.metric-circle.danger {
  border-color: #ee0a24;
  background: linear-gradient(135deg, rgba(238, 10, 36, 0.1) 0%, rgba(238, 10, 36, 0.05) 100%);
}

.metric-circle.performance {
  border-color: #7232dd;
  background: linear-gradient(135deg, rgba(114, 50, 221, 0.1) 0%, rgba(114, 50, 221, 0.05) 100%);
}

.metric-value {
  font-size: 20px;
  font-weight: bold;
  color: #323233;
  line-height: 1;
}

.metric-label {
  font-size: 12px;
  color: #969799;
  margin-top: 4px;
}

.user-activity {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background-color: #fafafa;
  border-radius: 8px;
  border-left: 4px solid transparent;
  transition: all 0.3s ease;
}

.activity-item:hover {
  background-color: #f0f2f5;
  transform: translateX(2px);
}

.activity-icon {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 18px;
  color: white;
}

.activity-icon.users {
  background: linear-gradient(135deg, #1989fa 0%, #1a7fe8 100%);
}

.activity-icon.ips {
  background: linear-gradient(135deg, #ff976a 0%, #ff8445 100%);
}

.activity-icon.requests {
  background: linear-gradient(135deg, #07c160 0%, #06ad56 100%);
}

.activity-content {
  flex: 1;
}

.activity-value {
  font-size: 18px;
  font-weight: bold;
  color: #323233;
  line-height: 1;
}

.activity-label {
  font-size: 13px;
  color: #969799;
  margin-top: 2px;
}

.activity-trend {
  display: flex;
  align-items: center;
  gap: 4px;
}

.trend-value {
  font-size: 13px;
  font-weight: 600;
  color: #07c160;
}

@media (max-width: 768px) {
  .main-content {
    margin-left: 0;
    padding: 10px;
  }

  .page-header {
    flex-direction: column;
    gap: 15px;
    align-items: stretch;
  }

  .header-actions {
    justify-content: center;
  }

  .stats-cards {
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 10px;
  }

  .stat-card {
    padding: 15px;
  }

  .stat-icon {
    width: 40px;
    height: 40px;
    font-size: 20px;
    margin-right: 10px;
  }

  .stat-value {
    font-size: 24px;
  }

  .charts-container {
    grid-template-columns: 1fr;
  }

  .health-metrics {
    flex-direction: column;
    gap: 20px;
  }

  .metric-circle {
    width: 80px;
    height: 80px;
  }

  .metric-value {
    font-size: 16px;
  }

  .activity-item {
    padding: 12px;
  }

  .activity-icon {
    width: 35px;
    height: 35px;
    font-size: 16px;
  }

  .detailed-stats {
    grid-template-columns: 1fr;
  }

  .stats-table table {
    font-size: 12px;
  }

  .stats-table th,
  .stats-table td {
    padding: 8px;
  }
}
</style>