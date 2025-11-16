import axios from "axios"

// 根据环境变量获取API地址
function getApiBaseUrl() {
  const hostname = window.location.hostname
  const prodApi = 'https://zyroo.cn/backend/'

  // 生产环境安全校验 - 强制使用生产API，不允许覆盖
  if (hostname === 'zyroo.cn' || hostname === 'www.zyroo.cn' || hostname.includes('zyroo.cn')) {
    // 检查是否有非生产API的环境变量
    if (import.meta.env.VITE_API_BASE_URL && import.meta.env.VITE_API_BASE_URL !== prodApi) {
      console.warn('⚠️ 安全警告：生产环境检测到非生产API地址，已强制切换到生产API')
      console.warn(`🔒 原地址: ${import.meta.env.VITE_API_BASE_URL}`)
      console.warn(`✅ 强制切换到: ${prodApi}`)
    }
    return prodApi
  }

  // 非生产环境：优先使用指定API地址
  if (import.meta.env.VITE_API_BASE_URL) {
    return import.meta.env.VITE_API_BASE_URL
  }

  // 开发环境：自动判断API地址
  if (import.meta.env.VITE_APP_ENV === 'dev') {
    // 如果是localhost访问，使用localhost
    if (hostname === 'localhost' || hostname === '127.0.0.1') {
      return 'http://localhost:8085'
    }

    // 如果是IP访问，使用相同IP
    return `http://${hostname}:8085`
  }

  // 默认根据域名自动判断
  return `https://${hostname}/backend/`
}

// 环境一致性检查
function validateEnvironment() {
  const hostname = window.location.hostname
  const apiUrl = getApiBaseUrl()

  // 检查环境和API是否匹配
  if ((hostname === 'zyroo.cn' || hostname === 'www.zyroo.cn' || hostname.includes('zyroo.cn')) && !apiUrl.includes('zyroo.cn/backend/')) {
    console.error('🚨 安全错误：生产环境不能连接非生产API')
    console.error(`📍 当前域名: ${hostname}`)
    console.error(`🔗 API地址: ${apiUrl}`)
    console.error('⚡ 请检查环境配置')
  }
}

// 页面加载时执行环境验证
if (typeof window !== 'undefined') {
  validateEnvironment()
}

// 设置axios默认baseURL
axios.defaults.baseURL = getApiBaseUrl()

// 导出获取baseURL的函数，供其他模块使用
export { getApiBaseUrl }
